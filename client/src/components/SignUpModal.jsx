import { useState } from "react";
import Modal from "./Modal";

const SignUpModal = () => {
    const [isOpen, setIsOpen] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [values, setValues] = useState({
        email: "",
        password: "",
        confirmPassword: "",
        username: "",
    });
    const [errors, setErrors] = useState({});

    const validateForm = () => {
        const newErrors = {};
        if (!values.username) newErrors.username = "Username is required";
        if (!values.email) newErrors.email = "Email is required";
        else if (!/\S+@\S+\.\S+/.test(values.email))
            newErrors.email = "Email is invalid";
        if (!values.password) newErrors.password = "Password is required";
        if (!values.confirmPassword)
            newErrors.confirmPassword = "Please confirm your password";
        else if (values.password !== values.confirmPassword)
            newErrors.confirmPassword = "Passwords do not match";
        return newErrors;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setValues((prev) => ({ ...prev, [name]: value }));
        if (errors[name]) {
            setErrors((prev) => ({ ...prev, [name]: "" }));
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        const formErrors = validateForm();

        if (Object.keys(formErrors).length === 0) {
            setIsLoading(true);
            try {
                const formData = new URLSearchParams();
                formData.append("email", values.email);
                formData.append("username", values.username);
                formData.append("password", values.password);

                const response = await fetch("/api/account/force", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/x-www-form-urlencoded",
                    },
                    body: formData.toString(),
                });

                const data = await response.json();
                if (!data.good) {
                    throw new Error(data.error || "Registration failed");
                }

                // Success - close modal and reset form
                setIsOpen(false);
                setValues({
                    email: "",
                    password: "",
                    confirmPassword: "",
                    username: "",
                });
                setErrors({});
            } catch (error) {
                setErrors({ submit: error.message });
            } finally {
                setIsLoading(false);
            }
        } else {
            setErrors(formErrors);
        }
    };

    const handleClose = () => {
        if (!isLoading) {
            setIsOpen(false);
            setValues({
                email: "",
                password: "",
                confirmPassword: "",
                username: "",
            });
            setErrors({});
        }
    };

    return (
        <>
            <button
                onClick={() => setIsOpen(true)}
                className="hidden sm:block text-white hover:text-teal-300 transition-colors"
            >
                Sign up
            </button>

            <Modal isOpen={isOpen} onClose={handleClose} title="Create an account">
                <form onSubmit={handleSubmit} className="space-y-4">
                    <div>
                        <label htmlFor="username" className="block text-white mb-2">
                            Username
                        </label>
                        <input
                            id="username"
                            name="username"
                            type="text"
                            value={values.username}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.username ? "border-red-500" : "border-gray-600"
                                } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                            disabled={isLoading}
                        />
                        {errors.username && (
                            <p className="text-red-500 text-sm mt-1">{errors.username}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="email" className="block text-white mb-2">
                            Email
                        </label>
                        <input
                            id="email"
                            name="email"
                            type="email"
                            value={values.email}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.email ? "border-red-500" : "border-gray-600"
                                } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                            disabled={isLoading}
                        />
                        {errors.email && (
                            <p className="text-red-500 text-sm mt-1">{errors.email}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="password" className="block text-white mb-2">
                            Password
                        </label>
                        <input
                            id="password"
                            name="password"
                            type="password"
                            value={values.password}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.password ? "border-red-500" : "border-gray-600"
                                } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                            disabled={isLoading}
                        />
                        {errors.password && (
                            <p className="text-red-500 text-sm mt-1">{errors.password}</p>
                        )}
                    </div>

                    <div>
                        <label htmlFor="confirmPassword" className="block text-white mb-2">
                            Confirm Password
                        </label>
                        <input
                            id="confirmPassword"
                            name="confirmPassword"
                            type="password"
                            value={values.confirmPassword}
                            onChange={handleChange}
                            className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${errors.confirmPassword ? "border-red-500" : "border-gray-600"
                                } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
                            disabled={isLoading}
                        />
                        {errors.confirmPassword && (
                            <p className="text-red-500 text-sm mt-1">
                                {errors.confirmPassword}
                            </p>
                        )}
                    </div>

                    {errors.submit && (
                        <p className="text-red-500 text-sm">{errors.submit}</p>
                    )}

                    <button
                        type="submit"
                        disabled={isLoading}
                        className="w-full bg-teal-500 text-white py-2 px-4 rounded-lg hover:bg-teal-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
                    >
                        {isLoading ? (
                            <>
                                <svg
                                    className="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                                    xmlns="http://www.w3.org/2000/svg"
                                    fill="none"
                                    viewBox="0 0 24 24"
                                >
                                    <circle
                                        className="opacity-25"
                                        cx="12"
                                        cy="12"
                                        r="10"
                                        stroke="currentColor"
                                        strokeWidth="4"
                                    ></circle>
                                    <path
                                        className="opacity-75"
                                        fill="currentColor"
                                        d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4z"
                                    ></path>
                                </svg>
                                Creating account...
                            </>
                        ) : (
                            "Create Account"
                        )}
                    </button>
                </form>
            </Modal>
        </>
    );
};

export default SignUpModal;
