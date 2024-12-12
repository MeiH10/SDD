/** @type {import('tailwindcss').Config} */
export default {
    content: ["./index.html", "./src/**/*.{js,ts,jsx,tsx}"],
    theme: {
        theme: {
            extend: {
                animation: {
                    gradient: "gradient 3s ease infinite",
                },
                keyframes: {
                    gradient: {
                        "0%, 100%": {
                            "background-size": "200% 200%",
                            "background-position": "left center",
                        },
                        "50%": {
                            "background-size": "200% 200%",
                            "background-position": "right center",
                        },
                    },
                },
            },
        },
    },
    plugins: [],
};
