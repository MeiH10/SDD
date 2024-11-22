import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const SchoolMajorSelector = () => {
  const navigate = useNavigate();
  const [selectedSchool, setSelectedSchool] = useState(null);
  const [schools, setSchools] = useState([]);
  const [allMajors, setAllMajors] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const [schoolsResponse, majorsResponse] = await Promise.all([
          fetch('/api/school?return=object'),
          fetch('/api/major?return=object')
        ]);

        const schoolsData = await schoolsResponse.json();
        const majorsData = await majorsResponse.json();

        if (!schoolsData.good || !majorsData.good) {
          throw new Error('Failed to fetch data');
        }

        setSchools(schoolsData.data);
        setAllMajors(majorsData.data);
        setLoading(false);
      } catch (err) {
        console.error('Error fetching data:', err);
        setError(err.message);
        setLoading(false);
      }
    };

    fetchData();
  }, []);

  const filteredMajors = selectedSchool 
    ? allMajors.filter(major => major.school === selectedSchool.id)
    : [];

  const handleMajorClick = (major) => {
    navigate(`/${major.code}`, { 
      state: { majorName: major.name }
    });
  };

  if (loading) {
    return (
      <div className="flex justify-center items-center">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  if (error) {
    return <div className="text-red-500 text-center">Error: {error}</div>;
  }

  return (
    <div className="px-4 sm:px-24 mx-auto pt-10">
      <div className="flex">
        {/* schools */}
        <div className="w-1/3 p-4 bg-gray-800 border-r border-gray-600">
          <h2 className="text-lg font-bold text-teal-400 mb-4">School</h2>
          <ul className="space-y-2">
            {schools.map((school) => (
              <li
                key={school.id}
                className={`p-2 ${
                  selectedSchool?.id === school.id ? 'bg-gray-600' : 'bg-gray-700'
                } rounded-lg hover:bg-gray-600 cursor-pointer transition-colors duration-200`}
                onClick={() => setSelectedSchool(school)}
              >
                {school.name}
              </li>
            ))}
          </ul>
        </div>

        {/* majors */}
        <div className="w-2/3 p-4 bg-gray-800">
          <h2 className="text-lg font-bold text-teal-400 mb-4">Major</h2>
          <div className="grid gap-4">
            {selectedSchool ? (
              filteredMajors.length > 0 ? (
                filteredMajors.map((major) => (
                  <div
                    key={major.id}
                    className="p-4 bg-gray-700 rounded-lg hover:bg-gray-600 cursor-pointer transition-colors duration-200"
                    onClick={() => handleMajorClick(major)}
                  >
                    {major.code}: {major.name}
                  </div>
                ))
              ) : (
                <div className="p-4 bg-gray-700 rounded-lg">
                  No majors found for this school
                </div>
              )
            ) : (
              <div className="p-4 bg-gray-700 rounded-lg">
                Select a school to view majors
              </div>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default SchoolMajorSelector;