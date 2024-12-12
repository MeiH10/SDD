import { useState, useEffect } from "react";

const NoteFilters = ({
    notes,
    sortBy,
    onSortChange,
    sortOrder,
    onSortOrderChange,
    selectedTags,
    onTagToggle,
    selectedSection,
    onSectionChange,
    selectedProfessor,
    onProfessorChange,
    selectedSchool,
    onSchoolChange,
    selectedMajor,
    onMajorChange,
    onClearFilters,
    availableProfessors,
    availableSections,
    isSearchPage = false,
}) => {
    const [schools, setSchools] = useState([]);
    const [majors, setMajors] = useState([]);
    const [filteredMajors, setFilteredMajors] = useState([]);

    const availableTags = Array.from(
        new Set(notes.flatMap((note) => note.tags || [])),
    ).sort((a, b) => a.localeCompare(b));

    useEffect(() => {
        const fetchSchoolsAndMajors = async () => {
            try {
                const [schoolsResponse, majorsResponse] = await Promise.all([
                    fetch("/api/school?return=object"),
                    fetch("/api/major?return=object"),
                ]);

                const schoolsData = await schoolsResponse.json();
                const majorsData = await majorsResponse.json();

                if (schoolsData.good && majorsData.good) {
                    setSchools(schoolsData.data);
                    setMajors(majorsData.data);
                    setFilteredMajors(majorsData.data);
                }
            } catch (error) {
                console.error("Error fetching schools and majors:", error);
            }
        };

        if (isSearchPage) {
            fetchSchoolsAndMajors();
        }
    }, [isSearchPage]);

    useEffect(() => {
        if (selectedSchool) {
            const filtered = majors.filter(
                (major) => major.school === selectedSchool,
            );
            setFilteredMajors(filtered);
        } else {
            setFilteredMajors(majors);
        }
    }, [selectedSchool, majors]);

    return (
        <div className="w-64">
            <div className="mb-6 space-y-2">
                <div className="flex flex-col space-y-2">
                    <select
                        value={sortBy}
                        onChange={(e) => onSortChange(e.target.value)}
                        className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                    >
                        <option value="likes">Sort by likes</option>
                        <option value="createdDate">Sort by date</option>
                        <option value="title">Sort by title</option>
                    </select>

                    <select
                        value={sortOrder}
                        onChange={(e) => onSortOrderChange(e.target.value)}
                        className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                    >
                        <option value="desc">Descending</option>
                        <option value="asc">Ascending</option>
                    </select>
                </div>
            </div>

            {isSearchPage && (
                <>
                    <div className="mb-6">
                        <h3 className="text-white mb-2">School:</h3>
                        <select
                            value={selectedSchool}
                            onChange={(e) => onSchoolChange(e.target.value)}
                            className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                        >
                            <option value="">All Schools</option>
                            {schools.map((school) => (
                                <option key={school.id} value={school.id}>
                                    {school.name}
                                </option>
                            ))}
                        </select>
                    </div>

                    <div className="mb-6">
                        <h3 className="text-white mb-2">Major:</h3>
                        <select
                            value={selectedMajor}
                            onChange={(e) => onMajorChange(e.target.value)}
                            className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                        >
                            <option value="">All Majors</option>
                            {filteredMajors.map((major) => (
                                <option key={major.id} value={major.id}>
                                    {major.code}: {major.name}
                                </option>
                            ))}
                        </select>
                    </div>
                </>
            )}

            <div className="mb-6">
                <div className="flex justify-between items-center mb-2">
                    <h3 className="text-white">Filter by tags:</h3>
                    {selectedTags.length > 0 && (
                        <button
                            onClick={onClearFilters}
                            className="text-sm text-gray-400 hover:text-white transition-colors"
                        >
                            Clear filters
                        </button>
                    )}
                </div>
                {availableTags.length > 0 ? (
                    <div className="flex flex-wrap gap-2">
                        {availableTags.map((tag) => (
                            <button
                                key={tag}
                                onClick={() => onTagToggle(tag)}
                                className={`px-3 py-1 rounded-full text-sm transition-colors ${selectedTags.includes(tag)
                                        ? "bg-teal-500 text-white hover:bg-teal-600"
                                        : "bg-gray-800 text-gray-300 hover:bg-gray-700"
                                    }`}
                            >
                                {tag}
                            </button>
                        ))}
                    </div>
                ) : (
                    <p className="text-gray-400 text-sm">No tags available</p>
                )}
            </div>

            <div className="mb-6">
                <h3 className="text-white mb-2">Sections:</h3>
                <select
                    value={selectedSection}
                    onChange={(e) => onSectionChange(e.target.value)}
                    className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                >
                    <option value="">All Sections</option>
                    {availableSections.map((section) => (
                        <option key={section} value={section}>
                            Section {section}
                        </option>
                    ))}
                </select>
            </div>

            <div className="mb-6">
                <h3 className="text-white mb-2">Prof:</h3>
                <select
                    value={selectedProfessor}
                    onChange={(e) => onProfessorChange(e.target.value)}
                    className="w-full px-3 py-2 bg-gray-800 text-white rounded border border-gray-700 focus:border-teal-500"
                >
                    <option value="">All Professors</option>
                    {availableProfessors.map((professor) => (
                        <option key={professor} value={professor}>
                            {professor}
                        </option>
                    ))}
                </select>
            </div>
        </div>
    );
};

export default NoteFilters;
