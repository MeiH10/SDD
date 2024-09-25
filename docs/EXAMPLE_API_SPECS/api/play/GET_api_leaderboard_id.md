# `GET /leaderboard/:id`

Retrieve the leaderboard for a specific quiz by its ID.

## Request

### Path Parameters

| Name | Type   | Description           |
|------|--------|-----------------------|
| id   | string | The unique identifier for the quiz. |

## Responses

### Success Response

Returns `200 OK` with an array of top scores for the specified quiz. Each object in the array includes:

```json
[
  {
    "username": "username",
    "highScore": "highest score achieved by the user",
    "lastPlayed": "date of the last play"
  },
  {
  "error": "Internal server error."
}

]
