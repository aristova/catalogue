# Catalogue Service

## Getting Started

### Prerequisites

- **JDK 17** or higher
- **Maven 3.9+** (or use `./mvnw` wrapper once generated)

## API Reference

### Authentication

**Endpoints:**

- **`POST /api/auth/register`** — Register new user
- **`POST /api/auth/login`** — Obtain JWT access token

### Product API Endpoints

- **GET** `/api/products` — Get all products.
- **POST** `/api/products` — Create a new product.
- **GET** `/api/products/{id}` — Get a product by ID.
- **PUT** `/api/products/{id}` — Update an existing product.
- **DELETE** `/api/products/{id}` — Delete a product by ID.
- **POST** `/api/products/{id}/upload` — Upload an image for a product.

### Review API Endpoints
- **GET** `/api/products/{product_id}/reviews` — Get all reviews for a product.
- **POST** `/api/products/{product_id}/reviews` — Create a new review for a product.
- **GET** `/api/products/{product_id}/reviews/{id}` — Get a review by ID.
- **PUT** `/api/products/{product_id}/reviews/{id}` — Update an existing review.
- **DELETE** `/api/products/{product_id}/reviews/{id}` — Delete a review by ID.

**Authentication Header:**

```http
Authorization: Bearer <jwt-token>