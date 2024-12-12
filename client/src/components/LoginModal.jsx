import { useState } from "react";
import Modal from "./Modal";
import { useAuth } from "../context/AuthContext";

const LoginModal = () => {
  const { login } = useAuth();

  const [isOpen, setIsOpen] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const [values, setValues] = useState({ email: "", password: "" });
  const [errors, setErrors] = useState({});

  // validate form fields before submitting
  const validateForm = () => {
    const newErrors = {};
    if (!values.email) newErrors.email = "Email is required";
    if (!values.password) newErrors.password = "Password is required";
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
        formData.append("password", values.password);

        // send login request
        const response = await fetch("/api/session", {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded",
          },
          body: formData.toString(),
        });

        const data = await response.json();
        if (!data.good) {
          throw new Error(data.error || "Login failed");
        }

        // update auth context and reset form
        login(data.data);
        setIsOpen(false);
        setValues({ email: "", password: "" });
        setErrors({});
      } catch (error) {
        setErrors({ submit: "Invalid email or password" });
      } finally {
        setIsLoading(false);
      }
    } else {
      setErrors(formErrors);
    }
  };

  // handle modal close and form reset
  const handleClose = () => {
    if (!isLoading) {
      setIsOpen(false);
      setValues({ email: "", password: "" });
      setErrors({});
    }
  };

  return (
    <>
      {/* login button */}
      <button
        onClick={() => setIsOpen(true)}
        className="hidden sm:block text-white hover:text-teal-300 transition-colors"
      >
        Log in
      </button>

      {/* login modal */}
      <Modal
        isOpen={isOpen}
        onClose={handleClose}
        title="Login to your account"
      >
        <form onSubmit={handleSubmit} className="space-y-4">
          {/* email input field */}
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
              className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${
                errors.email ? "border-red-500" : "border-gray-600"
              } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
              disabled={isLoading}
            />
            {errors.email && (
              <p className="text-red-500 text-sm mt-1">{errors.email}</p>
            )}
          </div>

          {/* password input field */}
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
              className={`w-full px-3 py-2 bg-gray-700 text-white rounded-lg border ${
                errors.password ? "border-red-500" : "border-gray-600"
              } focus:border-teal-500 focus:ring-1 focus:ring-teal-500 outline-none`}
              disabled={isLoading}
            />
            {errors.password && (
              <p className="text-red-500 text-sm mt-1">{errors.password}</p>
            )}
          </div>

          {/* error message */}
          {errors.submit && (
            <p className="text-red-500 text-sm">{errors.submit}</p>
          )}

          {/* loading state */}
          <button
            type="submit"
            disabled={isLoading}
            className="w-full bg-teal-500 text-white py-2 px-4 rounded-lg hover:bg-teal-400 transition-colors disabled:opacity-50 disabled:cursor-not-allowed flex items-center justify-center"
          >
            {isLoading ? (
              <>
                {/* loading skelton */}
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
                Logging in...
              </>
            ) : (
              "Login"
            )}
          </button>
        </form>
      </Modal>
    </>
  );
};

export default LoginModal;