import { useState, useEffect } from 'react';
import { ChevronUp, ChevronDown } from 'lucide-react';
import { useAuth } from '../../context/AuthContext';

const NoteCard = ({ note, onDownload }) => {
  const [author, setAuthor] = useState(null);
  const [section, setSection] = useState(null);
  const [hasLiked, setHasLiked] = useState(false);
  const [localLikeCount, setLocalLikeCount] = useState(note.totalLikes || 0);
  const [isVoting, setIsVoting] = useState(false);
  const { isLoggedIn } = useAuth();

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

    const checkIfLiked = async () => {
      if (!isLoggedIn) return;
      
      try {
        const response = await fetch(`/api/note/${note.id}/like`);
        const data = await response.json();
        if (data.good) {
          setHasLiked(data.data);
        }
      } catch (error) {
        console.error('Error checking like status:', error);
      }
    };

    fetchAuthorAndSection();
    checkIfLiked();
  }, [note.owner, note.section, note.id, isLoggedIn]);

  const handleVote = async (isUpvote) => {
    if (!isLoggedIn) return;
    if (isVoting) return;

    setIsVoting(true);
    try {
      let response;
      if (isUpvote && !hasLiked) {
        // Add like
        response = await fetch(`/api/note/${note.id}/like`, {
          method: 'PUT'
        });
        if (response.ok) {
          setHasLiked(true);
          setLocalLikeCount(prev => prev + 1);
        }
      } else if (!isUpvote && hasLiked) {
        // Remove like
        response = await fetch(`/api/note/${note.id}/like`, {
          method: 'DELETE'
        });
        if (response.ok) {
          setHasLiked(false);
          setLocalLikeCount(prev => prev - 1);
        }
      }
    } catch (error) {
      console.error('Error voting:', error);
    } finally {
      setIsVoting(false);
    }
  };
  
  const mainProfessor = section?.professors?.[0]?.split(',')?.[0];

  return (
    <div className="bg-gray-800 rounded-lg p-4 hover:bg-gray-700/50 transition-colors border border-[#1a1f2e]">
      <div className="flex">
        {/* Vote Column */}
        <div className="flex flex-col items-center mr-4 w-8">
          <button 
            onClick={() => handleVote(true)}
            disabled={!isLoggedIn || isVoting}
            className={`transition-colors ${
              !isLoggedIn 
                ? 'text-gray-600 cursor-not-allowed' 
                : hasLiked
                  ? 'text-teal-500 hover:text-teal-400'
                  : 'text-gray-400 hover:text-teal-500'
            }`}
          >
            <ChevronUp className="w-6 h-6" />
          </button>
          <span className="text-white my-1 font-medium">{localLikeCount}</span>
          <button
            onClick={() => handleVote(false)}
            disabled={!isLoggedIn || isVoting || !hasLiked}
            className={`transition-colors ${
              !isLoggedIn 
                ? 'text-gray-600 cursor-not-allowed' 
                : hasLiked
                  ? 'text-gray-400 hover:text-red-500'
                  : 'text-gray-600'
            }`}
          >
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
            {mainProfessor && mainProfessor !== 'TBA' && (
              <span className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                {mainProfessor}
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