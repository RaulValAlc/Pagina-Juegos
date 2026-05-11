import { Link } from 'react-router-dom'
import { useAuth } from '../../context/AuthContext'

export default function Navbar() {
  const { user, logout } = useAuth()

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/" className="navbar-logo">
          <span className="logo-icon">&#9733;</span>
          ListaJuegos
        </Link>
      </div>
      <div className="navbar-right">
        <div className="navbar-user-info">
          <div className="avatar-circle">
            {user?.username ? user.username[0].toUpperCase() : '?'}
          </div>
          <span className="navbar-user">{user?.username}</span>
        </div>
        <button onClick={logout} className="btn btn-sm btn-ghost">Salir</button>
      </div>
    </nav>
  )
}
