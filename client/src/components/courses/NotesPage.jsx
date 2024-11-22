import { useState, useEffect } from 'react';
import { useParams, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

const NotesPage = () => {
 const { majorCode, courseId } = useParams();
 const navigate = useNavigate();
 const { state } = useLocation();
 const courseData = state?.courseData;
 const semesterData = state?.semesterData;
 const { isLoggedIn } = useAuth();
 
 const [notes, setNotes] = useState([]);
 const [loading, setLoading] = useState(true);
 const [error, setError] = useState(null);

 useEffect(() => {
   const fetchNotes = async () => {
     if (!courseData || !semesterData) {
       setError('Missing course or semester data');
       setLoading(false);
       return;
     }

     try {
       const notesResponse = await fetch(`/api/note?courseID=${courseId}&semesterID=${semesterData.id}&return=object`);
       const notesData = await notesResponse.json();

       if (!notesData.good) {
         throw new Error('Failed to fetch notes');
       }

       setNotes(notesData.data);
     } catch (err) {
       console.error('Error fetching notes:', err);
       setError(err.message);
     } finally {
       setLoading(false);
     }
   };

   fetchNotes();
 }, [courseId, semesterData, courseData]);

 if (!courseData || !semesterData) {
   return (
     <div className="text-center p-8">
       <p className="text-red-500">Course or semester data not found. Please navigate from the course page.</p>
       <button
         onClick={() => navigate(`/${majorCode}`)}
         className="mt-4 text-teal-400 hover:text-teal-300 transition-colors"
       >
         Return to courses
       </button>
     </div>
   );
 }

 if (loading) {
   return (
     <div className="flex justify-center items-center h-64">
       <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
     </div>
   );
 }

 if (error) {
   return (
     <div className="text-red-500 text-center p-4">
       {error}
       <button
         onClick={() => navigate(`/${majorCode}`)}
         className="block mx-auto mt-4 text-teal-400 hover:text-teal-300 transition-colors"
       >
         Return to courses
       </button>
     </div>
   );
 }

 return (
   <div className="px-4 sm:px-24 mx-auto">
     <div className="bg-teal-500 p-4 rounded-t-lg">
       <div className="flex items-center justify-between">
         <div className="flex items-center">
           <button 
             onClick={() => navigate(`/${majorCode}/${courseId}`)}
             className="mr-4 text-white hover:text-gray-200 transition-colors"
           >
             ← Back
           </button>
           <div>
             <h1 className="text-xl text-white font-bold">
               {courseData.code}: {courseData.name}
             </h1>
             <h2 className="text-white capitalize">
               {semesterData.season} {semesterData.year}
             </h2>
           </div>
         </div>
         {isLoggedIn && (
           <button
             onClick={() => navigate('upload')}
             className="bg-white text-teal-500 px-4 py-2 rounded hover:bg-gray-100 transition-colors"
           >
             Upload Note
           </button>
         )}
       </div>
     </div>

     {notes.length === 0 ? (
       <div className="text-center p-8 bg-gray-800 mt-4 rounded">
         <p className="text-gray-400">No notes available for this semester yet.</p>
         {isLoggedIn && (
           <p className="text-gray-400 mt-2">Be the first to upload notes!</p>
         )}
       </div>
     ) : (
       <div className="grid gap-4 mt-4">
         {notes.map((note) => (
           <div 
             key={note.id}
             className="bg-gray-800 p-6 rounded-lg hover:bg-gray-700 transition-colors"
           >
             <h3 className="text-lg font-semibold mb-2">{note.title}</h3>
             {note.description && (
               <p className="text-gray-400 mb-2">{note.description}</p>
             )}
             {note.owner && (
               <p className="text-gray-400 text-sm">
                 Uploaded by {note.owner.username}
               </p>
             )}
             <div className="mt-4 flex gap-4">
               <button
                 onClick={() => window.location.href = `/api/note/${note.id}?download=true`}
                 className="text-teal-400 hover:text-teal-300 transition-colors"
               >
                 Download
               </button>
               {isLoggedIn && (
                 <>
                   <button
                     onClick={async () => {
                       try {
                         const response = await fetch(`/api/note/${note.id}/like`, {
                           method: 'PUT'
                         });
                         if (!response.ok) throw new Error('Failed to like note');
                         // Optionally refresh notes here or update UI
                       } catch (err) {
                         console.error('Error liking note:', err);
                       }
                     }}
                     className="text-gray-400 hover:text-teal-300 transition-colors"
                   >
                     Like
                   </button>
                   <span className="text-gray-400">
                     {note.totalLikes || 0} likes
                   </span>
                 </>
               )}
             </div>
           </div>
         ))}
       </div>
     )}
   </div>
 );
};

export default NotesPage;