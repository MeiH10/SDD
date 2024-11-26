import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userId, setUserId] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch("/api/session");
        const data = await response.json();
        if (data.good) {
          setIsLoggedIn(true);
          setUserId(data.data);

          const userResponse = await fetch(`/api/account/${data.data}`);
          const userData = await userResponse.json();
          if (userData.good) {
            setUserRole(userData.data.role);
          }
        }
      } catch (error) {
        console.error("Session check failed:", error);
      } finally {
        setIsLoading(false);
      }
    };

    checkSession();
  }, []);

  const login = async (id) => {
    setIsLoggedIn(true);
    setUserId(id);

    try {
      const response = await fetch(`/api/account/${id}`);
      const data = await response.json();
      if (data.good) {
        setUserRole(data.data.role);
      }
    } catch (error) {
      console.error("Failed to fetch user role:", error);
    }
  };

  const logout = async () => {
    try {
      await fetch("/api/session", {
        method: "DELETE",
      });
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      setIsLoggedIn(false);
      setUserId(null);
      setUserRole(null);
    }
  };

  if (isLoading) {
    return (
      <div className="h-screen w-screen flex items-center justify-center bg-gray-900">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  return (
    <AuthContext.Provider
      value={{ isLoggedIn, userId, userRole, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
