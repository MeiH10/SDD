# API: Session

---

## 1. POST /session

**Description:** Log in a user and create a new session.

**Request Parameters:**

- `id`: **Required**. The user's ID (e.g., username or email).
- `password`: **Required**. The user's password.

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:
    ```json
    {
      "message": "Login successful",
      "token": "session-token-string"
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Missing or invalid request parameters.
  - `401 Unauthorized` - Incorrect ID or password.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 2. GET /session

**Description:** Check the current user's login status.

**Request Headers:**

- `Authorization`: **Required**. Bearer session-token-string.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```json
    {
      "id": "string",
      "status": "logged_in"
    }
    ```

- **Failure Codes:**
  - `401 Unauthorized` - Missing or invalid session token.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 3. DELETE /session

**Description:** Log out the current user and invalidate the session.

**Request Headers:**

- `Authorization`: **Required**. Bearer session-token-string.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```json
    {
      "message": "Logout successful"
    }
    ```

- **Failure Codes:**
  - `401 Unauthorized` - Missing or invalid session token.
  - `500 Internal Server Error` - Unexpected error on the server.

---