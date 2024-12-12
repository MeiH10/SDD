import { useState, useEffect } from "react";

const ApiTest = () => {
    const [data, setData] = useState(null);
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
                const response = await fetch("/api/note");
                if (!response.ok) {
                    throw new Error(`HTTP error! status: ${response.status}`);
                }
                const result = await response.json();
                setData(result);
            } catch (error) {
                setError(error.message);
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, []);

    if (loading) return <div className="text-white">Loading...</div>;
    if (error) return <div className="text-red-500">Error: {error}</div>;
    if (!data) return <div className="text-white">No data found</div>;

    return (
        <div className="text-white">
            <h2 className="text-xl font-bold mb-4">API Test Results:</h2>
            <pre className="bg-gray-800 p-4 rounded">
                {JSON.stringify(data, null, 2)}
            </pre>
        </div>
    );
};

export default ApiTest;
