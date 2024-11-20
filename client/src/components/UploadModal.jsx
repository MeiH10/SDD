import React, { useState } from "react";
import Modal from "./Modal";
import { useAuth } from "../context/AuthContext";

const UploadModal = () => {
  const { userId } = useAuth();

  const [isOpen, setIsOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [isAnonymous, setIsAnonymous] = useState(false);
  const [selectedFileName, setSelectedFileName] = useState("");

  const [values, setValues] = useState({
    courseCode: "",
    noteName: "",
    fileType: "Lecture Note",
    professor: "",
    semester: "Summer 2024",
    file: null,
    videoLink: "",
    tags: "",
    description: "",
  });
  const [errors, setErrors] = useState({});

  const validateForm = () => {
    const newErrors = {};
    if (!values.courseCode) newErrors.courseCode = "Course code is required";
    if (!values.noteName) newErrors.noteName = "Note name is required";
    if (!values.file) newErrors.file = "File is required";
    else {
      const validTypes = ["application/pdf", "image/jpeg", "image/png"];
      if (!validTypes.includes(values.file.type)) {
        newErrors.file = "Only PDF, JPG, and PNG files are allowed";
      }
      if (values.file.size > 2 * 1024 * 1024) {
        newErrors.file = "File size must be less than 2MB";
      }
    }
    if (!values.description) newErrors.description = "Description is required";
    return newErrors;
  };

  const handleChange = (e) => {
    const { name, type, files } = e.target;
    if (type === "file" && files[0]) {
      setValues((prev) => ({
        ...prev,
        file: files[0],
      }));
      setSelectedFileName(files[0].name);
    } else {
      setValues((prev) => ({
        ...prev,
        [name]: e.target.value,
      }));
    }
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
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
      formData.append("title", values.noteName);
      formData.append("file", values.file);

      const response = await fetch("/api/note", {
        method: "POST",
        body: formData,
      });

      if (response.ok) {
        setIsOpen(false);
        setValues({
          courseCode: "",
          noteName: "",
          fileType: "Lecture Note",
          professor: "",
          semester: "Summer 2024",
          file: null,
          videoLink: "",
          tags: "",
          description: "",
        });
        setSelectedFileName("");
      } else {
        throw new Error("Upload failed");
      }
    } catch (error) {
      setErrors({ submit: error.message });
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <button
        onClick={() => setIsOpen(true)}
        className="hidden sm:block bg-teal-500 px-4 py-2 rounded-lg text-white hover:bg-teal-400 transition-colors"
      >
        Upload
      </button>

      <Modal
        isOpen={isOpen}
        onClose={() => !isLoading && setIsOpen(false)}
        title="Add your note"
        width="max-w-2xl"
      >
        <div className="flex justify-between items-center mb-6">
          <span className="text-gray-300">Upload anonymously</span>
          <button
            onClick={() => setIsAnonymous(!isAnonymous)}
            className={`w-12 h-6 rounded-full p-1 transition-colors ${
              isAnonymous ? "bg-teal-500" : "bg-gray-600"
            }`}
          >
            <div
              className={`w-4 h-4 bg-white rounded-full transition-transform ${
                isAnonymous ? "translate-x-6" : "translate-x-0"
              }`}
            />
          </button>
        </div>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <input
              name="courseCode"
              type="text"
              placeholder="e.g. MATH 4100"
              value={values.courseCode}
              onChange={handleChange}
              className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${
                errors.courseCode ? "border-red-500" : "border-gray-600"
              } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
              disabled={isLoading}
            />
            {errors.courseCode && (
              <p className="text-red-500 text-sm mt-1">{errors.courseCode}</p>
            )}
          </div>

          <div>
            <input
              name="noteName"
              type="text"
              placeholder="Lec 1 Note"
              value={values.noteName}
              onChange={handleChange}
              className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${
                errors.noteName ? "border-red-500" : "border-gray-600"
              } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
              disabled={isLoading}
            />
            {errors.noteName && (
              <p className="text-red-500 text-sm mt-1">{errors.noteName}</p>
            )}
          </div>

          <div className="flex gap-4">
            <select
              name="fileType"
              value={values.fileType}
              onChange={handleChange}
              className="w-1/3 bg-gray-700 text-white rounded-lg border border-gray-600 px-3 py-2"
            >
              <option value="Lecture Note">Lecture Note</option>
              <option value="Summary">Summary</option>
              <option value="Practice Problem">Practice Problem</option>
            </select>
            <select
              name="semester"
              value={values.semester}
              onChange={handleChange}
              className="w-2/3 bg-gray-700 text-white rounded-lg border border-gray-600 px-3 py-2"
            >
              <option>Summer 2024</option>
              <option>Fall 2023</option>
              <option>Spring 2024</option>
            </select>
          </div>

          <div>
            <input
              name="professor"
              type="text"
              placeholder="e.g. Maria Brown"
              value={values.professor}
              onChange={handleChange}
              className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600"
            />
          </div>

          <div className="border-2 border-dashed border-gray-600 rounded-lg p-8 text-center">
            <input
              id="file-upload"
              name="file"
              type="file"
              accept=".pdf,.jpg,.png"
              onChange={handleChange}
              className="hidden"
            />
            <label
              htmlFor="file-upload"
              className="cursor-pointer flex flex-col items-center"
            >
              {selectedFileName ? (
                <>
                  <div className="text-teal-500 mb-2">Selected file:</div>
                  <div className="text-white">{selectedFileName}</div>
                  <div className="text-gray-400 text-sm mt-2">
                    Click to change file
                  </div>
                </>
              ) : (
                <>
                  <svg
                    className="w-12 h-12 text-gray-400 mb-4"
                    fill="none"
                    viewBox="0 0 24 24"
                    stroke="currentColor"
                  >
                    <path
                      strokeLinecap="round"
                      strokeLinejoin="round"
                      strokeWidth={2}
                      d="M12 6v6m0 0v6m0-6h6m-6 0H6"
                    />
                  </svg>
                  <span className="text-white">Click to upload</span>
                  <span className="text-gray-400 text-sm mt-2">
                    or drag and drop
                  </span>
                  <span className="text-gray-400 text-sm mt-1">
                    SVG, PNG, JPG or PDF (max. 2MB)
                  </span>
                </>
              )}
            </label>
            {errors.file && (
              <p className="text-red-500 text-sm mt-2">{errors.file}</p>
            )}
          </div>
          <input
            name="videoLink"
            type="text"
            placeholder="e.g. https://www.youtube.com/watch?v=GngBZDNZMy4"
            value={values.videoLink}
            onChange={handleChange}
            className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600"
          />

          <input
            name="tags"
            type="text"
            placeholder="Add 1-10 tags, separated by comma. For example, clear, examples, concise, ..."
            value={values.tags}
            onChange={handleChange}
            className="w-full px-3 py-2 bg-gray-700 text-white rounded-lg border border-gray-600"
          />

          <textarea
            name="description"
            placeholder="What topic does the note cover..."
            value={values.description}
            onChange={handleChange}
            rows="3"
            className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${
              errors.description ? "border-red-500" : "border-gray-600"
            } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none resize-none`}
            disabled={isLoading}
          />
          {errors.description && (
            <p className="text-red-500 text-sm mt-1">{errors.description}</p>
          )}

          <div className="flex gap-4 pt-4">
            <button
              type="button"
              onClick={() => setIsOpen(false)}
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
                  Uploading...
                </>
              ) : (
                "Confirm"
              )}
            </button>
          </div>
        </form>
      </Modal>
    </>
  );
};

export default UploadModal;
