import axios from "axios";

// Requests use Vite's /api proxy in development. Set VITE_API_BASE_URL only
// when calling a separately hosted backend directly.
const api = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || "/api",
  timeout: 15000,
  headers: { "Content-Type": "application/json" },
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem("lh_token");
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

export function getApiError(error, fallback = "The request could not be completed.") {
  if (error?.response?.data?.message) return error.response.data.message;
  if (error?.code === "ECONNABORTED") return "The server took too long to respond. Check that the backend is running.";
  if (!error?.response) return "Cannot reach the Learn Hub backend. Start the backend and check the API proxy.";
  return fallback;
}

export default api;
