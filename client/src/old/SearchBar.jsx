import React from 'react';

const SearchBar = () => {
    return (
        <input
            type="text"
            placeholder="Search Courses"
            className="hidden sm:block px-4 py-2 bg-gray-700 rounded-lg text-white"
        />
    );
};

export default SearchBar;
