# ListaJuegos

Plataforma personal para gestionar tu biblioteca de videojuegos. Inspirada en Steam, Backloggd y Letterboxd.

## Tecnologías

### Backend
- **Java 17** + **Spring Boot 3.2**
- **Maven** - Gestión de dependencias
- **JPA / Hibernate** - ORM
- **PostgreSQL** - Base de datos
- **JWT** - Autenticación segura
- **API REST** - Arquitectura modular

### Frontend
- **React 18** + **Vite** - UI moderna
- **React Router** - Navegación SPA
- **Axios** - Cliente HTTP
- **Recharts** - Gráficos interactivos
- **CSS** - Diseño responsive oscuro

## Funcionalidades

- **Biblioteca personal**: CRUD completo con filtros y búsqueda
- **Categorías**: Jugados, Pendientes, Deseados, Comprados, Abandonados
- **Sistema de puntuación**: 1-5 estrellas con reseña opcional
- **Rankings personalizados**: Listas ordenables con posición
- **Wishlist con precios**: Seguimiento de precios Steam/Eneba
- **Dashboard**: Estadísticas, gráficos, juegos recientes y mejor valorados
- **Autenticación JWT**: Registro e inicio de sesión seguros

## Estructura

```
lista-juegos/
├── backend/                    # API REST Spring Boot
│   ├── src/main/java/com/listajuegos/
│   │   ├── config/            # Seguridad, CORS, AppConfig
│   │   ├── controller/        # Endpoints REST
│   │   ├── dto/               # Objetos de transferencia
│   │   ├── entity/            # Entidades JPA
│   │   ├── enums/             # Enumeraciones
│   │   ├── exception/         # Manejo de errores
│   │   ├── repository/        # Acceso a datos
│   │   ├── security/          # JWT + UserDetailsService
│   │   └── service/           # Lógica de negocio
│   └── src/main/resources/
│       ├── application.yml    # Configuración
│       └── schema.sql         # Script BD
├── frontend/                   # React + Vite
│   └── src/
│       ├── api/               # Cliente Axios
│       ├── components/        # Componentes reutilizables
│       ├── context/           # AuthContext
│       ├── pages/             # Páginas de la app
│       └── styles/            # CSS global
├── docker-compose.yml         # Orquestación Docker
└── .env.example               # Variables de entorno
```

## Cómo ejecutar

### Con Docker (recomendado)

```bash
# Clonar y entrar
git clone <repo-url>
cd lista-juegos

# Configurar variables de entorno (opcional)
cp .env.example .env

# Iniciar todo
docker-compose up -d

# Acceder
# Frontend: http://localhost
# Backend:  http://localhost:8080
```

### Manual

#### Backend

```bash
cd backend

# Configurar PostgreSQL y crear BD "lista_juegos"

# Ejecutar
./mvnw spring-boot:run
```

#### Frontend

```bash
cd frontend
npm install
npm run dev
```

## API Endpoints

### Auth
- `POST /api/auth/register` - Registro
- `POST /api/auth/login` - Inicio de sesión

### Games
- `GET /api/games` - Listar (filtros: status, search, paginación)
- `GET /api/games/{id}` - Detalle
- `POST /api/games` - Crear
- `PUT /api/games/{id}` - Actualizar
- `DELETE /api/games/{id}` - Eliminar
- `PATCH /api/games/{id}/status` - Cambiar estado

### Reviews
- `GET /api/games/{gameId}/reviews` - Obtener reseña
- `POST /api/games/{gameId}/reviews` - Crear reseña
- `PUT /api/games/{gameId}/reviews` - Actualizar reseña
- `DELETE /api/games/{gameId}/reviews` - Eliminar reseña

### Wishlist
- `GET /api/wishlist` - Listar deseados
- `POST /api/wishlist` - Añadir
- `DELETE /api/wishlist/{id}` - Eliminar

### Rankings
- `GET /api/rankings` - Listar rankings (filtro: listName)
- `POST /api/rankings` - Añadir juego a ranking
- `DELETE /api/rankings/{id}` - Eliminar
- `PATCH /api/rankings/{id}/reorder` - Reordenar

### Dashboard
- `GET /api/dashboard` - Estadísticas y resumen

## Licencia

MIT
