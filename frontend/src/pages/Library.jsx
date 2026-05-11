import { useState, useEffect, useRef } from 'react'
import api from '../api/axios'
import GameCard from '../components/GameCard'

const statuses = ['ALL', 'PLAYED', 'PENDING', 'WISHED', 'PURCHASED', 'ABANDONED']
const statusLabels = { ALL: 'Todas', PLAYED: 'Jugados', PENDING: 'Pendientes', WISHED: 'Deseados', PURCHASED: 'Comprados', ABANDONED: 'Abandonados' }

export default function Library() {
  const [games, setGames] = useState([])
  const [status, setStatus] = useState('ALL')
  const [search, setSearch] = useState('')
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [loading, setLoading] = useState(true)
  const [showForm, setShowForm] = useState(false)
  const [form, setForm] = useState({ name: '', genre: '', platform: '', coverUrl: '', status: 'PENDING' })

  const [rawgQuery, setRawgQuery] = useState('')
  const [rawgResults, setRawgResults] = useState([])
  const [rawgLoading, setRawgLoading] = useState(false)
  const [showRawgResults, setShowRawgResults] = useState(false)
  const [rawgError, setRawgError] = useState('')
  const rawgRef = useRef(null)

  useEffect(() => {
    loadGames()
  }, [status, page, search])

  useEffect(() => {
    function handleClickOutside(e) {
      if (rawgRef.current && !rawgRef.current.contains(e.target)) {
        setShowRawgResults(false)
      }
    }
    document.addEventListener('mousedown', handleClickOutside)
    return () => document.removeEventListener('mousedown', handleClickOutside)
  }, [])

  const loadGames = async () => {
    setLoading(true)
    try {
      const params = { page, size: 12 }
      if (status !== 'ALL') params.status = status
      if (search) params.search = search
      const { data } = await api.get('/games', { params })
      setGames(data.content)
      setTotalPages(data.totalPages)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const handleAddGame = async e => {
    e.preventDefault()
    try {
      await api.post('/games', form)
      setShowForm(false)
      setForm({ name: '', genre: '', platform: '', coverUrl: '', status: 'PENDING' })
      loadGames()
    } catch (err) {
      console.error(err)
    }
  }

  const searchRawg = async () => {
    if (!rawgQuery.trim()) return
    setRawgLoading(true)
    setRawgError('')
    setRawgResults([])
    setShowRawgResults(true)
    try {
      const { data } = await api.get('/rawg/search', { params: { query: rawgQuery } })
      setRawgResults(data)
    } catch (err) {
      setRawgError('Error al buscar en RAWG')
      console.error(err)
    } finally {
      setRawgLoading(false)
    }
  }

  const selectRawgGame = (game) => {
    setForm({
      name: game.name,
      genre: game.genres,
      platform: game.platforms ? game.platforms.split(', ')[0] : '',
      coverUrl: game.coverUrl || '',
      status: form.status,
      developer: game.developers,
      releaseDate: game.released,
      globalRating: game.rating,
      rawgId: game.rawgId,
    })
    setShowRawgResults(false)
    setRawgQuery('')
  }

  const handleRawgKeyDown = (e) => {
    if (e.key === 'Enter') {
      e.preventDefault()
      searchRawg()
    }
  }

  return (
    <div className="library-page">
      <div className="page-header">
        <h1 className="page-title">Biblioteca</h1>
        <button className="btn btn-primary" onClick={() => setShowForm(!showForm)}>
          {showForm ? 'Cancelar' : '+ Añadir juego'}
        </button>
      </div>

      {showForm && (
        <form className="game-form" onSubmit={handleAddGame}>
          <div className="rawg-search-wrapper" ref={rawgRef}>
            <div className="rawg-search-row">
              <input className="input" placeholder="Nombre del juego *" value={form.name}
                onChange={e => { setForm({...form, name: e.target.value}); setRawgQuery(e.target.value) }}
                onKeyDown={handleRawgKeyDown} required />
              <button type="button" className="btn btn-primary btn-rawg-search"
                onClick={searchRawg} disabled={rawgLoading || !rawgQuery.trim()}>
                {rawgLoading ? <span className="btn-loader" /> : 'RAWG'}
              </button>
            </div>
            {showRawgResults && (
              <div className="rawg-results">
                {rawgLoading && <div className="rawg-results-loading"><span className="spinner" /></div>}
                {rawgError && <div className="rawg-results-error">{rawgError}</div>}
                {!rawgLoading && !rawgError && rawgResults.length === 0 && (
                  <div className="rawg-results-empty">Sin resultados</div>
                )}
                {!rawgLoading && rawgResults.map(game => (
                  <div key={game.rawgId} className="rawg-result-item" onClick={() => selectRawgGame(game)}>
                    <div className="rawg-result-cover">
                      {game.coverUrl ? (
                        <img src={game.coverUrl} alt={game.name} />
                      ) : (
                        <span>{game.name.charAt(0)}</span>
                      )}
                    </div>
                    <div className="rawg-result-info">
                      <div className="rawg-result-name">{game.name}</div>
                      <div className="rawg-result-meta">
                        {game.platforms && <span>{game.platforms}</span>}
                        {game.released && <span>{game.released}</span>}
                        {game.rating > 0 && <span className="rawg-result-rating">{game.rating.toFixed(1)}</span>}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
          <input className="input" placeholder="Género" value={form.genre} onChange={e => setForm({...form, genre: e.target.value})} />
          <input className="input" placeholder="Plataforma" value={form.platform} onChange={e => setForm({...form, platform: e.target.value})} />
          <input className="input" placeholder="URL de portada" value={form.coverUrl} onChange={e => setForm({...form, coverUrl: e.target.value})} />
          <select className="input" value={form.status} onChange={e => setForm({...form, status: e.target.value})}>
            {statuses.filter(s => s !== 'ALL').map(s => (
              <option key={s} value={s}>{statusLabels[s]}</option>
            ))}
          </select>
          <button type="submit" className="btn btn-primary">Guardar</button>
        </form>
      )}

      <div className="filters">
        <div className="status-tabs">
          {statuses.map(s => (
            <button key={s} className={`btn btn-sm ${status === s ? 'btn-primary' : 'btn-ghost'}`} onClick={() => { setStatus(s); setPage(0) }}>
              {statusLabels[s]}
            </button>
          ))}
        </div>
        <input className="input search-input" placeholder="Buscar juegos..." value={search} onChange={e => { setSearch(e.target.value); setPage(0) }} />
      </div>

      {loading ? (
        <div className="loading-screen"><div className="spinner" /></div>
      ) : games.length === 0 ? (
        <p className="text-muted">No hay juegos todavía</p>
      ) : (
        <>
          <div className="games-grid">
            {games.map(game => (
              <GameCard key={game.id} game={game} onUpdate={loadGames} onDelete={() => loadGames()} />
            ))}
          </div>
          {totalPages > 1 && (
            <div className="pagination">
              <button className="btn btn-sm" disabled={page === 0} onClick={() => setPage(p => p - 1)}>Anterior</button>
              <span>Página {page + 1} de {totalPages}</span>
              <button className="btn btn-sm" disabled={page >= totalPages - 1} onClick={() => setPage(p => p + 1)}>Siguiente</button>
            </div>
          )}
        </>
      )}
    </div>
  )
}
