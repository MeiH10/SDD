import { useState, useEffect } from "react";
import { useSearchParams, useNavigate } from "react-router-dom";

const SearchResults = () => {
  const [searchParams] = useSearchParams();
  const query = searchParams.get("q");
  const navigate = useNavigate();
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchCourses = async () => {
      setLoading(true);
      try {
        const response = await fetch(
          `/api/course?name=${encodeURIComponent(
            query
          )}&return=object&sort=code&order=asc`
        );
        const data = await response.json();
        if (!data.good) {
          throw new Error(data.error || "Failed to fetch courses");
        }
        setCourses(data.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    if (query) {
      fetchCourses();
    }
  }, [query]);

  if (loading) {
    return (
      <div className="px-4 sm:px-24 mx-auto">
        <div className="flex justify-center items-center h-64">
          <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="px-4 sm:px-24 mx-auto">
        <div className="text-red-500 text-center p-4">{error}</div>
      </div>
    );
  }

  return (
    <div className="px-4 sm:px-24 mx-auto">
      <div className="bg-teal-500 p-4 rounded-t-lg">
        <div className="flex items-center">
          <button
            onClick={() => navigate("/")}
            className="mr-4 text-white hover:text-gray-200 transition-colors"
          >
            ← Back
          </button>
          <h1 className="text-xl text-white font-bold">
            Search Results for "{query}"
          </h1>
        </div>
      </div>

      {courses.length === 0 ? (
        <div className="bg-gray-800 p-6 text-center text-gray-400">
          No courses found matching "{query}"
        </div>
      ) : (
        <div className="space-y-2">
          {courses.map((course) => (
            <div
              key={course.id}
              onClick={() =>
                navigate(`/${course.major}/${course.id}`, {
                  state: { courseData: course },
                })
              }
              className="bg-gray-800 p-4 hover:bg-gray-700 transition-colors cursor-pointer border border-gray-700"
            >
              <div className="flex items-baseline gap-2">
                <h2 className="text-lg font-bold text-white">{course.code}:</h2>
                <h3 className="text-white">{course.name}</h3>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default SearchResults;
