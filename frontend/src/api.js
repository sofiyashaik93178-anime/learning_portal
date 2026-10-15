
import axios from "axios";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  timeout: 30000,
  headers: {
    "Content-Type": "application/json",
  },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("lh_token");

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

export function getApiError(
  error,
  fallback = "The request could not be completed."
) {
  if (error?.response?.data?.message) {
    return error.response.data.message;
  }

  if (error?.response?.status === 403) {
    return "Access forbidden (403). Check the backend security configuration and Render logs.";
  }

  if (error?.response?.status === 401) {
    return "Unauthorized (401). Please log in again.";
  }

  if (error?.code === "ECONNABORTED") {
    return "The server took too long to respond. Please try again.";
  }

  if (!error?.response) {
    return "Cannot reach the Learn Hub backend. Check that the backend is running.";
  }

  return fallback;
}

export default api;
