import React from 'react';

const NoteList = ({ notes, onSelect }) => {
    return (
        <ul>
            {notes.map(note => (
                <li key={note.id} onClick={() => onSelect(note)}>
                    {note.title}
                </li>
            ))}
        </ul>
    );
};

export default NoteList;
