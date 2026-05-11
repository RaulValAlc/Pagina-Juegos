export default function Rating({ value, onChange, readonly }) {
  return (
    <div className="rating">
      {[1, 2, 3, 4, 5].map(star => (
        <button
          key={star}
          type="button"
          className={`rating-star ${star <= value ? 'filled' : ''}`}
          onClick={() => !readonly && onChange?.(star)}
          disabled={readonly}
        >
          &#9733;
        </button>
      ))}
    </div>
  )
}
