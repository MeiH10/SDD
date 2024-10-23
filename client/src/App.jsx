import React, { useState, useEffect } from 'react';
import NoteForm from './NoteForm';
import NoteList from './NoteList';

const App = () => {
    const [notes, setNotes] = useState([]);
    const [selectedNote, setSelectedNote] = useState(null);

    useEffect(() => {
        fetchNotes();
    }, []);

    const fetchNotes = async () => {
        const response = await fetch('/api/note');
        const data = await response.json();
        setNotes(data);
    };

    const addOrUpdateNote = async (note) => {
        const method = note.id ? 'PUT' : 'POST';
        const response = await fetch(`/api/note${note.id ? `/${note.id}` : ''}`, {
            method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(note),
        });
        fetchNotes();
    };

    return (
        <div>
            <h1>Notes</h1>
            <NoteForm onSubmit={addOrUpdateNote} selectedNote={selectedNote} />
            <NoteList notes={notes} onSelect={setSelectedNote} />
        </div>
    );
};

export default App;
