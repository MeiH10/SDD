import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import NoteCard from "./courses/NoteCard";
import NoteFilters from "./courses/NoteFilters";

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get("q");
  
  const [notes, setNotes] = useState([]);
  const [filteredNotes, setFilteredNotes] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  
  const [selectedTags, setSelectedTags] = useState([]);
  const [selectedSection, setSelectedSection] = useState("");
  const [selectedProfessor, setSelectedProfessor] = useState("");
  const [sortBy, setSortBy] = useState("likes");
  const [sortOrder, setSortOrder] = useState("desc");
  const [availableProfessors, setAvailableProfessors] = useState([]);
  const [availableSections, setAvailableSections] = useState([]);
  const [sectionDataCache, setSectionDataCache] = useState({});

  useEffect(() => {
    const fetchNotes = async () => {
      if (!query) return;

      setLoading(true);
      try {
        const response = await fetch(
          `/api/note?query=${encodeURIComponent(query)}&return=object&sort=${sortBy}&order=${sortOrder}`
        );
        const data = await response.json();
        if (!data.good) {
          throw new Error(data.error || "Failed to fetch notes");
        }

        const sectionPromises = data.data.map((note) =>
          fetch(`/api/section/${note.section}`).then((res) => res.json())
        );

        const sectionResponses = await Promise.all(sectionPromises);
        const newSectionCache = {};
        const sections = sectionResponses.map((res) => res.data);

        // Build section cache
        sections.forEach((section) => {
          if (section && section.id) {
            newSectionCache[section.id] = section;
          }
        });

        const uniqueProfessors = [
          ...new Set(
            sections
              .map((section) => section?.professors?.[0]?.split(",")[0])
              .filter((prof) => prof && prof !== "TBA")
          ),
        ].sort((a, b) => a.localeCompare(b));

        const uniqueSections = [
          ...new Set(sections.map((section) => section?.number).filter(Boolean)),
        ].sort((a, b) => a.localeCompare(b));

        setSectionDataCache(newSectionCache);
        setAvailableProfessors(uniqueProfessors);
        setAvailableSections(uniqueSections);
        setNotes(data.data);
        setFilteredNotes(data.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchNotes();
  }, [query, sortBy, sortOrder]);

  // Filter notes based on selected filters
  useEffect(() => {
    let filtered = [...notes];

    // Filter by tags
    if (selectedTags.length > 0) {
      filtered = filtered.filter((note) =>
        selectedTags.every((tag) => note.tags?.includes(tag))
      );
    }

    // Filter by section
    if (selectedSection) {
      filtered = filtered.filter((note) => {
        const sectionData = sectionDataCache[note.section];
        return sectionData && sectionData.number === selectedSection;
      });
    }

    // Filter by professor
    if (selectedProfessor) {
      filtered = filtered.filter((note) => {
        const sectionData = sectionDataCache[note.section];
        const mainProfessor = sectionData?.professors?.[0]?.split(",")?.[0];
        return mainProfessor === selectedProfessor;
      });
    }

    setFilteredNotes(filtered);
  }, [notes, selectedTags, selectedSection, selectedProfessor, sectionDataCache]);

  const handleClearFilters = () => {
    setSelectedTags([]);
    setSelectedSection("");
    setSelectedProfessor("");
    setSortBy("likes");
    setSortOrder("desc");
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  if (error) {
    return <div className="text-red-500 text-center p-4">{error}</div>;
  }

  return (
    <div className="min-h-screen bg-gray-900">
      <div className="max-w-[1920px] mx-auto px-4 sm:px-24 py-6">
        <div className="bg-teal-500 p-4 rounded-lg mb-6">
          <h1 className="text-xl text-white font-bold">
            Search Results for "{query}"
          </h1>
        </div>

        <div className="flex gap-8">
          <NoteFilters
            notes={notes}
            sortBy={sortBy}
            onSortChange={setSortBy}
            sortOrder={sortOrder}
            onSortOrderChange={setSortOrder}
            selectedTags={selectedTags}
            onTagToggle={(tag) =>
              setSelectedTags((prev) =>
                prev.includes(tag)
                  ? prev.filter((t) => t !== tag)
                  : [...prev, tag]
              )
            }
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
                <p className="text-gray-400">
                  No notes found matching "{query}"
                </p>
              </div>
            ) : (
              <div className="space-y-4">
                {filteredNotes.map((note) => (
                  <NoteCard key={note.id} note={note} />
                ))}
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SearchResults;