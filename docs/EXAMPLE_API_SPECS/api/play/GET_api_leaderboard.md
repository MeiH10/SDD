
# `GET /leaderboard`

Retrieve the global leaderboard of the most active players.

## Request

| Name | Values | Description |
|-|-|-|
||| There is no request |
## Responses

### Success Response

Returns `200 OK` with an array of the most active players. Each object in the array contains the following fields:

```json
[
  {
    "username": "username",
    "numberOfPlays": "total number of plays by the user",
    "lastPlayed": "date of the last play"
  },
  {
  "error": "Internal server error."
}
]
