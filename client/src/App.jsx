import { BrowserRouter, Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import Navbar from './components/Navbar';
import SchoolMajorSelector from './components/SchoolMajorSelector';
import CoursesPage from './components/courses/CoursesPage';
import CourseSectionsPage from './components/courses/CourseSectionsPage';
import NotesPage from './components/notes/NotesPage';
import CourseSemestersPage from './components/courses/CourseSemestersPage';

const App = () => {
  return (
    <AuthProvider>
      <BrowserRouter>
        <div className="min-h-screen bg-gray-900 text-white">
          <Navbar />
          <Routes>
            <Route path="/" element={<SchoolMajorSelector />} />
            <Route path="/:majorCode" element={<CoursesPage />} />
            <Route path="/:majorCode/:courseId" element={<CourseSemestersPage />} />
          </Routes>
        </div>
      </BrowserRouter>
    </AuthProvider>
  );
};

export default App;