import { useState, useEffect } from "react";
import { ChevronUp, ChevronDown, Eye, Download } from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import Modal from "../Modal";

const NoteCard = ({ note }) => {
  const [author, setAuthor] = useState(null);
  const [section, setSection] = useState(null);
  const [hasLiked, setHasLiked] = useState(false);
  const [localLikeCount, setLocalLikeCount] = useState(note.totalLikes || 0);
  const [isVoting, setIsVoting] = useState(false);
  const [isPreviewOpen, setIsPreviewOpen] = useState(false);
  const [previewUrl, setPreviewUrl] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);
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
        console.error("Error fetching note details:", error);
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
        console.error("Error checking like status:", error);
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
          method: "PUT",
        });
        if (response.ok) {
          setHasLiked(true);
          setLocalLikeCount((prev) => prev + 1);
        }
      } else if (!isUpvote && hasLiked) {
        // Remove like
        response = await fetch(`/api/note/${note.id}/like`, {
          method: "DELETE",
        });
        if (response.ok) {
          setHasLiked(false);
          setLocalLikeCount((prev) => prev - 1);
        }
      }
    } catch (error) {
      console.error("Error voting:", error);
    } finally {
      setIsVoting(false);
    }
  };

  const handlePreview = async () => {
    setIsPreviewOpen(true);
    setIsLoading(true);
    try {
      const response = await fetch(`/api/note/${note.id}/file`);
      if (!response.ok) throw new Error("Failed to load file");

      const blob = await response.blob();
      const url = URL.createObjectURL(blob);
      setPreviewUrl(url);
    } catch (error) {
      console.error("Error loading preview:", error);
    } finally {
      setIsLoading(false);
    }
  };

  const handleDownload = async () => {
    if (isDownloading) return;

    setIsDownloading(true);
    try {
      const response = await fetch(`/api/note/${note.id}/file`);
      if (!response.ok) throw new Error("Failed to download file");

      const blob = await response.blob();
      const url = URL.createObjectURL(blob);

      // temporary link and trigger download
      const link = document.createElement("a");
      link.href = url;
      link.download = `${note.title}.pdf`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      URL.revokeObjectURL(url);
    } catch (error) {
      console.error("Error downloading file:", error);
    } finally {
      setIsDownloading(false);
    }
  };

  const handleClosePreview = () => {
    setIsPreviewOpen(false);
    if (previewUrl) {
      URL.revokeObjectURL(previewUrl);
      setPreviewUrl(null);
    }
  };

  const mainProfessor = section?.professors?.[0]?.split(",")?.[0];

  return (
    <>
      <div className="bg-gray-800 rounded-lg p-4 hover:bg-gray-700/50 transition-colors border border-[#1a1f2e]">
        <div className="flex">
          {/* Vote Column */}
          <div className="flex flex-col items-center mr-4 w-8">
            <button
              onClick={() => handleVote(true)}
              disabled={!isLoggedIn || isVoting}
              className={`transition-colors ${
                !isLoggedIn
                  ? "text-gray-600 cursor-not-allowed"
                  : hasLiked
                  ? "text-teal-500 hover:text-teal-400"
                  : "text-gray-400 hover:text-teal-500"
              }`}
            >
              <ChevronUp className="w-6 h-6" />
            </button>
            <span className="text-white my-1 font-medium">
              {localLikeCount}
            </span>
            <button
              onClick={() => handleVote(false)}
              disabled={!isLoggedIn || isVoting || !hasLiked}
              className={`transition-colors ${
                !isLoggedIn
                  ? "text-gray-600 cursor-not-allowed"
                  : hasLiked
                  ? "text-gray-400 hover:text-red-500"
                  : "text-gray-600"
              }`}
            >
              <ChevronDown className="w-6 h-6" />
            </button>
          </div>

          <div className="flex-1">
            <div className="text-sm text-gray-400 mb-1">
              Uploaded by {author?.username || "anonymous"} on{" "}
              {new Date(note.createdDate).toLocaleDateString()}
            </div>

            <div className="mb-3">
              <h3 className="text-lg font-medium text-white">{note.title}</h3>
              {note.description && (
                <p className="text-gray-300 text-sm">{note.description}</p>
              )}
            </div>

            {/* Tags */}
            <div className="flex flex-wrap gap-2">
              {mainProfessor && mainProfessor !== "TBA" && (
                <span className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                  {mainProfessor}
                </span>
              )}

              {section && (
                <span className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300">
                  Sect. {section.number}
                </span>
              )}

              {note.tags?.map((tag) => (
                <span
                  key={tag}
                  className="px-2 py-1 bg-gray-700 rounded-full text-sm text-gray-300"
                >
                  {tag}
                </span>
              ))}
            </div>
          </div>

          {/* actions Column */}
          <div className="flex flex-col items-center justify-center ml-4 space-y-2">
            <button
              onClick={handlePreview}
              className="p-2 text-gray-400 hover:text-teal-500 transition-colors rounded-full hover:bg-gray-700/50"
              title="Preview"
            >
              <Eye className="w-5 h-5" />
            </button>
            <button
              onClick={handleDownload}
              disabled={isDownloading}
              className={`p-2 transition-colors rounded-full hover:bg-gray-700/50 ${
                isDownloading
                  ? "text-gray-600 cursor-not-allowed"
                  : "text-gray-400 hover:text-teal-500"
              }`}
              title="Download"
            >
              <Download className="w-5 h-5" />
            </button>
          </div>
        </div>
      </div>

      <Modal
        isOpen={isPreviewOpen}
        onClose={handleClosePreview}
        title={note.title}
        width="max-w-[67%]"
      >
        <div className="h-[85vh]">
          {isLoading ? (
            <div className="flex items-center justify-center h-full">
              <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent" />
            </div>
          ) : previewUrl ? (
            <object
              data={previewUrl}
              type="application/pdf"
              className="w-full h-full rounded-lg"
            >
              <embed src={previewUrl} className="w-full h-full rounded-lg" />
            </object>
          ) : (
            <div className="flex items-center justify-center h-full text-red-500">
              Failed to load preview
            </div>
          )}
        </div>
      </Modal>
    </>
  );
};

export default NoteCard;
