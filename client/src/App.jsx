import React from 'react';
import Navbar from './components/Navbar';
import SchoolMajorSelector from './components/SchoolMajorSelector';
import NoteForm from './components/NoteForm';
import NoteList from './components/NoteList';
import { useEffect, useState } from 'react';
import ApiTest from './components/ApiTest'
import { AuthProvider } from './context/AuthContext';

const App = () => {
    const [notes, setNotes] = useState([]);
    const [selectedNote, setSelectedNote] = useState(null);

    // useEffect(() => {
    //     fetchNotes();
    // }, []);

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
        <AuthProvider>
            <div className="min-h-screen bg-gray-900 text-white">
                <Navbar />
                <SchoolMajorSelector />
                <ApiTest />
            </div>
        </AuthProvider>
    );
};

export default App;