
import React, { useState } from 'react';

const SchoolMajorSelector = () => {
    const [selectedSchool, setSelectedSchool] = useState("Architecture");

    const schools = [
        "Architecture",
        "Engineering",
        "Humanities, Arts, and Social Sciences",
        "Information Technology and Web Science",
        "Interdisciplinary and Other",
        "Management"
    ];

    const schoolMajors = {
        Architecture: [
            "ARCH Architecture",
            "LGHT Lighting"
        ],
        Engineering: [
            "BMED Biomedical Engineering",
            "CHME Chemical Engineering",
            "CIVL Civil Engineering",
            "ECSE Electrical, Computer, and Systems Engineering",
            "ENGR General Engineering",
            "ENVE Environmental Engineering",
            "ESCI Engineering Science",
            "ISYE Industrial and Systems Engineering",
            "MANE Mechanical, Aerospace, and Nuclear Engineering",
            "MTLE Materials Science and Engineering"
        ],
        "Humanities, Arts, and Social Sciences": [
            "ARTS Arts",
            "COGS Cognitive Science",
            "COMM Communication",
            "ECON Economics",
            "GSAS Games and Simulation Arts and Sciences",
            "IHSS Interdisciplinary Humanities and Social Sciences",
            "INQR HASS Inquiry",
            "LANG Foreign Languages",
            "LITR Literature",
            "PHIL Philosophy",
            "PSYC Psychology",
            "STSO Science, Technology, and Society",
            "WRIT Writing"
        ],
        Science: [
            "ASTR Astronomy",
            "BCBP Biochemistry and Biophysics",
            "BIOL Biology",
            "CHEM Chemistry",
            "CSCI Computer Science",
            "ERTH Earth and Environmental Science",
            "ISCI Interdisciplinary Science",
            "MATH Mathematics",
            "MATP Mathematical Programming, Probability, and Statistics",
            "PHYS Physics"
        ],
        "Interdisciplinary and Other": [
            "ADMN Administrative Courses",
            "USAF Aerospace Studies (Air Force ROTC)",
            "USAR Military Science (Army ROTC)",
            "USNA Naval Science (Navy ROTC)"
        ],
        Management: [
            "BUSN Business",
            "MGMT Management"
        ],
        "Information Technology and Web Science": [
            "ITWS Information Technology and Web Science"
        ]
    };
    

    const handleSchoolClick = (school) => {
        setSelectedSchool(school);
    };

    return (
        <div className="px-4 sm:px-24 mx-auto pt-10">
            <div className="flex">
                {/* schools*/}
                <div className="w-1/3 p-4 bg-gray-800 border-r border-gray-600">
                    <h2 className="text-lg font-bold text-teal-400 mb-4">School</h2>
                    <ul className="space-y-2">
                        {schools.map((school) => (
                            <li
                                key={school}
                                className={`p-2 ${
                                    selectedSchool === school ? 'bg-gray-600' : 'bg-gray-700'
                                } rounded-lg hover:bg-gray-600 cursor-pointer transition-colors duration-200`}
                                onClick={() => handleSchoolClick(school)}
                            >
                                {school}
                            </li>
                        ))}
                    </ul>
                </div>

                {/* majors*/}
                <div className="w-2/3 p-4 bg-gray-800">
                    <h2 className="text-lg font-bold text-teal-400 mb-4">Major</h2>
                    <div className="grid gap-4">
                        {schoolMajors[selectedSchool]?.map((major) => (
                            <div
                                key={major}
                                className="p-4 bg-gray-700 rounded-lg hover:bg-gray-600 cursor-pointer transition-colors duration-200"
                            >
                                {major}
                            </div>
                        )) || (
                            <div className="p-4 bg-gray-700 rounded-lg">
                                No majors found for this school
                            </div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
};

export default SchoolMajorSelector;