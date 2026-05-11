import { useState } from 'react'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Register() {
  const [form, setForm] = useState({ username: '', email: '', password: '', confirm: '' })
  const [showPassword, setShowPassword] = useState(false)
  const [showConfirm, setShowConfirm] = useState(false)
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const { register } = useAuth()
  const navigate = useNavigate()

  const handleChange = e => setForm({ ...form, [e.target.name]: e.target.value })

  const validateForm = () => {
    if (form.username.length < 3) {
      setError('El usuario debe tener al menos 3 caracteres')
      return false
    }
    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(form.email)) {
      setError('Ingresa un email válido')
      return false
    }
    if (form.password.length < 6) {
      setError('La contraseña debe tener al menos 6 caracteres')
      return false
    }
    if (form.password !== form.confirm) {
      setError('Las contraseñas no coinciden')
      return false
    }
    return true
  }

  const handleSubmit = async e => {
    e.preventDefault()
    setError('')
    if (!validateForm()) return
    setLoading(true)
    try {
      await register(form.username, form.email, form.password)
      navigate('/')
    } catch (err) {
      const data = err.response?.data
      if (data?.errors) {
        setError(Object.values(data.errors).join('. '))
      } else {
        setError(data?.message || 'Error al registrarse')
      }
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="auth-page">
      <div className="auth-card">
        <div className="auth-header">
          <span className="logo-icon">&#9733;</span>
          <h1>ListaJuegos</h1>
          <p>Crea tu cuenta</p>
        </div>
        <form onSubmit={handleSubmit} className="auth-form" autoComplete="off">
          {error && <div className="alert alert-error">{error}</div>}
          <div className="input-group">
            <label className="input-label" htmlFor="reg-user">Usuario</label>
            <input
              id="reg-user"
              className="input"
              name="username"
              placeholder="Elige un nombre de usuario"
              value={form.username}
              onChange={handleChange}
              autoFocus
              minLength={3}
              maxLength={50}
              required
            />
          </div>
          <div className="input-group">
            <label className="input-label" htmlFor="reg-email">Email</label>
            <input
              id="reg-email"
              className="input"
              name="email"
              type="email"
              placeholder="tu@email.com"
              value={form.email}
              onChange={handleChange}
              required
              autoComplete="email"
            />
          </div>
          <div className="input-group">
            <label className="input-label" htmlFor="reg-pass">Contraseña</label>
            <div className="password-wrapper">
              <input
                id="reg-pass"
                className="input password-input"
                name="password"
                type={showPassword ? 'text' : 'password'}
                placeholder="Mínimo 6 caracteres"
                value={form.password}
                onChange={handleChange}
                required
                minLength={6}
                autoComplete="new-password"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowPassword(!showPassword)}
                tabIndex={-1}
                aria-label={showPassword ? 'Ocultar contraseña' : 'Mostrar contraseña'}
              >
                {showPassword ? '🙈' : '👁️'}
              </button>
            </div>
            {form.password.length > 0 && form.password.length < 6 && (
              <span className="field-hint">Debe tener al menos 6 caracteres</span>
            )}
          </div>
          <div className="input-group">
            <label className="input-label" htmlFor="reg-confirm">Confirmar contraseña</label>
            <div className="password-wrapper">
              <input
                id="reg-confirm"
                className="input password-input"
                name="confirm"
                type={showConfirm ? 'text' : 'password'}
                placeholder="Repite la contraseña"
                value={form.confirm}
                onChange={handleChange}
                required
                autoComplete="new-password"
              />
              <button
                type="button"
                className="password-toggle"
                onClick={() => setShowConfirm(!showConfirm)}
                tabIndex={-1}
                aria-label={showConfirm ? 'Ocultar contraseña' : 'Mostrar contraseña'}
              >
                {showConfirm ? '🙈' : '👁️'}
              </button>
            </div>
            {form.confirm.length > 0 && form.password !== form.confirm && (
              <span className="field-hint error">Las contraseñas no coinciden</span>
            )}
          </div>
          <button type="submit" className="btn btn-primary btn-block" disabled={loading}>
            {loading ? <span className="btn-loader" /> : null}
            {loading ? 'Creando cuenta...' : 'Crear cuenta'}
          </button>
        </form>
        <p className="auth-footer">
          Ya tienes cuenta? <Link to="/login">Inicia sesión</Link>
        </p>
      </div>
    </div>
  )
}
