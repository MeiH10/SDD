import { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useAuth } from "../../context/AuthContext";
import { Trash2, XCircle, Ban } from "lucide-react";

const AdminReportsPage = () => {
  const [reports, setReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();
  const { isLoggedIn } = useAuth();

  // redirect to home if user is not logged in
  useEffect(() => {
    if (!isLoggedIn) {
      navigate("/");
      return;
    }

    fetchReports();
  }, [isLoggedIn, navigate]);

  // fetch all reports and populate them with user details
  const fetchReports = async () => {
    setLoading(true);
    try {
      const response = await fetch("/api/reports");
      const data = await response.json();

      if (!data.good) {
        throw new Error(data.error || "Failed to fetch reports");
      }

      // fill each report with reporter and content owner details
      const reportsWithDetails = await Promise.all(
        data.data.map(async (report) => {
          const ownerResponse = await fetch(`/api/account/${report.owner}`);
          const ownerData = await ownerResponse.json();

          // fetch owner details if it exists
          let contentOwner = null;
          if (report.item) {
            const itemResponse = await fetch(
              `/api/${report.type}/${report.item}`,
            );
            const itemData = await itemResponse.json();
            if (itemData.good) {
              // get the account details of the note owner
              const ownerResponse = await fetch(
                `/api/account/${itemData.data.account || itemData.data.owner}`,
              );
              const ownerData = await ownerResponse.json();
              if (ownerData.good) {
                contentOwner = ownerData.data;
              }
            }
          }

          return {
            ...report,
            reporterName: ownerData.good ? ownerData.data.username : "Unknown",
            contentOwner,
          };
        }),
      );

      setReports(reportsWithDetails);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  // delete a report from the system after it's resolved
  const handleDismissReport = async (reportId) => {
    if (!window.confirm("Are you sure you want to dismiss this report?"))
      return;

    try {
      const response = await fetch(`/api/reports/${reportId}`, {
        method: "DELETE",
      });

      const data = await response.json();
      if (!data.good) {
        throw new Error(data.error || "Failed to dismiss report");
      }

      // update states to remove the dismissed report
      setReports(reports.filter((report) => report.id !== reportId));
    } catch (err) {
      setError(err.message);
    }
  };

  // delete reported comment/note and dismiss the report associated with it
  const handleDeleteContent = async (report) => {
    if (!window.confirm(`Are you sure you want to delete this ${report.type}?`))
      return;

    try {
      const response = await fetch(`/api/${report.type}/${report.item}`, {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error(`Failed to delete ${report.type}`);
      }

      // After successfully deleting the content, also dismiss the report
      await handleDismissReport(report.id);
    } catch (err) {
      setError(err.message);
    }
  };

  // ban a user by setting their role to 0
  const handleBanUser = async (userId, username) => {
    if (!window.confirm(`Are you sure you want to ban user "${username}"?`))
      return;

    try {
      const formData = new URLSearchParams();
      formData.append("role", "0");

      const response = await fetch(`/api/account/${userId}`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: formData,
      });

      if (!response.ok) {
        throw new Error("Failed to ban user");
      }

      // refresh the reports
      await fetchReports();
    } catch (err) {
      setError(err.message);
    }
  };

  // show loading skelton while fetching data
  if (loading) {
    return (
      <div className="flex justify-center items-center h-64">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  // display error message if something goes wrong
  if (error) {
    return <div className="text-red-500 text-center p-4">{error}</div>;
  }

  return (
    <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-2xl font-bold text-white">Report Management</h1>
        <span className="text-gray-400">
          {reports.length} active report{reports.length !== 1 ? "s" : ""}
        </span>
      </div>

      {/* reports list section */}
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
                <div className="flex-1">
                  {/* report header with title and type */}
                  <div className="flex items-center gap-3 mb-2">
                    <h3 className="text-lg font-medium text-white">
                      {report.title}
                    </h3>
                    <span className="px-2 py-1 bg-gray-700 rounded-full text-xs font-medium text-gray-300">
                      {report.type}
                    </span>
                  </div>

                  {/* reporter info and timestamp */}
                  <p className="text-sm text-gray-400 mb-2">
                    Reported by:{" "}
                    <span className="text-teal-400">{report.reporterName}</span>{" "}
                    • {new Date(report.createdDate).toLocaleDateString()}
                  </p>

                  <p className="text-gray-300 mb-4">{report.description}</p>

                  {/* note/comment owner section with action buttons */}
                  {report.contentOwner && (
                    <div className="mt-4 p-4 bg-gray-700/50 rounded-lg">
                      <div className="flex items-center justify-between">
                        <div>
                          <span className="text-sm text-gray-400">
                            Content owner:{" "}
                          </span>
                          <span className="text-sm text-teal-400">
                            {report.contentOwner.username}
                          </span>
                        </div>
                        <div className="flex items-center gap-2">
                          {/* admin action buttons */}
                          <button
                            onClick={() =>
                              handleBanUser(
                                report.contentOwner.id,
                                report.contentOwner.username,
                              )
                            }
                            className="p-2 text-gray-400 hover:text-yellow-500 transition-colors rounded-lg hover:bg-gray-600/50"
                            title="Ban user"
                          >
                            <Ban className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDeleteContent(report)}
                            className="p-2 text-gray-400 hover:text-red-500 transition-colors rounded-lg hover:bg-gray-600/50"
                            title={`Delete ${report.type}`}
                          >
                            <XCircle className="w-4 h-4" />
                          </button>
                          <button
                            onClick={() => handleDismissReport(report.id)}
                            className="p-2 text-gray-400 hover:text-red-500 transition-colors rounded-lg hover:bg-gray-600/50"
                            title="Dismiss report"
                          >
                            <Trash2 className="w-4 h-4" />
                          </button>
                        </div>
                      </div>
                    </div>
                  )}
                </div>
              </div>
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default AdminReportsPage;
