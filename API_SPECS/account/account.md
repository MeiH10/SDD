# API: Account Management

---

## 1. POST /account

**Description:** Create a new account for a user.

**Request Body:**

- `username`: **Required**. A unique username for the new account.
- `email`: **Required**. A valid email address.
- `password`: **Required**. A strong password for the account.
- `first_name`: **Optional**. User's first name.
- `last_name`: **Optional**. User's last name.

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:
    ```
    {
      "id": 1,
      "username": "john_doe",
      "message": "Account created successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid or missing parameters.
  - `409 Conflict` - Username or email already exists.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 2. GET /account/{id}

**Description:** Retrieve specific account information by the account ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the account.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "id": 1,
      "username": "john_doe",
      "email": "john@example.com",
      "first_name": "John",
      "last_name": "Doe",
      "created_at": "2023-01-01T00:00:00Z"
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid account ID.
  - `401 Unauthorized` - User is not authenticated.
  - `404 Not Found` - Account not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 3. DELETE /account/{id}

**Description:** Delete an account by its ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the account.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "message": "Account deleted successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid account ID.
  - `401 Unauthorized` - User is not authenticated.
  - `403 Forbidden` - User is not allowed to delete this account.
  - `404 Not Found` - Account not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---
