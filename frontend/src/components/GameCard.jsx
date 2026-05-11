import { useState } from 'react'
import Rating from './Rating'
import api from '../api/axios'

const statusLabels = {
  PLAYED: 'Jugado',
  PENDING: 'Pendiente',
  WISHED: 'Deseado',
  PURCHASED: 'Comprado',
  ABANDONED: 'Abandonado'
}

export default function GameCard({ game, onUpdate, onDelete }) {
  const [showReview, setShowReview] = useState(false)
  const [rating, setRating] = useState(game.review?.rating || game.personalRating || 0)
  const [opinion, setOpinion] = useState(game.review?.opinion || '')
  const [editing, setEditing] = useState(false)

  const handleSaveReview = async () => {
    try {
      if (game.review) {
        await api.put(`/games/${game.id}/reviews`, { rating, opinion })
      } else {
        await api.post(`/games/${game.id}/reviews`, { rating, opinion })
      }
      setEditing(false)
      setShowReview(false)
      onUpdate?.()
    } catch (err) {
      console.error('Error saving review', err)
    }
  }

  const handleDeleteReview = async () => {
    try {
      await api.delete(`/games/${game.id}/reviews`)
      setRating(0)
      setOpinion('')
      onUpdate?.()
    } catch (err) {
      console.error('Error deleting review', err)
    }
  }

  const handleDelete = async () => {
    if (confirm('Eliminar este juego?')) {
      try {
        await api.delete(`/games/${game.id}`)
        onDelete?.(game.id)
      } catch (err) {
        console.error('Error deleting game', err)
      }
    }
  }

  return (
    <div className="game-card">
      <div className="game-card-cover">
        {game.coverUrl ? (
          <img src={game.coverUrl} alt={game.name} />
        ) : (
          <div className="game-card-placeholder">{game.name[0]}</div>
        )}
        <span className={`game-card-status status-${game.status?.toLowerCase()}`}>
          {statusLabels[game.status]}
        </span>
      </div>
      <div className="game-card-body">
        <h3 className="game-card-title">{game.name}</h3>
        {game.genre && <span className="game-card-genre">{game.genre}</span>}
        {game.personalRating && (
          <div className="game-card-rating">
            <Rating value={game.personalRating} readonly />
          </div>
        )}
        <div className="game-card-actions">
          <button className="btn btn-sm btn-primary" onClick={() => setShowReview(!showReview)}>
            {game.review ? 'Ver reseña' : 'Reseñar'}
          </button>
          <button className="btn btn-sm btn-danger" onClick={handleDelete}>Eliminar</button>
        </div>
        {showReview && (
          <div className="game-card-review">
            {!editing && game.review ? (
              <div>
                <Rating value={game.review.rating} readonly />
                <p className="review-opinion">{game.review.opinion}</p>
                <div className="review-actions">
                  <button className="btn btn-sm" onClick={() => setEditing(true)}>Editar</button>
                  <button className="btn btn-sm btn-danger" onClick={handleDeleteReview}>Eliminar</button>
                </div>
              </div>
            ) : (
              <div className="review-form">
                <Rating value={rating} onChange={setRating} />
                <textarea
                  className="input"
                  placeholder="Tu opinión..."
                  value={opinion}
                  onChange={e => setOpinion(e.target.value)}
                  rows={3}
                />
                <button className="btn btn-sm btn-primary" onClick={handleSaveReview}>
                  {game.review ? 'Actualizar' : 'Guardar'}
                </button>
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  )
}
