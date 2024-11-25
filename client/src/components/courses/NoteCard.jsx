import { useState, useEffect } from "react";
import {
  ChevronUp,
  ChevronDown,
  Eye,
  Download,
  Link as LinkIcon,
  MessageSquare,
} from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import Modal from "../Modal";
import CommentModal from "./CommentModal";

import { AlertTriangle } from "lucide-react";
import ReportModal from "../reports/ReportModal";
import { Trash2 } from "lucide-react";
import { useNote } from "../../context/NoteContext";
import NoteEditModal from "./NoteEditModal";
import { PencilIcon } from "lucide-react";

const NoteCard = ({ note }) => {
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const { userId } = useAuth();
  const { triggerNoteRefresh } = useNote();
  const [author, setAuthor] = useState(null);
  const [section, setSection] = useState(null);
  const [hasLiked, setHasLiked] = useState(false);
  const [localLikeCount, setLocalLikeCount] = useState(note.totalLikes || 0);
  const [isVoting, setIsVoting] = useState(false);
  const [isPreviewOpen, setIsPreviewOpen] = useState(false);
  const [previewUrl, setPreviewUrl] = useState(null);
  const [isLoading, setIsLoading] = useState(false);
  const [isDownloading, setIsDownloading] = useState(false);
  const [isCommentModalOpen, setIsCommentModalOpen] = useState(false);
  const [commentCount, setCommentCount] = useState(0);
  const { isLoggedIn } = useAuth();
  const [isReportModalOpen, setIsReportModalOpen] = useState(false);

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

  useEffect(() => {
    const fetchCommentCount = async () => {
      try {
        const response = await fetch(
          `/api/comment?noteID=${note.id}&return=count`
        );
        const data = await response.json();
        if (data.good) {
          setCommentCount(data.data);
        }
      } catch (error) {
        console.error("Error fetching comment count:", error);
      }
    };

    fetchCommentCount();
  }, [note.id]);

  const handleDelete = async () => {
    if (!window.confirm("Are you sure you want to delete this note?")) return;

    try {
      const response = await fetch(`/api/note/${note.id}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Failed to delete note");

      // refresh of notes list
      triggerNoteRefresh();
    } catch (error) {
      console.error("Error deleting note:", error);
    }
  };

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

  const handleLinkClick = (url) => {
    window.open(url, "_blank", "noopener,noreferrer");
  };

  const mainProfessor = section?.professors?.[0]?.split(",")?.[0];

  // Function to check if a string is a valid URL
  const isValidUrl = (string) => {
    try {
      new URL(string);
      return true;
    } catch (_) {
      return false;
    }
  };

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

            {/* Links section */}
            {note.link && isValidUrl(note.link) && (
              <div className="mb-3">
                <button
                  onClick={() => handleLinkClick(note.link)}
                  className="inline-flex items-center px-3 py-1 bg-gray-700 rounded-lg text-teal-400 hover:text-teal-300 hover:bg-gray-600 transition-colors text-sm"
                >
                  <LinkIcon className="w-4 h-4 mr-2" />
                  Related Resource
                </button>
              </div>
            )}

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
            <button
              onClick={() => setIsCommentModalOpen(true)}
              className="p-2 text-gray-400 hover:text-teal-500 transition-colors rounded-full hover:bg-gray-700/50"
              title="Comments"
            >
              <div className="relative">
                <MessageSquare className="w-5 h-5" />
                {commentCount > 0 && (
                  <span className="absolute -top-2 -right-2 bg-teal-500 text-white text-xs rounded-full w-4 h-4 flex items-center justify-center">
                    {commentCount}
                  </span>
                )}
              </div>
            </button>

            <button
              onClick={() => setIsReportModalOpen(true)}
              disabled={!isLoggedIn}
              className={`p-2 transition-colors rounded-full hover:bg-gray-700/50 ${
                isLoggedIn
                  ? "text-gray-400 hover:text-red-500"
                  : "text-gray-600 cursor-not-allowed"
              }`}
              title={isLoggedIn ? "Report" : "Login to report"}
            >
              <AlertTriangle className="w-5 h-5" />
            </button>

            {userId === note.owner && (
              <>
                <button
                  onClick={() => setIsEditModalOpen(true)}
                  className="p-2 text-gray-400 hover:text-teal-500 transition-colors rounded-full hover:bg-gray-700/50"
                  title="Edit note"
                >
                  <PencilIcon className="w-5 h-5" />
                </button>
                <button
                  onClick={handleDelete}
                  className="p-2 text-gray-400 hover:text-red-500 transition-colors rounded-full hover:bg-gray-700/50"
                  title="Delete note"
                >
                  <Trash2 className="w-5 h-5" />
                </button>
              </>
            )}
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

      <CommentModal
        isOpen={isCommentModalOpen}
        onClose={() => setIsCommentModalOpen(false)}
        noteId={note.id}
      />
      <ReportModal
        isOpen={isReportModalOpen}
        onClose={() => setIsReportModalOpen(false)}
        noteId={note.id}
      />

      <NoteEditModal
        isOpen={isEditModalOpen}
        onClose={() => setIsEditModalOpen(false)}
        note={note}
        onUpdateSuccess={triggerNoteRefresh}
      />
    </>
  );
};

export default NoteCard;
