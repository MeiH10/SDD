import { useState, useEffect } from "react";
import { useSearchParams } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import { useNote } from "../context/NoteContext";
import NoteFilters from "./courses/NoteFilters";
import NoteCard from "./courses/NoteCard";

const SearchResults = () => {
    const [searchParams] = useSearchParams();
    const query = searchParams.get("q");
    const { isLoggedIn } = useAuth();
    const { shouldRefreshNotes } = useNote();

    const [notes, setNotes] = useState([]);
    const [filteredNotes, setFilteredNotes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [sectionDataCache, setSectionDataCache] = useState({});

    const [selectedTags, setSelectedTags] = useState([]);
    const [selectedSection, setSelectedSection] = useState("");
    const [selectedProfessor, setSelectedProfessor] = useState("");
    const [selectedSchool, setSelectedSchool] = useState("");
    const [selectedMajor, setSelectedMajor] = useState("");

    const [sortBy, setSortBy] = useState("likes");
    const [sortOrder, setSortOrder] = useState("desc");

    const [availableProfessors, setAvailableProfessors] = useState([]);
    const [availableSections, setAvailableSections] = useState([]);

    useEffect(() => {
        const fetchNotes = async () => {
            if (!query) {
                setNotes([]);
                setLoading(false);
                return;
            }

            setLoading(true);
            try {
                const notesResponse = await fetch(
                    `/api/note?query=${encodeURIComponent(
                        query,
                    )}&return=object&sort=${sortBy}&order=${sortOrder}`,
                );
                const notesData = await notesResponse.json();

                if (!notesData.good) {
                    throw new Error("Failed to fetch notes");
                }

                const sectionPromises = notesData.data.map((note) =>
                    fetch(`/api/section/${note.section}`).then((res) => res.json()),
                );

                const sectionResponses = await Promise.all(sectionPromises);
                const newSectionCache = {};
                const sections = sectionResponses.map((res) => res.data);

                sections.forEach((section) => {
                    if (section && section.id) {
                        newSectionCache[section.id] = section;
                    }
                });

                const uniqueProfessors = [
                    ...new Set(
                        sections
                            .map((section) => section?.professors?.[0]?.split(",")[0])
                            .filter((prof) => prof && prof !== "TBA"),
                    ),
                ].sort((a, b) => a.localeCompare(b));

                const uniqueSections = [
                    ...new Set(
                        sections.map((section) => section?.number).filter(Boolean),
                    ),
                ].sort((a, b) => a.localeCompare(b));

                setSectionDataCache(newSectionCache);
                setAvailableProfessors(uniqueProfessors);
                setAvailableSections(uniqueSections);
                setNotes(notesData.data);
                setFilteredNotes(notesData.data);
            } catch (err) {
                setError(err.message);
                console.error("Error fetching notes:", err);
            } finally {
                setLoading(false);
            }
        };

        fetchNotes();
    }, [query, shouldRefreshNotes, sortBy, sortOrder]);

    useEffect(() => {
        let filtered = [...notes];

        if (selectedSchool) {
            filtered = filtered.filter((note) => note.school === selectedSchool);
        }

        if (selectedMajor) {
            filtered = filtered.filter((note) => note.major === selectedMajor);
        }

        if (selectedTags.length > 0) {
            filtered = filtered.filter((note) =>
                selectedTags.every((tag) => note.tags?.includes(tag)),
            );
        }

        if (selectedSection) {
            filtered = filtered.filter((note) => {
                const sectionData = sectionDataCache[note.section];
                return sectionData && sectionData.number === selectedSection;
            });
        }

        if (selectedProfessor) {
            filtered = filtered.filter((note) => {
                const sectionData = sectionDataCache[note.section];
                const mainProfessor = sectionData?.professors?.[0]?.split(",")?.[0];
                return mainProfessor === selectedProfessor;
            });
        }

        if (sortBy === "createdDate") {
            filtered.sort((a, b) => {
                const dateA = new Date(a.createdDate);
                const dateB = new Date(b.createdDate);
                return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
            });
        } else if (sortBy === "title") {
            filtered.sort((a, b) => {
                return sortOrder === "asc"
                    ? a.title.localeCompare(b.title)
                    : b.title.localeCompare(a.title);
            });
        } else if (sortBy === "likes") {
            filtered.sort((a, b) => {
                return sortOrder === "asc"
                    ? a.totalLikes - b.totalLikes
                    : b.totalLikes - a.totalLikes;
            });
        }

        setFilteredNotes(filtered);
    }, [
        notes,
        selectedSchool,
        selectedMajor,
        selectedSection,
        selectedProfessor,
        selectedTags,
        sortBy,
        sortOrder,
        sectionDataCache,
    ]);

    const handleTagToggle = (tag) => {
        setSelectedTags((prev) =>
            prev.includes(tag) ? prev.filter((t) => t !== tag) : [...prev, tag],
        );
    };

    const handleClearFilters = () => {
        setSelectedTags([]);
        setSelectedSection("");
        setSelectedProfessor("");
        setSelectedSchool("");
        setSelectedMajor("");
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
            <div className="max-w-[1920px] mx-auto px-4 sm:px-24">
                <div className="bg-teal-500 p-4 rounded-t-lg mb-6">
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
                        onTagToggle={handleTagToggle}
                        selectedSection={selectedSection}
                        onSectionChange={setSelectedSection}
                        selectedProfessor={selectedProfessor}
                        onProfessorChange={setSelectedProfessor}
                        selectedSchool={selectedSchool}
                        onSchoolChange={setSelectedSchool}
                        selectedMajor={selectedMajor}
                        onMajorChange={setSelectedMajor}
                        onClearFilters={handleClearFilters}
                        availableProfessors={availableProfessors}
                        availableSections={availableSections}
                        isSearchPage={true}
                    />

                    <div className="flex-1">
                        {filteredNotes.length === 0 ? (
                            <div className="text-center p-8 bg-gray-800 rounded">
                                <p className="text-gray-400">
                                    No notes found matching "{query}"
                                </p>
                                {isLoggedIn && (
                                    <p className="text-gray-400 mt-2">
                                        Be the first to upload notes!
                                    </p>
                                )}
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
