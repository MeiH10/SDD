# `DELETE /api/comment/:cid`

Delete a comment on the quiz.

## Request

| Name | Values | Description |
|-|-|-|
| `:cid` | `string` | The comment in question. |

## Responses

Return `200` if the user has `edit` access. No body. Delete the comment, and decrement the quizzes `totalComment` count.

Return `401` if the user is not logged in.

```json
{
  "error": "you must log in"
}
```

Return `403` if the user has `view` access, but not `edit` access.

```json
{
  "error": "you are unauthorized"
}
```

Return `404` if the comment does not exist.

```json
{
  "error": "comment <cid> does not exist"
}
```

Return `500` if there an error during processing.

```json
{
  "error": "internal server error"
}
```