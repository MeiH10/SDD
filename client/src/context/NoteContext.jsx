import { createContext, useContext, useState } from "react";

const NoteContext = createContext(null);

export const NoteProvider = ({ children }) => {
  const [shouldRefreshNotes, setShouldRefreshNotes] = useState(false);

  const triggerNoteRefresh = () => {
    setShouldRefreshNotes((prev) => !prev);
  };

  return (
    <NoteContext.Provider value={{ shouldRefreshNotes, triggerNoteRefresh }}>
      {children}
    </NoteContext.Provider>
  );
};

export const useNote = () => useContext(NoteContext);
