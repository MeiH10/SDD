import { useState, useEffect } from 'react';
import { ChevronUp, ChevronDown } from 'lucide-react';

const NoteCard = ({ note, onDownload }) => {
  const [author, setAuthor] = useState(null);
  const [section, setSection] = useState(null);

  useEffect(() => {
    const fetchAuthorAndSection = async () => {
      try {
        const authorResponse = await fetch(`/api/account/${note.owner}`);
        const authorData = await authorResponse.json();
        if (authorData.good) {
          setAuthor(authorData.data);
        }

        const sectionResponse = await fetch(`/api/section/${note.section}`);
        const sectionData = await sectionResponse.json();
        if (sectionData.good) {
          setSection(sectionData.data);
        }
      } catch (error) {
        console.error('Error fetching note details:', error);
      }
    };

    fetchAuthorAndSection();
  }, [note.owner, note.section]);
  
  return (
    <div className="bg-gray-800 rounded-lg p-4 hover:bg-gray-700/50 transition-colors border border-[#1a1f2e]">
      <div className="flex">
        {/* Vote Column */}
        <div className="flex flex-col items-center mr-4 w-8">
          <button className="text-gray-400 hover:text-teal-500 transition-colors">
            <ChevronUp className="w-6 h-6" />
          </button>
          <span className="text-white my-1 font-medium">{note.totalLikes || 0}</span>
          <button className="text-gray-400 hover:text-red-500 transition-colors">
            <ChevronDown className="w-6 h-6" />
          </button>
        </div>

        <div className="flex-1">
          <div className="text-sm text-gray-400 mb-1">
            Uploaded by {author?.username || 'anonymous'} on {new Date(note.createdDate).toLocaleDateString()}
          </div>

          <div className="mb-3">
            <h3 className="text-lg font-medium text-white">
              {note.title}
            </h3>
            {note.description && (
              <p className="text-gray-300 text-sm">{note.description}</p>
            )}
          </div>

          {/* Tags */}
          <div className="flex flex-wrap gap-2">
            {section?.professors?.[0] && (
              <span className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                {section.professors[0]}
              </span>
            )}

            {section && (
              <span className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                Sect. {section.number}
              </span>
            )}

            {note.tags?.map(tag => (
              <span key={tag} className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                {tag}
              </span>
            ))}

            <button className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300 hover:bg-gray-600 transition-colors">
              Clear
            </button>
          </div>
        </div>

        <button
          onClick={() => onDownload(note.id)}
          className="hidden"
        >
          Download
        </button>
      </div>
    </div>
  );
};

export default NoteCard;