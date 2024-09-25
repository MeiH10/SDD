# `GET /api/user/:id/trophies`

Get the plays that `:id` have in their trophy room.

## Request

| Name | Values | Description |
|-|-|-|
| `:id` | `string` | The account in question. |

## Responses

Return `200` if account `:id` exists.

```json
{
  "likes": ["<play ids that they prize and are visible to the user>..."]
}
```

Return `404` if account `:id` does not exist.

```json
{
  "error": "user <:id> does not exist"
}
```

Return `500` if there an error during processing.

```json
{
  "error": "internal server error"
}
```