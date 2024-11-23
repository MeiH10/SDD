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
  const [filteredNotes, setFilteredNotes] = useState([]);
  const [availableProfessors, setAvailableProfessors] = useState([]);
  const [availableSections, setAvailableSections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedTags, setSelectedTags] = useState([]);
  const [selectedSection, setSelectedSection] = useState('');
  const [selectedProfessor, setSelectedProfessor] = useState('');
  const [sortBy, setSortBy] = useState('likes');
  const [sortOrder, setSortOrder] = useState('desc');
  const [sectionDataCache, setSectionDataCache] = useState({});

  useEffect(() => {
    const fetchNotes = async () => {
      if (!courseData || !semesterData) {
        setError('Missing course or semester data');
        setLoading(false);
        return;
      }

      try {
        const notesResponse = await fetch(
          `/api/note?courseID=${courseId}&semesterID=${semesterData.id}&return=object&sort=${sortBy}&order=${sortOrder}`
        );
        const notesData = await notesResponse.json();

        if (!notesData.good) {
          throw new Error('Failed to fetch notes');
        }

        const sectionPromises = notesData.data.map(note => 
          fetch(`/api/section/${note.section}`).then(res => res.json())
        );
        
        const sectionResponses = await Promise.all(sectionPromises);
        const newSectionCache = {};
        const sections = sectionResponses.map(res => res.data);
        
        // Build section cache
        sections.forEach(section => {
          if (section && section.id) {
            newSectionCache[section.id] = section;
          }
        });
        
        const uniqueProfessors = [...new Set(
          sections
            .map(section => section?.professors?.[0]?.split(',')[0])
            .filter(prof => prof && prof !== 'TBA')
        )].sort((a, b) => a.localeCompare(b));

        const uniqueSections = [...new Set(
          sections
            .map(section => section?.number)
            .filter(Boolean)
        )].sort((a, b) => a.localeCompare(b));

        setSectionDataCache(newSectionCache);
        setAvailableProfessors(uniqueProfessors);
        setAvailableSections(uniqueSections);
        setNotes(notesData.data);
        setFilteredNotes(notesData.data);

      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchNotes();
  }, [courseId, semesterData, courseData, shouldRefreshNotes, sortBy, sortOrder]);

  useEffect(() => {
    let filtered = [...notes];

    if (selectedSection) {
      filtered = filtered.filter(note => {
        const sectionData = sectionDataCache[note.section];
        return sectionData && sectionData.number === selectedSection;
      });
    }

    if (selectedProfessor) {
      filtered = filtered.filter(note => {
        const sectionData = sectionDataCache[note.section];
        const mainProfessor = sectionData?.professors?.[0]?.split(',')?.[0];
        return mainProfessor === selectedProfessor;
      });
    }


    if (sortBy === 'createdDate') {
      filtered.sort((a, b) => {
        const dateA = new Date(a.createdDate);
        const dateB = new Date(b.createdDate);
        return sortOrder === 'asc' ? dateA - dateB : dateB - dateA;
      });
    } else if (sortBy === 'title') {
      filtered.sort((a, b) => {
        return sortOrder === 'asc' 
          ? a.title.localeCompare(b.title)
          : b.title.localeCompare(a.title);
      });
    }

    setFilteredNotes(filtered);
  }, [notes, selectedSection, selectedProfessor, selectedTags, sortBy, sortOrder, sectionDataCache]);

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
    setSortBy('likes');
    setSortOrder('desc');
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
            sortOrder={sortOrder}
            onSortOrderChange={setSortOrder}
            selectedTags={selectedTags}
            onTagToggle={handleTagToggle}
            selectedSection={selectedSection}
            onSectionChange={setSelectedSection}
            selectedProfessor={selectedProfessor}
            onProfessorChange={setSelectedProfessor}
            onClearFilters={handleClearFilters}
            availableProfessors={availableProfessors}
            availableSections={availableSections}
          />

          <div className="flex-1">
            {filteredNotes.length === 0 ? (
              <div className="text-center p-8 bg-gray-800 rounded">
                <p className="text-gray-400">No notes available for this semester yet.</p>
                {isLoggedIn && (
                  <p className="text-gray-400 mt-2">Be the first to upload notes!</p>
                )}
              </div>
            ) : (
              <div className="space-y-4">
                {filteredNotes.map((note) => (
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