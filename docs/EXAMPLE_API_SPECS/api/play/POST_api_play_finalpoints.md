# `POST /api/play/finalpoints`

Get the total points that a user earned on their quiz.

## Requests

| Name | Values | Description |
|-|-|-|
| `quiz` | `string` | The quiz that the user played |

## Responses

Return `201` if the user is logged in, and can `edit` the quiz. Create a new play session with the quiz. This play session should be completed.

```json
{
  "playId": "<play id>",
  "message": "Play session created"
}
```

Return `400` if the request is badly formatted.

```json
{
  "error": "<offending issue>"
}
```

Return `401` if the user is not logged in.

```json
{
  "error": "you must log in"
}
```

Return `404` if quiz does not exist, or the user does not have `view` access.

```json
{
  "error": "user <id> does not exist"
}
```

Return `403` the user cannot `edit` the quiz.

```json
{
  "error": "you are unauthorized"
}
```

Return `500` if there an error during processing.

```json
{
  "error": "internal server error"
}
```