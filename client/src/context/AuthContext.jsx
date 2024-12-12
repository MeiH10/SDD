import { createContext, useContext, useState, useEffect } from "react";

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userId, setUserId] = useState(null);
  const [userRole, setUserRole] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  // check user session
  useEffect(() => {
    const checkSession = async () => {
      try {
        // verify existing session
        const response = await fetch("/api/session");
        const data = await response.json();
        if (data.good && data.data) {
          // set auth state if session exists
          setIsLoggedIn(true);
          setUserId(data.data);

          // fetch user role information
          const userResponse = await fetch(`/api/account/${data.data}`);
          const userData = await userResponse.json();
          if (userData.good) {
            setUserRole(userData.data.role);
          }
        } else {
          setIsLoggedIn(false);
          setUserId(null);
          setUserRole(null);
        }
      } catch (error) {
        console.error("Session check failed:", error);
        // clear auth state on error
        setIsLoggedIn(false);
        setUserId(null);
        setUserRole(null);
      } finally {
        setIsLoading(false);
      }
    };

    checkSession();
  }, []);

  const login = async (id) => {
    try {
      // set initial auth state
      setIsLoggedIn(true);
      setUserId(id);

      // fetch user role
      const response = await fetch(`/api/account/${id}`);
      const data = await response.json();
      if (data.good) {
        setUserRole(data.data.role);
      }
    } catch (error) {
      console.error("Failed to fetch user role:", error);
      // clear auth state on error
      setIsLoggedIn(false);
      setUserId(null);
      setUserRole(null);
    }
  };

  // handle user logout and session cleanup
  const logout = async () => {
    try {
      // end server session
      const response = await fetch("/api/session", {
        method: "DELETE",
      });

      if (!response.ok) {
        throw new Error("Logout failed");
      }
    } catch (error) {
      console.error("Logout failed:", error);
    } finally {
      // clear auth state regardless of logout success
      setIsLoggedIn(false);
      setUserId(null);
      setUserRole(null);
    }
  };

  // display loading skelton while checking session
  if (isLoading) {
    return (
      <div className="h-screen w-screen flex items-center justify-center bg-gray-900">
        <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
      </div>
    );
  }

  return (
    <AuthContext.Provider
      value={{
        isLoggedIn,
        userId,
        userRole,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};
