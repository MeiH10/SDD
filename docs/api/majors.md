# API: Majors

---

## 1. GET /major

**Description:** Retrieve a list of all available majors.

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    [
      {
        "major_id": 1,
        "major_name": "Computer Science"
      },
      {
        "major_id": 2,
        "major_name": "Mechanical Engineering"
      }
    ]
    ```

- **Failure Codes:**
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 2. GET /class?major={major}

**Description:** Retrieve a list of classes based on the provided major.

**Request Parameters:**

- `major`: **Required**. The name of the major (e.g., "Computer Science").

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    [
      {
        "class_id": 101,
        "class_name": "CS101 - Introduction to Computer Science"
      },
      {
        "class_id": 102,
        "class_name": "CS102 - Data Structures and Algorithms"
      }
    ]
    ```

- **Failure Codes:**
  - `400 Bad Request` - Missing or invalid major parameter.
  - `500 Internal Server Error` - Unexpected error on the server.

---

## 3. GET /section?major={major}&class={class}

**Description:** Retrieve a list of sections based on the provided major and class.

**Request Parameters:**

- `major`: **Required**. The name of the major (e.g., "Computer Science").
- `class`: **Required**. The class name or ID (e.g., "CS101").

**Example Response:**

- **Success:**
  - HTTP Status: `200 OK`
  - Response Body:

    ```json
    [
      {
        "section_id": 1,
        "section_name": "Section 1",
        "instructor": "Dr. John Doe"
      },
      {
        "section_id": 2,
        "section_name": "Section 2",
        "instructor": "Prof. Jane Smith"
      }
    ]
    ```

- **Failure Codes:**
  - `400 Bad Request` - Missing or invalid major or class parameter.
  - `500 Internal Server Error` - Unexpected error on the server.

---
