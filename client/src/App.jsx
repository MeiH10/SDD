import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import Navbar from "./components/Navbar";
import SchoolMajorSelector from "./components/SchoolMajorSelector";
import CoursesPage from "./components/courses/CoursesPage";
import CourseSemestersPage from "./components/courses/CourseSemestersPage";
import NotesPage from "./components/courses/NotesPage";
import { NoteProvider } from "./context/NoteContext";
import AdminReportsPage from "./components/admin/AdminReportsPage";
import SearchResults from './components/SearchResults';


const App = () => {
  return (
    <AuthProvider>
      <NoteProvider>
        <BrowserRouter>
          <div className="min-h-screen bg-gray-900 text-white">
            <Navbar />
            <Routes>
              <Route path="/" element={<SchoolMajorSelector />} />
              <Route path="/:majorCode" element={<CoursesPage />} />
              <Route
                path="/:majorCode/:courseId"
                element={<CourseSemestersPage />}
              />
              <Route
                path="/:majorCode/:courseId/:semester"
                element={<NotesPage />}
              />
              <Route path="/admin/reports" element={<AdminReportsPage />} />
              <Route path="/search" element={<SearchResults />} />
            </Routes>
          </div>
        </BrowserRouter>
      </NoteProvider>
    </AuthProvider>
  );
};

export default App;
