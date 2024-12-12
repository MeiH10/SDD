import React, { useState, useEffect } from "react";

const NoteForm = ({ onSubmit, selectedNote }) => {
  console.log(import.meta.env);
  const [title, setTitle] = useState("");
  const [file, setFile] = useState(null);

  useEffect(() => {
    if (selectedNote) {
      setTitle(selectedNote.title);
    } else {
      setTitle("");
    }
  }, [selectedNote]);

  const handleSubmit = (e) => {
    e.preventDefault();
    const noteData = { title, file };
    onSubmit(noteData);
    setTitle("");
    setFile(null);
  };

  return (
    <form onSubmit={handleSubmit}>
      <input
        type="text"
        value={title}
        onChange={(e) => setTitle(e.target.value)}
        placeholder="Title"
        required
      />
      <input type="file" onChange={(e) => setFile(e.target.files[0])} />
      <button type="submit">Save Note</button>
    </form>
  );
};

export default NoteForm;
