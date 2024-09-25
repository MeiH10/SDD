# API Specifications

## Account

POST /account

- Create an account.

GET /account/{id}

- Get account info.

DELETE /account/{id}

- Delete an account.

## Session

POST /session

- Log in.

GET /session

- Check login status.

DELETE /session

- Log out.

## Notes

GET /note?major={...}&class={...}&section={...}

- Search for notes.

POST /note

- Create a note.

GET /note/{id}

- Get note information.

POST /note/{id}/like

- Like a note.

DELETE /note/{id}/like

- Unlike a note.

PUT /note/{id}

- Replace a note.

DELETE /note/{id}

- Remove a note.

POST /note/{id}/report

- Report a note.

## QuACS Info

GET /major

- List all majors.

GET /class?major={...}

- Get classes by major.

GET /section?major={...}&class={...}

- Get sections by class and/or major.

## Comments

POST /comment

- Create a comment.

GET /comment?note={...}

- Search comments.

GET /comment/{id}

- Get comment details.

PUT /comment/{id}

- Replace comment.

DELETE /comment/{id}

- Remove a comment.

POST /comment/{id}/like

- Like a comment.

DELETE /comment/{id}/like

- Unlike a comment.

POST /comment/{id}/report

- Report a comment.

## Miscellaneous

GET /data/{id}

- Get note data.

DELETE /report/{id}

- Remove a report.

GET /report

- Get all the reports.
