import { useState, useEffect } from 'react'
import api from '../api/axios'

export default function Ranking() {
  const [rankings, setRankings] = useState([])
  const [listName, setListName] = useState('Top 10')
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ listName: 'Top 10', gameId: '', position: '' })
  const [games, setGames] = useState([])

  useEffect(() => { loadRankings() }, [listName])

  const loadRankings = async () => {
    try {
      const { data } = await api.get('/rankings', { params: { listName } })
      setRankings(data)
    } catch (err) {
      console.error(err)
    }
  }

  const loadGames = async () => {
    try {
      const { data } = await api.get('/games', { params: { size: 50 } })
      setGames(data.content || [])
    } catch (err) {
      console.error(err)
    }
  }

  const handleShowForm = () => {
    loadGames()
    setShowForm(!showForm)
  }

  const handleAdd = async e => {
    e.preventDefault()
    try {
      await api.post('/rankings', { ...form, listName })
      setForm({ listName: 'Top 10', gameId: '', position: '' })
      setShowForm(false)
      loadRankings()
    } catch (err) {
      console.error(err)
    }
  }

  const handleRemove = async id => {
    if (confirm('Eliminar de este ranking?')) {
      try {
        await api.delete(`/rankings/${id}`)
        loadRankings()
      } catch (err) {
        console.error(err)
      }
    }
  }

  const handleReorder = async (id, position) => {
    try {
      await api.patch(`/rankings/${id}/reorder`, null, { params: { position } })
      loadRankings()
    } catch (err) {
      console.error(err)
    }
  }

  return (
    <div className="ranking-page">
      <div className="page-header">
        <h1 className="page-title">Rankings</h1>
        <div className="ranking-controls">
          <select className="input" value={listName} onChange={e => setListName(e.target.value)}>
            <option>Top 10</option>
            <option>Favoritos</option>
            <option>Recomendados</option>
          </select>
          <button className="btn btn-primary" onClick={handleShowForm}>
            {showForm ? 'Cancelar' : '+ Añadir juego'}
          </button>
        </div>
      </div>

      {showForm && (
        <form className="game-form" onSubmit={handleAdd}>
          <select className="input" value={form.gameId} onChange={e => setForm({...form, gameId: e.target.value})} required>
            <option value="">Seleccionar juego</option>
            {games.map(g => (
              <option key={g.id} value={g.id}>{g.name}</option>
            ))}
          </select>
          <input className="input" type="number" placeholder="Posición" value={form.position} onChange={e => setForm({...form, position: e.target.value})} />
          <button type="submit" className="btn btn-primary">Añadir</button>
        </form>
      )}

      <div className="ranking-list">
        {rankings.sort((a, b) => a.position - b.position).map((item, i) => (
          <div key={item.id} className="ranking-item">
            <span className="ranking-position">#{item.position || i + 1}</span>
            <div className="ranking-cover">
              {item.coverUrl ? <img src={item.coverUrl} alt={item.gameName} /> : <span>{item.gameName[0]}</span>}
            </div>
            <span className="ranking-name">{item.gameName}</span>
            <div className="ranking-actions">
              <button className="btn btn-sm" onClick={() => handleReorder(item.id, Math.max(1, item.position - 1))} disabled={item.position <= 1}>↑</button>
              <button className="btn btn-sm" onClick={() => handleReorder(item.id, item.position + 1)}>↓</button>
              <button className="btn btn-sm btn-danger" onClick={() => handleRemove(item.id)}>✕</button>
            </div>
          </div>
        ))}
        {rankings.length === 0 && <p className="text-muted">No hay juegos en este ranking</p>}
      </div>
    </div>
  )
}
