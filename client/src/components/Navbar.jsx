import React from "react";
import SearchBar from "./SearchBar";
import pucknotesLogo from "../assets/pucknotesLogo.svg";
import LoginModal from "./LoginModal";
import SignUpModal from "./SignUpModal";
import UploadModal from "./UploadModal";
import { useAuth } from "../context/AuthContext";
import { useNavigate, Link } from "react-router-dom";

const Navbar = () => {
  const { isLoggedIn, logout, userRole } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
  };

  const handleLogoClick = () => {
    navigate("/");
  };

  return (
    <div className="flex justify-between items-center p-4 px-4 sm:px-24 bg-gray-800 w-full">
      <div className="flex items-center space-x-4">
        <button
          onClick={handleLogoClick}
          className="flex items-center justify-center group"
        >
          <img
            src={pucknotesLogo}
            alt="Pucknotes Logo"
            className="w-10 h-10 sm:w-12 sm:h-12 
                     opacity-100 
                     group-hover:opacity-80 
                     transition-opacity duration-200
                     group-hover:scale-110"
          />
        </button>
        <SearchBar />
      </div>

      <div className="flex items-center space-x-4">
        <UploadModal />
        {isLoggedIn ? (
          <>
            <button
              onClick={handleLogout}
              className="hidden sm:block bg-red-500 px-4 py-2 rounded-lg text-white hover:bg-red-400 transition-colors"
            >
              Log out
            </button>
            {userRole === 3 && (
              <Link
                to="/admin/reports"
                className="px-4 py-2 rounded-lg text-sm font-medium text-white bg-gradient-to-r from-pink-500 to-orange-500 hover:from-pink-600 hover:to-orange-600 transition-all duration-300"
              >
                Reports
              </Link>
            )}
          </>
        ) : (
          <>
            <LoginModal />
            <SignUpModal />
          </>
        )}

        <button className="block sm:hidden text-white hover:text-teal-300 transition-colors">
          <svg
            xmlns="http://www.w3.org/2000/svg"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
            className="w-6 h-6"
          >
            <path
              strokeLinecap="round"
              strokeLinejoin="round"
              strokeWidth="2"
              d="M4 6h16M4 12h16M4 18h16"
            />
          </svg>
        </button>
      </div>
    </div>
  );
};

export default Navbar;
