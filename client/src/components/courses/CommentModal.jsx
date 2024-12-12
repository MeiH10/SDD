import { useState, useEffect } from "react";
import {
  MessageSquare,
  ChevronUp,
  ChevronDown,
  Trash2,
  PencilIcon,
  AlertTriangle,
} from "lucide-react";
import { useAuth } from "../../context/AuthContext";
import Modal from "../Modal";
import CommentEditModal from "./CommentEditModal";
import ReportModal from "../reports/ReportModal";

const CommentModal = ({ isOpen, onClose, noteId }) => {
  const [comments, setComments] = useState([]);
  const [newComment, setNewComment] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);
  const [commentAuthors, setCommentAuthors] = useState({});
  const [editingComment, setEditingComment] = useState(null);
  const [reportingComment, setReportingComment] = useState(null);
  const { isLoggedIn, userId } = useAuth();

  // fetch comments when modal opens or note changes
  useEffect(() => {
    if (isOpen && noteId) {
      fetchComments();
    }
  }, [isOpen, noteId]);

  // fetch comments and their author details
  const fetchComments = async () => {
    setIsLoading(true);
    try {
      const response = await fetch(
        `/api/comment?noteID=${noteId}&sort=date&order=desc&return=object`,
      );
      const data = await response.json();
      if (!data.good) throw new Error(data.error || "Failed to fetch comments");
      setComments(data.data);

      // fetch usernames for all unique comment authors
      const authorIds = [
        ...new Set(data.data.map((comment) => comment.account)),
      ];
      const authorResponses = await Promise.all(
        authorIds.map((id) =>
          fetch(`/api/account/${id}`).then((res) => res.json()),
        ),
      );

      // create a mapping of user ids to usernames
      const authorMap = {};
      authorResponses.forEach((response) => {
        if (response.good) {
          authorMap[response.data.id] = response.data.username;
        }
      });
      setCommentAuthors(authorMap);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!newComment.trim()) return;

    setIsLoading(true);
    try {
      const formData = new URLSearchParams();
      formData.append("body", newComment);
      formData.append("noteID", noteId);

      const response = await fetch("/api/comment", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData,
      });

      const data = await response.json();
      if (!data.good) throw new Error(data.error || "Failed to post comment");

      // refresh comments and reset input
      await fetchComments();
      setNewComment("");
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  // handle comment deletion
  const handleDelete = async (commentId) => {
    if (!window.confirm("Are you sure you want to delete this comment?"))
      return;
    try {
      const response = await fetch(`/api/comment/${commentId}`, {
        method: "DELETE",
      });
      if (!response.ok) throw new Error("Failed to delete comment");
      await fetchComments();
    } catch (error) {
      setError(error.message);
    }
  };

  const handleVote = async (commentId, isUpvote) => {
    if (!isLoggedIn) return;

    try {
      // toggle like status based on vote type
      let response;
      if (isUpvote) {
        response = await fetch(`/api/comment/${commentId}/like`, {
          method: "PUT",
        });
      } else {
        response = await fetch(`/api/comment/${commentId}/like`, {
          method: "DELETE",
        });
      }

      if (!response.ok) throw new Error("Failed to update vote");
      await fetchComments();
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <>
      <Modal
        isOpen={isOpen}
        onClose={onClose}
        title="Comments"
        width="max-w-2xl"
      >
        <div className="flex flex-col h-[70vh]">
          <div className="flex-1 overflow-y-auto mb-4">
            {isLoading && !comments.length ? (
              // loading skelton
              <div className="flex justify-center items-center h-32">
                <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent" />
              </div>
            ) : comments.length === 0 ? (
              // empty state message
              <div className="text-center text-gray-400 py-8">
                No comments yet. Be the first to comment!
              </div>
            ) : (
              // comments list
              <div className="space-y-4">
                {comments.map((comment) => (
                  <div
                    key={comment.id}
                    className="bg-gray-700 rounded-lg p-4 relative"
                  >
                    <div className="flex items-start">
                      {/* voting buttons */}
                      <div className="flex flex-col items-center mr-4">
                        <button
                          onClick={() => handleVote(comment.id, true)}
                          disabled={!isLoggedIn}
                          className={`transition-colors ${
                            !isLoggedIn
                              ? "text-gray-600 cursor-not-allowed"
                              : "text-gray-400 hover:text-teal-500"
                          }`}
                        >
                          <ChevronUp className="w-5 h-5" />
                        </button>
                        <span className="text-white my-1 text-sm">
                          {comment.totalLikes}
                        </span>
                        <button
                          onClick={() => handleVote(comment.id, false)}
                          disabled={!isLoggedIn}
                          className={`transition-colors ${
                            !isLoggedIn
                              ? "text-gray-600 cursor-not-allowed"
                              : "text-gray-400 hover:text-red-500"
                          }`}
                        >
                          <ChevronDown className="w-5 h-5" />
                        </button>
                      </div>

                      {/* comment content */}
                      <div className="flex-1">
                        <div className="flex items-center mb-2">
                          <span className="text-sm text-gray-400">
                            {commentAuthors[comment.account] || "Anonymous"} •{" "}
                            {new Date(comment.createdDate).toLocaleDateString()}
                          </span>
                        </div>
                        <p className="text-white">{comment.description}</p>
                      </div>

                      {/* action buttons */}
                      <div className="absolute top-2 right-2 flex gap-2">
                        <button
                          onClick={() => setReportingComment(comment)}
                          disabled={!isLoggedIn}
                          className={`p-2 transition-colors rounded-full ${
                            isLoggedIn
                              ? "text-gray-400 hover:text-red-500 hover:bg-gray-600/50"
                              : "text-gray-600 cursor-not-allowed"
                          }`}
                          title={
                            isLoggedIn ? "Report comment" : "Login to report"
                          }
                        >
                          <AlertTriangle className="w-4 h-4" />
                        </button>

                        {/* edit and delete buttons */}
                        {userId === comment.account && (
                          <>
                            <button
                              onClick={() => setEditingComment(comment)}
                              className="p-2 text-gray-400 hover:text-teal-500 hover:bg-gray-600/50 transition-colors rounded-full"
                              title="Edit comment"
                            >
                              <PencilIcon className="w-4 h-4" />
                            </button>
                            <button
                              onClick={() => handleDelete(comment.id)}
                              className="p-2 text-gray-400 hover:text-red-500 hover:bg-gray-600/50 transition-colors rounded-full"
                              title="Delete comment"
                            >
                              <Trash2 className="w-4 h-4" />
                            </button>
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>

          <div className="mt-auto">
            {error && <p className="text-red-500 text-sm mb-2">{error}</p>}
            <form onSubmit={handleSubmit} className="flex gap-2">
              <input
                type="text"
                value={newComment}
                onChange={(e) => setNewComment(e.target.value)}
                placeholder={
                  isLoggedIn ? "Write a comment..." : "Login to comment"
                }
                disabled={!isLoggedIn || isLoading}
                className="flex-1 px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none disabled:opacity-50 disabled:cursor-not-allowed"
              />
              <button
                type="submit"
                disabled={!isLoggedIn || isLoading || !newComment.trim()}
                className="bg-teal-500 text-white px-4 py-2 rounded-lg hover:bg-teal-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
              >
                Post
              </button>
            </form>
          </div>
        </div>
      </Modal>

      {/* edit comment */}
      {editingComment && (
        <CommentEditModal
          isOpen={!!editingComment}
          onClose={() => setEditingComment(null)}
          comment={editingComment}
          onUpdateSuccess={() => {
            fetchComments();
            setEditingComment(null);
          }}
        />
      )}

      {/* report comment */}
      {reportingComment && (
        <ReportModal
          isOpen={!!reportingComment}
          onClose={() => setReportingComment(null)}
          type="comment"
          itemId={reportingComment.id}
        />
      )}
    </>
  );
};

export default CommentModal;
