import React, { useEffect, useState } from "react";

const PreviewModal = ({ isOpen, onClose, noteId }) => {
  const [file, setFile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFile = async () => {
      if (!noteId) return;

      try {
        setLoading(true);
        const response = await fetch(`/api/note/${noteId}/file`);
        if (!response.ok) throw new Error("Failed to load file");

        const blob = await response.blob();
        const url = URL.createObjectURL(blob);
        setFile(url);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (isOpen) {
      fetchFile();
    }

    return () => {
      if (file) URL.revokeObjectURL(file);
    };
  }, [isOpen, noteId]);

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex justify-end">
      {/* Backdrop */}
      <div
        className="absolute inset-0 bg-black/50 transition-opacity duration-300"
        onClick={onClose}
      />

      {/* Modal */}
      <div className="relative w-1/2 bg-gray-900 h-full overflow-hidden shadow-xl">
        <div className="absolute top-4 right-4 z-10">
          <button
            onClick={onClose}
            className="text-gray-400 hover:text-white transition-colors"
          >
            <svg
              className="w-6 h-6"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                strokeWidth={2}
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <div className="h-full p-6">
          {loading ? (
            <div className="flex items-center justify-center h-full">
              <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent" />
            </div>
          ) : error ? (
            <div className="flex items-center justify-center h-full text-red-500">
              {error}
            </div>
          ) : (
            <object
              data={file}
              type="application/pdf"
              className="w-full h-full rounded-lg"
            >
              <embed src={file} className="w-full h-full rounded-lg" />
            </object>
          )}
        </div>
      </div>
    </div>
  );
};

export default PreviewModal;
