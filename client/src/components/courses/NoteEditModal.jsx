import { useState } from "react";
import Modal from "../Modal";

const NoteEditModal = ({ isOpen, onClose, note, onUpdateSuccess }) => {
    const [values, setValues] = useState({
        title: note.title || "",
        description: note.description || "",
        link: note.link || "",
        tags: note.tags?.join(", ") || "",
        file: null,
        anonymous: note.anonymous || false,
    });
    const [isLoading, setIsLoading] = useState(false);
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        if (!values.title) newErrors.title = "Title is required";
        if (!values.description) newErrors.description = "Description is required";
        if (values.link && !isValidUrl(values.link)) {
            newErrors.link = "Please enter a valid URL";
        }
        return newErrors;
    };

    const isValidUrl = (string) => {
        try {
            new URL(string);
            return true;
        } catch (_) {
            return false;
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formErrors = validateForm();
        if (Object.keys(formErrors).length > 0) {
            setErrors(formErrors);
            return;
        }

        setIsLoading(true);
        try {
            const formData = new FormData();
            if (values.title !== note.title) formData.append("title", values.title);
            if (values.description !== note.description)
                formData.append("description", values.description);
            if (values.link !== note.link) formData.append("link", values.link);
            if (values.anonymous !== note.anonymous)
                formData.append("anonymous", values.anonymous);
            if (values.file) formData.append("file", values.file);

            const tagArray = values.tags
                .split(",")
                .map((tag) => tag.trim())
                .filter(Boolean);
            if (JSON.stringify(tagArray) !== JSON.stringify(note.tags)) {
                formData.append("tags", tagArray);
            }

            const response = await fetch(`/api/note/${note.id}`, {
                method: "PUT",
                body: formData,
            });

            const data = await response.json();
            if (!data.good) throw new Error(data.error || "Failed to update note");

            onUpdateSuccess();
            onClose();
        } catch (error) {
            setErrors({ submit: error.message });
        } finally {
            setIsLoading(false);
        }
    };

    return (
        <Modal
            isOpen={isOpen}
            onClose={onClose}
            title="Edit Note"
            width="max-w-2xl"
        >
            <form onSubmit={handleSubmit} className="space-y-4">
                <div>
                    <input
                        type="text"
                        value={values.title}
                        onChange={(e) =>
                            setValues((prev) => ({ ...prev, title: e.target.value }))
                        }
                        placeholder="Note Title"
                        className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.title ? "border-red-500" : "border-gray-600"
                            } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                        disabled={isLoading}
                    />
                    {errors.title && (
                        <p className="text-red-500 text-sm mt-1">{errors.title}</p>
                    )}
                </div>

                <div>
                    <textarea
                        value={values.description}
                        onChange={(e) =>
                            setValues((prev) => ({ ...prev, description: e.target.value }))
                        }
                        placeholder="Description"
                        rows="3"
                        className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.description ? "border-red-500" : "border-gray-600"
                            } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none resize-none`}
                        disabled={isLoading}
                    />
                    {errors.description && (
                        <p className="text-red-500 text-sm mt-1">{errors.description}</p>
                    )}
                </div>

                <div>
                    <input
                        type="text"
                        value={values.link}
                        onChange={(e) =>
                            setValues((prev) => ({ ...prev, link: e.target.value }))
                        }
                        placeholder="Related Link (optional)"
                        className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.link ? "border-red-500" : "border-gray-600"
                            } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                        disabled={isLoading}
                    />
                    {errors.link && (
                        <p className="text-red-500 text-sm mt-1">{errors.link}</p>
                    )}
                </div>

                <div>
                    <input
                        type="text"
                        value={values.tags}
                        onChange={(e) =>
                            setValues((prev) => ({ ...prev, tags: e.target.value }))
                        }
                        placeholder="Tags (comma separated)"
                        className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600 focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none"
                        disabled={isLoading}
                    />
                </div>

                <div className="border-2 border-dashed border-gray-600 rounded-lg p-8 text-center">
                    <input
                        id="file-upload"
                        type="file"
                        onChange={(e) =>
                            setValues((prev) => ({ ...prev, file: e.target.files[0] }))
                        }
                        className="hidden"
                        accept=".pdf,.jpg,.png"
                        disabled={isLoading}
                    />
                    <label
                        htmlFor="file-upload"
                        className="cursor-pointer flex flex-col items-center"
                    >
                        <span className="text-white mb-2">
                            Click to upload new file (optional)
                        </span>
                        <span className="text-gray-400 text-sm">
                            PDF, JPG or PNG (max. 2MB)
                        </span>
                        {values.file && (
                            <span className="text-teal-500 mt-2">{values.file.name}</span>
                        )}
                    </label>
                </div>

                <div className="flex items-center justify-between">
                    <label className="flex items-center space-x-2 cursor-pointer">
                        <input
                            type="checkbox"
                            checked={values.anonymous}
                            onChange={(e) =>
                                setValues((prev) => ({ ...prev, anonymous: e.target.checked }))
                            }
                            className="form-checkbox h-4 w-4 text-teal-500 rounded border-gray-600 bg-gray-700 focus:ring-teal-500"
                            disabled={isLoading}
                        />
                        <span className="text-gray-300">Post anonymously</span>
                    </label>
                </div>

                {errors.submit && (
                    <p className="text-red-500 text-sm">{errors.submit}</p>
                )}

                <div className="flex gap-4 pt-4">
                    <button
                        type="button"
                        onClick={onClose}
                        className="flex-1 bg-white text-gray-900 py-2 px-4 rounded-lg hover:bg-gray-100 transition-colors"
                        disabled={isLoading}
                    >
                        Cancel
                    </button>
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="flex-1 bg-teal-500 text-white py-2 px-4 rounded-lg hover:bg-teal-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                    >
                        {isLoading ? (
                            <>
                                <svg
                                    className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                >
                                    <circle
                                        className="opacity-25"
                                        cx="12"
                                        cy="12"
                                        r="10"
                                        stroke="currentColor"
                                        strokeWidth="4"
                                    ></circle>
                                    <path
                                        className="opacity-75"
                                        fill="currentColor"
                                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                                    ></path>
                                </svg>
                                Updating...
                            </>
                        ) : (
                            "Update Note"
                        )}
                    </button>
                </div>
            </form>
        </Modal>
    );
};

export default NoteEditModal;
