# API: Comments

---

## 1. POST /comment

**Description:** Create a new comment.

**Request Body:**

- `note_id`: **Required**. The ID of the note this comment is associated with.
- `user_id`: **Required**. The ID of the user creating the comment.
- `content`: **Required**. The content of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:
    ```
    {
      "id": 1,
      "message": "Comment created successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid or missing parameters.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 2. GET /comment?note={note_id}

**Description:** Retrieve a list of comments based on a note ID.

**Request Parameters:**

- `note_id`: **Required**. The ID of the note to retrieve comments for.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    [
      {
        "id": 1,
        "user_id": 123,
        "content": "This is a comment.",
        "created_at": "2023-01-01T00:00:00Z"
      },
      {
        "id": 2,
        "user_id": 456,
        "content": "This is another comment.",
        "created_at": "2023-01-02T00:00:00Z"
      }
    ]
    ```

- **Failure Codes:**
  - `400 Bad Request` - Missing or invalid note ID.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 3. GET /comment/{id}

**Description:** Retrieve specific comment details by the comment ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "id": 1,
      "user_id": 123,
      "content": "This is a comment.",
      "created_at": "2023-01-01T00:00:00Z"
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID.
  - `404 Not Found` - Comment not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 4. PUT /comment/{id}

**Description:** Replace an existing comment with a new one.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Request Body:**

- `content`: **Required**. The new content of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "message": "Comment updated successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID or missing content.
  - `401 Unauthorized` - User is not authenticated.
  - `404 Not Found` - Comment not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 5. DELETE /comment/{id}

**Description:** Remove a comment by its ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "message": "Comment deleted successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID.
  - `401 Unauthorized` - User is not authenticated.
  - `403 Forbidden` - User is not allowed to delete this comment.
  - `404 Not Found` - Comment not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 6. POST /comment/{id}/like

**Description:** Like a comment.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:
    ```
    {
      "message": "Comment liked successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 7. DELETE /comment/{id}/like

**Description:** Unlike a comment.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "message": "Comment unliked successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 8. POST /comment/{id}/report

**Description:** Report a comment as inappropriate or offensive.

**Path Parameters:**

- `id`: **Required**. The unique ID of the comment.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```
    {
      "message": "Comment reported successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid comment ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---
