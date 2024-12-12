import { useState, useEffect } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";
import NoteBreadcrumb from "./NoteBreadcrumb";

const CourseSemestersPage = () => {
  const { majorCode, courseId } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const courseData = state?.courseData;

  const [semesters, setSemesters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // fetch and process semester data
  useEffect(() => {
    const fetchData = async () => {
      // validate data
      if (!courseData?.code) {
        console.error("No course code available", courseData);
        return;
      }

      try {
        // fetch sections for the course
        const response = await fetch(
          `/api/section?courseCode=${courseData.code}&return=object`,
        );
        const data = await response.json();

        if (!data.good) {
          throw new Error(data.error || "Failed to fetch sections");
        }

        // get unique semester ids from sections
        const sectionDetails = data.data;
        const uniqueSemesterIds = [
          ...new Set(sectionDetails.map((section) => section.semester)),
        ].filter(Boolean);

        // get detailed data for each semester
        const semesterDetails = await Promise.all(
          uniqueSemesterIds.map(async (semesterId) => {
            const semesterResponse = await fetch(`/api/semester/${semesterId}`);
            const semesterData = await semesterResponse.json();
            return semesterData.data;
          }),
        );

        const sortedSemesters = semesterDetails.sort((a, b) => {
          if (a.year !== b.year) {
            return b.year - a.year;
          }
          const seasonOrder = { spring: 0, summer: 1, fall: 2 };
          return seasonOrder[b.season] - seasonOrder[a.season];
        });

        setSemesters(sortedSemesters);
      } catch (err) {
        console.error("Error fetching data:", err);
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [courseData?.code]);

  // handle navigation to specific semester
  const handleSemesterClick = (semester) => {
    navigate(
      `/${majorCode}/${courseId}/${semester.season.toLowerCase()}-${
        semester.year
      }`,
      {
        state: {
          courseData,
          semesterData: semester,
        },
      },
    );
  };

  if (!courseData) return <div>No course data available</div>;

  if (loading)
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );

  if (error) return <div className="text-red-500 text-center p-4">{error}</div>;

  return (
    <div className="min-h-screen bg-gray-900">
      <div className="max-w-[1920px] mx-auto px-4 sm:px-24">
        <div className="py-6">
          <NoteBreadcrumb
            majorCode={majorCode}
            courseData={courseData}
            semesterData={null}
          />
        </div>

        {/* semesters list */}
        <div className="space-y-6">
          {semesters.length > 0 ? (
            semesters.map((semester) => (
              <div
                key={semester.id}
                className="bg-gray-800 p-6 rounded-lg hover:bg-gray-700 cursor-pointer transition-colors"
                onClick={() => handleSemesterClick(semester)}
              >
                <h2 className="text-lg font-bold text-teal-400">
                  {semester.season.charAt(0).toUpperCase() +
                    semester.season.slice(1)}{" "}
                  {semester.year}
                </h2>
              </div>
            ))
          ) : (
            // empty message
            <div className="text-gray-400 text-center p-6 bg-gray-800 rounded-lg">
              No semesters found for this course
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default CourseSemestersPage;
