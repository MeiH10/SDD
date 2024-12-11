import { useState, useEffect } from "react";
import { useParams, useNavigate, useLocation } from "react-router-dom";

const CourseSectionsPage = () => {
  const { majorCode, courseId } = useParams();
  const navigate = useNavigate();
  const { state } = useLocation();
  const courseData = state?.courseData;

  const [sections, setSections] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSections = async () => {
      try {
        const response = await fetch(
          `/api/section?courseCode=${courseData.code}&sort=number&order=asc`,
        );
        const data = await response.json();
        if (!data.good) {
          throw new Error(data.error || "Failed to fetch sections");
        }

        const sectionsData = await Promise.all(
          data.data.map(async (sectionId) => {
            const detailResponse = await fetch(`/api/section/${sectionId}`);
            const detailData = await detailResponse.json();
            return detailData.data;
          }),
        );

        setSections(sectionsData);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchSections();
  }, [courseData.code]);

  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  if (error) {
    return <div className="text-red-500 text-center p-4">{error}</div>;
  }

  return (
    <div className="px-4 sm:px-24 mx-auto">
      <div className="bg-teal-500 p-4 rounded-t-lg">
        <div className="flex items-center">
          <button
            onClick={() => navigate(`/${majorCode}`)}
            className="mr-4 text-white hover:text-gray-200 transition-colors"
          >
            ← Back
          </button>
          <h1 className="text-xl text-white font-bold">
            {courseData?.code}: {courseData?.name}
          </h1>
        </div>
      </div>

      <div className="space-y-2">
        {sections.map((section) => (
          <div
            key={section.id || section._id}
            className="bg-gray-800 p-6 hover:bg-gray-700 transition-colors border border-gray-700"
            onClick={() =>
              navigate(`/${majorCode}/${courseId}/${section.number}`, {
                state: { courseData },
              })
            }
          >
            <div className="flex justify-between items-start">
              <div>
                <h2 className="text-lg font-bold text-white mb-2">
                  Section {section.number}
                </h2>
                {section.professors && section.professors.length > 0 && (
                  <div className="text-gray-300">
                    Professor: {section.professors[0]}
                  </div>
                )}
              </div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
};

export default CourseSectionsPage;
