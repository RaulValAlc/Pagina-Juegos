import { NavLink } from 'react-router-dom'

const links = [
  { to: '/', label: 'Dashboard', icon: '\u25A1' },
  { to: '/library', label: 'Biblioteca', icon: '\u25B6' },
  { to: '/wishlist', label: 'Deseados', icon: '\u2605' },
  { to: '/rankings', label: 'Rankings', icon: '\u2666' },
]

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="sidebar-logo">
        <span className="logo-icon">&#9733;</span>
        <span className="sidebar-title">ListaJuegos</span>
      </div>
      <nav className="sidebar-nav">
        {links.map(link => (
          <NavLink
            key={link.to}
            to={link.to}
            end={link.to === '/'}
            className={({ isActive }) => `sidebar-link ${isActive ? 'active' : ''}`}
          >
            <span className="sidebar-link-icon">{link.icon}</span>
            <span>{link.label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
