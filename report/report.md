# API: Data and Reports

---

## 1. GET /report/{id}

**Description:** Retrieve all reports associated with the authenticated user.

**Path Parameters:**

- `id`: **Required**. The ID of the user (path parameter).

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```json
    [
      {
        "id": "report1",
        "title": "First Report",
        "description": "Details of the first report.",
        "created_at": "2024-09-30T08:00:00Z"
      },
      {
        "id": "report2",
        "title": "Second Report",
        "description": "Details of the second report.",
        "created_at": "2024-09-25T08:00:00Z"
      }
    ]
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid Request
  - `401 Unauthorized` - User not authenticated
  - `500 Internal Server Error` - Unexpected server error

---
## 2. POST /report/{id}

**Description:** Create a new report for the authenticated user.

**Path Parameters:**

- `id`: **Required**. The ID of the user (path parameter).

**Request Body:**

- `title`: **Required**. The title of the report.
- `description`: **Optional**. A description of the report.

**Example Request:**

{
  "title": "Missing Data",
  "description": "Some data is missing from the report."
}

**Example Response:**

- **Success:**
  - HTTP Status: `201 Created`
  - Response Body:
  
  {
    "id": "report3",
    "message": "Report created successfully."
  }

**Failure Codes:**

- `400 Bad Request` - Invalid or missing request parameters.
- `401 Unauthorized` - User not authenticated.
- `500 Internal Server Error` - Unexpected server error.

---

## 3. DELETE /report/{id}

**Description:** Remove a specific report by its ID.

**Path Parameters:**

- `id`: **Required**. The ID of the report to be deleted (path parameter).

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:
    ```json
    {
      "message": "Report successfully deleted."
    }
    ```

- **Failure Codes:**
  - `400 Bad Request` - Invalid Request (e.g., invalid report ID format)
  - `401 Unauthorized` - User not authenticated
  - `404 Not Found` - Report does not exist
  - `500 Internal Server Error` - Unexpected server error

---