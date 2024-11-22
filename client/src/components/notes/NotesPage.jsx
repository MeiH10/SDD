import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const NotesPage = () => {
  const { majorCode, courseId, sectionNumber } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const courseData = state?.courseData;
  const { isLoggedIn } = useAuth();
  const [notes, setNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [section, setSection] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Get section IDs for this course and section number
        const sectionsResponse = await fetch(`/api/section?courseCode=${courseData.code}&number=${sectionNumber}`);
        const sectionsData = await sectionsResponse.json();
        
        if (!sectionsData.good || sectionsData.data.length === 0) {
          throw new Error('Section not found');
        }

        // Get the first section's details
        const sectionId = sectionsData.data[0];
        const sectionResponse = await fetch(`/api/section/${sectionId}`);
        const sectionData = await sectionResponse.json();
        
        if (!sectionData.good) {
          throw new Error('Failed to fetch section details');
        }

        setSection(sectionData.data);

        // Get all note IDs
        const noteIdsResponse = await fetch('/api/note');
        const noteIds = await noteIdsResponse.json();

        // Fetch details for each note
        const noteDetails = await Promise.all(
          noteIds.map(async id => {
            const response = await fetch(`/api/note/${id}`);
            const data = await response.json();
            return data.good ? data.data : null;
          })
        );

        // Filter out null responses and notes that don't belong to this section
        const filteredNotes = noteDetails
          .filter(note => note !== null && note.section === sectionId);

        setNotes(filteredNotes);

      } catch (err) {
        console.error('Error in fetch:', err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (courseData) {
      fetchData();
    }
  }, [courseData, sectionNumber]);

  if (!courseData) {
    return (
      <div className="text-center p-8">
        <p className="text-red-500">Course data not found. Please navigate from the course page.</p>
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

  return (
    <div className="px-4 sm:px-24 mx-auto">
      <div className="bg-teal-500 p-4 rounded-t-lg">
        <div className="flex items-center justify-between">
          <div className="flex items-center">
            <button 
              onClick={() => navigate(`/${majorCode}/${courseId}`)}
              className="mr-4 text-white hover:text-gray-200 transition-colors"
            >
              ← Back
            </button>
            <div>
              <h1 className="text-xl text-white font-bold">
                {courseData.code}: {courseData.name}
              </h1>
              <h2 className="text-white">
                Section {sectionNumber}
              </h2>
              {section?.professors?.length > 0 && (
                <p className="text-white text-sm mt-1">
                  Professor: {section.professors.join(", ")}
                </p>
              )}
            </div>
          </div>
          {isLoggedIn && (
            <button
              onClick={() => navigate('upload')}
              className="bg-white text-teal-500 px-4 py-2 rounded hover:bg-gray-100 transition-colors"
            >
              Upload Note
            </button>
          )}
        </div>
      </div>

      {error ? (
        <div className="text-red-500 text-center p-4">{error}</div>
      ) : notes.length === 0 ? (
        <div className="text-center p-8 bg-gray-800 mt-4 rounded">
          <p className="text-gray-400">No notes available for this section yet.</p>
          {isLoggedIn && (
            <p className="text-gray-400 mt-2">Be the first to upload notes!</p>
          )}
        </div>
      ) : (
        <div className="grid gap-4 mt-4">
          {notes.map((note) => (
            <div 
              key={note.id} 
              className="bg-gray-800 p-6 rounded-lg hover:bg-gray-700 transition-colors"
            >
              <h3 className="text-lg font-semibold mb-2">{note.title}</h3>
              {note.owner && (
                <p className="text-gray-400 text-sm">
                  Uploaded by {note.owner.username}
                </p>
              )}
              <div className="mt-4 flex gap-4">
                <button
                  onClick={() => window.location.href = `/api/note/${note.id}?download=true`}
                  className="text-teal-400 hover:text-teal-300 transition-colors"
                >
                  Download
                </button>
                {isLoggedIn && (
                  <button
                    onClick={() => window.location.href = `/api/note/${note.id}/like`}
                    className="text-gray-400 hover:text-teal-300 transition-colors"
                  >
                    Like
                  </button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default NotesPage;