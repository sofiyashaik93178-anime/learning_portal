import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api, { getApiError } from "../api";
import { useAuth } from "../AuthContext";

export default function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const { loginSuccess } = useAuth();
  const navigate = useNavigate();

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    try {
      const { data } = await api.post("/auth/login", { email: email.trim().toLowerCase(), password });
      loginSuccess(data);
      const role = data.user.role;
      navigate(role === "ADMIN" ? "/admin" : role === "INSTRUCTOR" ? "/instructor" : "/dashboard");
    } catch (err) {
      setError(getApiError(err, "Could not log in. Check your email and password."));
    }
  }

  return (
    <div className="max-w-md mx-auto px-5 py-16">
      <h1 className="font-display text-4xl text-pine">Welcome back</h1>
      <p className="text-leaf mt-2">Your learning garden is waiting.</p>
      <form onSubmit={onSubmit} className="mt-8 space-y-4 bg-white/70 p-6 rounded-3xl border border-moss/10">
        {error && <p className="text-red-700 text-sm">{error}</p>}
        <input type="email" autoComplete="email" required className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} />
        <input type="password" autoComplete="current-password" required className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} />
        <button type="submit" className="w-full bg-pine text-cream py-3 rounded-full">Log in</button>
      </form>
      <p className="text-sm text-leaf mt-4">New here? <Link to="/register" className="text-pine underline">Create an account</Link></p>
    </div>
  );
}
