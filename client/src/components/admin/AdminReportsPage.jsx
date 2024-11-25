import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";

const AdminReportsPage = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();

  useEffect(() => {
    const fetchReports = async () => {
      if (!isLoggedIn) {
        navigate("/");
        return;
      }

      try {
        const response = await fetch("/api/reports");
        const data = await response.json();

        if (!data.good) {
          throw new Error(data.error || "Failed to fetch reports");
        }

        setReports(data.data);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchReports();
  }, [isLoggedIn, navigate]);

  const handleDismiss = async (reportId) => {
    try {
      const response = await fetch(`/api/reports/${reportId}`, {
        method: "DELETE",
      });

      const data = await response.json();
      if (!data.good) {
        throw new Error(data.error || "Failed to dismiss report");
      }

      setReports(reports.filter((report) => report.id !== reportId));
    } catch (err) {
      setError(err.message);
    }
  };

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
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <h1 className="text-2xl font-bold text-white mb-6">Report Management</h1>

      <div className="space-y-4">
        {reports.length === 0 ? (
          <div className="text-center text-gray-400 py-8">
            No reports to display
          </div>
        ) : (
          reports.map((report) => (
            <div
              key={report.id}
              className="bg-gray-800 rounded-lg p-6 border border-gray-700"
            >
              <div className="flex justify-between items-start">
                <div>
                  <h3 className="text-lg font-medium text-white">
                    {report.title}
                  </h3>
                  <p className="text-sm text-gray-400 mt-1">
                    Reported by: {report.owner}
                  </p>
                  <p className="text-gray-300 mt-2">{report.description}</p>
                </div>
                <button
                  onClick={() => handleDismiss(report.id)}
                  className="px-4 py-2 bg-red-500 text-white rounded-lg hover:bg-red-400 transition-colors"
                >
                  Dismiss
                </button>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AdminReportsPage;
