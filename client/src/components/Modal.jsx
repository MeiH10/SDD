import React from "react";

const Modal = ({ isOpen, onClose, children, title, width = "max-w-md" }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50">
      {/* semi-transparent background */}
      <div
        className="absolute inset-0 bg-black bg-opacity-50 transition-opacity duration-300"
        onClick={onClose}
      />

      <div className="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-0">
        <div
          className={`relative bg-gray-800 rounded-lg w-full ${width} mx-auto transform transition-all duration-300 shadow-xl`}
        >
          <div className="p-6">
            {/*  header */}
            <h2 className="text-xl font-bold text-white mb-4">{title}</h2>
            
            {/*  content */}
            {children}
            
            {/* close button */}
            <button
              onClick={onClose}
              className="absolute top-4 right-4 text-gray-400 hover:text-white transition-colors"
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
        </div>
      </div>
    </div>
  );
};

export default Modal;