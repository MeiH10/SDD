import { useState, useCallback, useEffect } from 'react';

// Helper functions to fetch majors, semesters, and courses based on API
const fetchMajors = async () => {
    const response = await fetch('/api/major');
    const data = await response.json();
    return data.data;  // Assuming the response is wrapped in a `data` field
};

const fetchSemesters = async () => {
    const response = await fetch('/api/semester');
    const data = await response.json();
    return data.data;  // Assuming the response is wrapped in a `data` field
};

const fetchCourses = async (majorId) => {
    const response = await fetch(`/api/course?major=${majorId}`);
    const data = await response.json();
    return data.data;  // Assuming the response is wrapped in a `data` field
};

const UseCase2 = () => {
    const [major, setMajor] = useState('');
    const [semester, setSemester] = useState('');
    const [course, setCourse] = useState('');
    const [file, setFile] = useState(null);
    const [title, setTitle] = useState('');
    const [description, setDescription] = useState('');
    const [noteType, setNoteType] = useState('');
    const [error, setError] = useState('');
    const [noteId, setNoteId] = useState(null);

    const [majors, setMajors] = useState([]);
    const [semesters, setSemesters] = useState([]);
    const [courses, setCourses] = useState([]);

    const [step, setStep] = useState(1);  // Step to keep track of the flow

    // Fetching majors on component mount
    useEffect(() => {
        const loadMajors = async () => {
            try {
                const fetchedMajors = await fetchMajors();
                setMajors(fetchedMajors);
            } catch (err) {
                console.log(err);
                setError('Error loading majors');
            }
        };
        loadMajors();
    }, []);

    const handleMajorSelect = useCallback(async (selectedMajor) => {
        setMajor(selectedMajor);
        try {
            const fetchedSemesters = await fetchSemesters();
            setSemesters(fetchedSemesters);
            setStep(2);  // Move to semester selection step
        } catch (err) {
            console.log(err);
            setError('Error loading semesters');
        }
    }, []);

    const handleSemesterSelect = useCallback(async (selectedSemester) => {
        setSemester(selectedSemester);
        try {
            const fetchedCourses = await fetchCourses(major);
            setCourses(fetchedCourses);
            setStep(3);  // Move to course selection step
        } catch (err) {
            console.log(err);
            setError('Error loading courses');
        }
    }, [major]);

    const handleCourseSelect = useCallback((selectedCourse) => {
        setCourse(selectedCourse);
        setStep(4);  // Move to note creation step
    }, []);

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        setError('');

        // Validate the form fields
        if (!file || !title || !course || !noteType) {
            setError('All fields are required!');
            return;
        }

        // Prepare FormData for note submission
        const formData = new FormData();
        formData.append('title', title);
        formData.append('file', file);
        formData.append('course', course);

        try {
            const response = await fetch('/api/note', {
                method: 'POST',
                body: formData,
            });

            if (!response.ok) {
                const errorMessage = await response.text();
                setError(`Error: ${errorMessage}`);
                return;
            }

            const result = await response.text();
            setNoteId(result);  // Assuming the API returns the note ID in the response
            setStep(5);  // Successfully created note, show the result
        } catch (err) {
            setError(`Error: ${err.message}`);
        }
    };

    const renderStep = () => {
        switch (step) {
            case 1:
                return (
                    <div>
                        <h2>Select a Major</h2>
                        <select onChange={(e) => handleMajorSelect(e.target.value)} value={major}>
                            <option value="">-- Select Major --</option>
                            {majors?.map((majorOption) => (
                                <option key={majorOption} value={majorOption}>
                                    {majorOption}
                                </option>
                            ))}
                        </select>
                    </div>
                );
            case 2:
                return (
                    <div>
                        <h2>Select a Semester</h2>
                        <select onChange={(e) => handleSemesterSelect(e.target.value)} value={semester}>
                            <option value="">-- Select Semester --</option>
                            {semesters?.map((semesterOption) => (
                                <option key={semesterOption} value={semesterOption}>
                                    {semesterOption}
                                </option>
                            ))}
                        </select>
                    </div>
                );
            case 3:
                return (
                    <div>
                        <h2>Select a Course</h2>
                        <select onChange={(e) => handleCourseSelect(e.target.value)} value={course}>
                            <option value="">-- Select Course --</option>
                            {courses?.map((courseOption) => (
                                <option key={courseOption} value={courseOption}>
                                    {courseOption}
                                </option>
                            ))}
                        </select>
                    </div>
                );
            case 4:
                return (
                    <div>
                        <h2>Upload Note</h2>
                        <form onSubmit={handleSubmit}>
                            <div>
                                <label>Title:</label>
                                <input
                                    type="text"
                                    value={title}
                                    onChange={(e) => setTitle(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label>Description:</label>
                                <textarea
                                    value={description}
                                    onChange={(e) => setDescription(e.target.value)}
                                />
                            </div>
                            <div>
                                <label>Note Type:</label>
                                <input
                                    type="text"
                                    value={noteType}
                                    onChange={(e) => setNoteType(e.target.value)}
                                    required
                                />
                            </div>
                            <div>
                                <label>File:</label>
                                <input type="file" onChange={handleFileChange} required />
                            </div>
                            {error && <p style={{ color: 'red' }}>{error}</p>}
                            <button type="submit">Create Note</button>
                        </form>
                    </div>
                );
            case 5:
                return (
                    <div>
                        <h2>Note Created Successfully!</h2>
                        <p>Your note has been created with ID: {noteId}</p>
                    </div>
                );
            default:
                return null;
        }
    };

    return (
        <div>
            <h1>Upload a Note</h1>
            {renderStep()}
        </div>
    );
};

export default UseCase2;
