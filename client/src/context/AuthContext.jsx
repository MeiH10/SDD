import { createContext, useContext, useState, useEffect } from 'react';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(false);
  const [userId, setUserId] = useState(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const checkSession = async () => {
      try {
        const response = await fetch('/api/session');
        const data = await response.json();
        if (data.good) {
          setIsLoggedIn(true);
          setUserId(data.data);
        }
      } catch (error) {
        console.error('Session check failed:', error);
      } finally {
        setIsLoading(false);
      }
    };

    checkSession();
  }, []);

  const login = (id) => {
    setIsLoggedIn(true);
    setUserId(id);
  };

  const logout = async () => {
    try {
      await fetch('/api/session', {
        method: 'DELETE'
      });
    } catch (error) {
      console.error('Logout failed:', error);
    } finally {
      setIsLoggedIn(false);
      setUserId(null);
    }
  };

  if (isLoading) {
    return <div className="h-screen w-screen flex items-center justify-center bg-gray-900">
      <div className="animate-spin h-8 w-8 border-4 border-teal-500 rounded-full border-t-transparent"></div>
    </div>;
  }

  return (
    <AuthContext.Provider value={{ isLoggedIn, userId, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);