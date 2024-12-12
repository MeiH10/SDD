# :pushpin: PuckNotes

Welcome to PuckNotes, the all-in-one note sharing app for all RPI students!
Collaborate on crib-sheets, share lecture notes, and more. When it comes to
studying for exams, we say:

> Why not share the notes?

PuckNotes is currently live. View it
[`here`](https://pucknotes.up.railway.app/), on the website: Railway. This
represents our production environment and is the recommended way to access
PuckNotes. The live version is continuously maintained and updated with the
latest stable features.

## :gear: Installation Guide

> [!WARNING] Docker Installation Currently, using Docker with PuckNotes is
> deprecated. Use it at your own risk!

To begin local development, here is all you need to do:

1. Clone this repository. (`git clone https://github.com/MeiH10/SDD.git`)
2. **COPY** `example.env`, and name that copy `.env`.
3. Follow the instructions inside the `.env` file on how to configure your
   PuckNotes instance.

### :alembic: Run Back-end Server

To start up the back-end server for local development, enter the `server`
folder, and start up the maven project:

```sh
cd server
./mvnw spring-boot:run
```

Or, if you have [`task`](taskfile.dev), just run `task backend` to boot up the
task server.

### :iphone: Run Front-end Server

To start up the front-end server for local development, enter the `client`
folder, and start up the maven project:

```sh
cd client
npm run dev
```

Again, you can just run `task frontend` if you have [`task`](taskfile.dev).

## :memo: Documentation

Want more information about this site? Look no further:

- For information as to the type of information we collect from QuACS, go
  [here](docs/quacs_data.md).
- For our database schema, go [here](docs/database/main.md).
