import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import { useNote } from '../../context/NoteContext';
import NoteBreadcrumb from './NoteBreadcrumb';
import NoteFilters from './NoteFilters';
import NoteCard from './NoteCard';

const NotesPage = () => {
  const { majorCode, courseId } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const courseData = state?.courseData;
  const semesterData = state?.semesterData;
  const { isLoggedIn } = useAuth();
  const { shouldRefreshNotes } = useNote();
  
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTags, setSelectedTags] = useState([]);
  const [selectedSection, setSelectedSection] = useState('');
  const [selectedProfessor, setSelectedProfessor] = useState('');
  const [sortBy, setSortBy] = useState('likes');

  useEffect(() => {
    const fetchNotes = async () => {
      if (!courseData || !semesterData) {
        setError('Missing course or semester data');
        setLoading(false);
        return;
      }

      try {
        const notesResponse = await fetch(
          `/api/note?courseID=${courseId}&semesterID=${semesterData.id}&return=object&sort=${sortBy}`
        );
        const notesData = await notesResponse.json();

        if (!notesData.good) {
          throw new Error('Failed to fetch notes');
        }

        setNotes(notesData.data);
      } catch (err) {
        console.error('Error fetching notes:', err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchNotes();
  }, [courseId, semesterData, courseData, shouldRefreshNotes, sortBy]);

  const handleTagToggle = (tag) => {
    setSelectedTags(prev => 
      prev.includes(tag) 
        ? prev.filter(t => t !== tag)
        : [...prev, tag]
    );
  };

  const handleClearFilters = () => {
    setSelectedTags([]);
    setSelectedSection('');
    setSelectedProfessor('');
  };

  const handleDownload = (noteId) => {
    window.location.href = `/api/note/${noteId}/file`;
  };

  if (!courseData || !semesterData) {
    return (
      <div className="text-center p-8">
        <p className="text-red-500">Course or semester data not found. Please navigate from the course page.</p>
        <button
          onClick={() => navigate(`/${majorCode}`)}
          className="mt-4 text-teal-400 hover:text-teal-300 transition-colors"
        >
          Return to courses
        </button>
      </div>
    );
  }

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="text-red-500 text-center p-4">
        {error}
        <button
          onClick={() => navigate(`/${majorCode}`)}
          className="block mx-auto mt-4 text-teal-400 hover:text-teal-300 transition-colors"
        >
          Return to courses
        </button>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-900">
      <div className="max-w-[1920px] mx-auto px-4 sm:px-24">
        <div className="py-6">
          <NoteBreadcrumb 
            majorCode={majorCode}
            courseData={courseData}
            semesterData={semesterData}
          />
        </div>

        <div className="flex gap-8">
          <NoteFilters
            sortBy={sortBy}
            onSortChange={setSortBy}
            selectedTags={selectedTags}
            onTagToggle={handleTagToggle}
            selectedSection={selectedSection}
            onSectionChange={setSelectedSection}
            selectedProfessor={selectedProfessor}
            onProfessorChange={setSelectedProfessor}
            onClearFilters={handleClearFilters}
          />

          <div className="flex-1">
            {notes.length === 0 ? (
              <div className="text-center p-8 bg-gray-800 rounded">
                <p className="text-gray-400">No notes available for this semester yet.</p>
                {isLoggedIn && (
                  <p className="text-gray-400 mt-2">Be the first to upload notes!</p>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                {notes.map((note) => (
                  <NoteCard 
                    key={note.id}
                    note={note}
                    onDownload={handleDownload}
                  />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default NotesPage;