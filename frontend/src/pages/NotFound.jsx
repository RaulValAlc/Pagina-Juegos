import { Link } from 'react-router-dom'

export default function NotFound() {
  return (
    <div className="auth-page">
      <div className="auth-card" style={{ textAlign: 'center' }}>
        <div className="auth-header">
          <span style={{ fontSize: 64, display: 'block', marginBottom: 8 }}>&#9733;</span>
          <h1>404</h1>
          <p>Página no encontrada</p>
        </div>
        <p className="text-muted" style={{ marginBottom: 24 }}>
          La página que buscas no existe o ha sido movida.
        </p>
        <Link to="/" className="btn btn-primary" style={{ display: 'inline-flex' }}>
          Volver al inicio
        </Link>
      </div>
    </div>
  )
}
