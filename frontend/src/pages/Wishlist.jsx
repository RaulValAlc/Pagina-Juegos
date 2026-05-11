import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function Wishlist() {
  const [items, setItems] = useState([])
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ name: '', coverUrl: '', steamUrl: '', enebaUrl: '' })

  useEffect(() => { loadWishlist() }, [])

  const loadWishlist = async () => {
    try {
      const { data } = await api.get('/wishlist')
      setItems(data)
    } catch (err) {
      console.error(err)
    }
  }

  const handleAdd = async e => {
    e.preventDefault()
    try {
      await api.post('/wishlist', form)
      setForm({ name: '', coverUrl: '', steamUrl: '', enebaUrl: '' })
      setShowForm(false)
      loadWishlist()
    } catch (err) {
      console.error(err)
    }
  }

  const handleDelete = async id => {
    if (confirm('Eliminar de la wishlist?')) {
      try {
        await api.delete(`/wishlist/${id}`)
        loadWishlist()
      } catch (err) {
        console.error(err)
      }
    }
  }

  return (
    <div className="wishlist-page">
      <div className="page-header">
        <h1 className="page-title">Lista de Deseados</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : '+ Añadir'}
        </button>
      </div>

      {showForm && (
        <form className="game-form" onSubmit={handleAdd}>
          <input className="input" placeholder="Nombre *" value={form.name} onChange={e => setForm({...form, name: e.target.value})} required />
          <input className="input" placeholder="URL portada" value={form.coverUrl} onChange={e => setForm({...form, coverUrl: e.target.value})} />
          <input className="input" placeholder="URL Steam" value={form.steamUrl} onChange={e => setForm({...form, steamUrl: e.target.value})} />
          <input className="input" placeholder="URL Eneba" value={form.enebaUrl} onChange={e => setForm({...form, enebaUrl: e.target.value})} />
          <button type="submit" className="btn btn-primary">Guardar</button>
        </form>
      )}

      <div className="wishlist-grid">
        {items.map(item => (
          <div key={item.id} className="wishlist-card">
            <div className="wishlist-cover">
              {item.coverUrl ? (
                <img src={item.coverUrl} alt={item.name} />
              ) : (
                <div className="wishlist-placeholder">{item.name[0]}</div>
              )}
            </div>
            <div className="wishlist-info">
              <h3>{item.name}</h3>
              <div className="wishlist-prices">
                {item.steamPrice && (
                  <div className="price-item">
                    <span className="price-label">Steam</span>
                    <span className={`price-value ${item.onSale ? 'sale' : ''}`}>
                      ${item.steamPrice.toFixed(2)}
                      {item.onSale && <span className="discount-badge">-{item.discountPercent}%</span>}
                    </span>
                  </div>
                )}
                {item.enebaPrice && (
                  <div className="price-item">
                    <span className="price-label">Eneba</span>
                    <span className="price-value">${item.enebaPrice.toFixed(2)}</span>
                  </div>
                )}
              </div>
              <div className="wishlist-actions">
                {item.steamUrl && (
                  <a href={item.steamUrl} target="_blank" rel="noopener noreferrer" className="btn btn-sm">Steam</a>
                )}
                {item.enebaUrl && (
                  <a href={item.enebaUrl} target="_blank" rel="noopener noreferrer" className="btn btn-sm">Eneba</a>
                )}
                <button className="btn btn-sm btn-danger" onClick={() => handleDelete(item.id)}>Eliminar</button>
              </div>
            </div>
          </div>
        ))}
        {items.length === 0 && <p className="text-muted">No hay juegos en la wishlist</p>}
      </div>
    </div>
  )
}
