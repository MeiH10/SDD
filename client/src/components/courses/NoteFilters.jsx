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
  onClearFilters,
  availableProfessors,
  availableSections,
}) => {
  const availableTags = Array.from(
    new Set(notes.flatMap((note) => note.tags || []))
  ).sort((a, b) => a.localeCompare(b));

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
                className={`px-3 py-1 rounded-full text-sm transition-colors ${
                  selectedTags.includes(tag)
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
