# API: Notes

---

## 1. GET /note?major={major}&class={class}

**Description:** Search for notes based on a user's major and class.

**Request Parameters:**

- `major`: **Required**. The user's major (e.g., "Computer Science").
- `class`: **Required**. The user's class or course (e.g., "CS101").

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    [
      {
        "id": 1,
        "title": "Note Title",
        "description": "Note Description",
        "created_at": "2023-01-01T00:00:00Z"
      }
    ]
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid or missing query parameters.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 2. POST /note

**Description:** Create a new note by uploading a file.

**Request Parameters:**

- `file`: **Required**. The note file (e.g., a PDF, image, or text document).

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:

    ```json
    {
      "id": 1,
      "message": "Note created successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid file or missing parameters.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 3. GET /note/{id}

**Description:** Retrieve specific note information by its ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    {
      "id": 1,
      "title": "Note Title",
      "description": "Note Description",
      "file_url": "http://example.com/note.pdf",
      "created_at": "2023-01-01T00:00:00Z"
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID.
  - `401 Unauthorized` - User is not authenticated.
  - `404 Not Found` - Note not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 4. POST /note/{id}/like

**Description:** Like a note.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:

    ```json
    {
      "message": "Note liked successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 5. DELETE /note/{id}/like

**Description:** Unlike a note.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    {
      "message": "Note unliked successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 6. PUT /note/{id}

**Description:** Replace or update a note.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Request Parameters:**

- `file`: **Required**. The updated note file.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    {
      "message": "Note updated successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID or file.
  - `401 Unauthorized` - User is not authenticated.
  - `404 Not Found` - Note not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 7. DELETE /note/{id}

**Description:** Remove a note by its ID.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    {
      "message": "Note deleted successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID.
  - `401 Unauthorized` - User is not authenticated.
  - `404 Not Found` - Note not found.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 8. POST /note/{id}/report

**Description:** Report a note as inappropriate or offensive.

**Path Parameters:**

- `id`: **Required**. The unique ID of the note.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    {
      "message": "Note reported successfully."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid note ID.
  - `401 Unauthorized` - User is not authenticated.
  - `500 Internal Server Error` - Unexpected error on the server.
  
---
