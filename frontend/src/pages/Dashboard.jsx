import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { PieChart, Pie, Cell, ResponsiveContainer, Tooltip } from 'recharts'
import api from '../api/axios'
import GameCard from '../components/GameCard'

const COLORS = ['#4ade80', '#facc15', '#a78bfa', '#f472b6', '#fb923c']

export default function Dashboard() {
  const [data, setData] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => { loadDashboard() }, [])

  const loadDashboard = async () => {
    try {
      const { data: res } = await api.get('/dashboard')
      setData(res)
    } catch (err) {
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  if (loading) {
    return (
      <div className="dashboard">
        <h1 className="page-title">Dashboard</h1>
        <div className="stats-grid">
          {[...Array(6)].map((_, i) => (
            <div key={i} className="stat-card skeleton" />
          ))}
        </div>
        <div className="dashboard-grid">
          <div className="dashboard-section skeleton" style={{ height: 200 }} />
          <div className="dashboard-section skeleton" style={{ height: 200 }} />
          <div className="dashboard-section skeleton" style={{ height: 200 }} />
        </div>
      </div>
    )
  }
  if (!data) return (
    <div className="dashboard">
      <h1 className="page-title">Dashboard</h1>
      <div className="stat-card" style={{ textAlign: 'center', padding: 40 }}>
        <p className="text-muted">Error al cargar el dashboard</p>
        <button className="btn btn-primary" style={{ marginTop: 12 }} onClick={loadDashboard}>
          Reintentar
        </button>
      </div>
    </div>
  )

  const pieData = [
    { name: 'Jugados', value: data.playedGames },
    { name: 'Pendientes', value: data.pendingGames },
    { name: 'Deseados', value: data.wishedGames },
    { name: 'Comprados', value: data.purchasedGames },
    { name: 'Abandonados', value: data.abandonedGames },
  ].filter(d => d.value > 0)

  return (
    <div className="dashboard">
      <h1 className="page-title">Dashboard</h1>

      <div className="stats-grid">
        <div className="stat-card"><span className="stat-value">{data.totalGames}</span><span className="stat-label">Total juegos</span></div>
        <div className="stat-card"><span className="stat-value">{data.playedGames}</span><span className="stat-label">Jugados</span></div>
        <div className="stat-card"><span className="stat-value">{data.pendingGames}</span><span className="stat-label">Pendientes</span></div>
        <div className="stat-card"><span className="stat-value">{data.wishedGames}</span><span className="stat-label">Deseados</span></div>
        <div className="stat-card"><span className="stat-value">{data.purchasedGames}</span><span className="stat-label">Comprados</span></div>
        <div className="stat-card"><span className="stat-value">{data.abandonedGames}</span><span className="stat-label">Abandonados</span></div>
      </div>

      <div className="dashboard-grid">
        <div className="dashboard-section">
          <h2>Recientes</h2>
          <div className="mini-list">
            {data.recentGames?.map(g => (
              <Link key={g.id} to="/library" className="mini-item">
                <div className="mini-cover">
                  {g.coverUrl ? <img src={g.coverUrl} alt={g.name} /> : <span>{g.name[0]}</span>}
                </div>
                <span className="mini-name">{g.name}</span>
              </Link>
            ))}
          </div>
        </div>

        <div className="dashboard-section">
          <h2>Mejor valorados</h2>
          <div className="mini-list">
            {data.topRated?.map(g => (
              <Link key={g.id} to="/library" className="mini-item">
                <div className="mini-cover">
                  {g.coverUrl ? <img src={g.coverUrl} alt={g.name} /> : <span>{g.name[0]}</span>}
                </div>
                <div>
                  <span className="mini-name">{g.name}</span>
                  <span className="mini-rating">{'★'.repeat(g.personalRating)}</span>
                </div>
              </Link>
            ))}
          </div>
        </div>

        <div className="dashboard-section">
          <h2>Estadísticas</h2>
          {pieData.length > 0 ? (
            <ResponsiveContainer width="100%" height={200}>
              <PieChart>
                <Pie data={pieData} dataKey="value" cx="50%" cy="50%" outerRadius={80} label>
                  {pieData.map((_, i) => <Cell key={i} fill={COLORS[i % COLORS.length]} />)}
                </Pie>
                <Tooltip />
              </PieChart>
            </ResponsiveContainer>
          ) : (
            <p className="text-muted">Añade juegos para ver estadísticas</p>
          )}
        </div>
      </div>
    </div>
  )
}
