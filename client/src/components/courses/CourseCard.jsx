const CourseCard = ({ course }) => {
    return (
      <div className="bg-gray-800 rounded-lg p-6 hover:bg-gray-700 transition-colors cursor-pointer">
        <div className="flex items-start justify-between mb-2">
          <h2 className="text-xl font-bold text-white">
            {course.code}: {course.name}
          </h2>
        </div>
      </div>
    );
  };
  
  export default CourseCard;