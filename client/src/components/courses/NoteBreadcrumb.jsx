import { useNavigate } from 'react-router-dom';

const NoteBreadcrumb = ({ majorCode, courseData, semesterData }) => {
  const navigate = useNavigate();
  
  return (
    <nav className="text-gray-300 text-lg">
      <span 
        className="hover:text-white cursor-pointer" 
        onClick={() => navigate('/')}
      >
        {majorCode}
      </span>
      <span className="mx-2">{'>'}</span>
      <span 
        className="hover:text-white cursor-pointer" 
        onClick={() => navigate(`/${majorCode}`)}
      >
        {courseData.code} {courseData.name}
      </span>
      {semesterData && (
        <>
          <span className="mx-2">{'>'}</span>
          <span 
            className="hover:text-white cursor-pointer"
            onClick={() => navigate(`/${majorCode}/${courseData.id}`, {
              state: { courseData }
            })}
          >
            {semesterData.season} {semesterData.year}
          </span>
        </>
      )}
    </nav>
  );
};

export default NoteBreadcrumb;