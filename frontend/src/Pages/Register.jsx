
import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api, { getApiError } from "../api";
import { useAuth } from "../AuthContext";

export default function Register() {
  const [form, setForm] = useState({
    name: "",
    email: "",
    password: "",
    role: "STUDENT",
  });

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const { loginSuccess } = useAuth();
  const navigate = useNavigate();

  async function onSubmit(e) {
    e.preventDefault();
    setError("");
    setLoading(true);

    try {
      const response = await api.post("/auth/register", {
        name: form.name.trim(),
        email: form.email.trim().toLowerCase(),
        password: form.password,
        role: form.role,
      });

      loginSuccess(response.data);

      navigate(
        form.role === "INSTRUCTOR" ? "/instructor" : "/dashboard"
      );
    } catch (err) {
      setError(
        getApiError(err, "Could not register. Please check your details.")
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="max-w-md mx-auto px-5 py-16">
      <h1 className="font-display text-4xl text-pine">
        Plant your account
      </h1>

      <form
        onSubmit={onSubmit}
        className="mt-8 space-y-4 bg-white/70 p-6 rounded-3xl border border-moss/10"
      >
        {error && (
          <p role="alert" className="text-red-700 text-sm">
            {error}
          </p>
        )}

        <input
          required
          minLength={2}
          autoComplete="name"
          className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream"
          placeholder="Full name"
          value={form.name}
          onChange={(e) =>
            setForm({ ...form, name: e.target.value })
          }
        />

        <input
          type="email"
          required
          autoComplete="email"
          className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream"
          placeholder="Email"
          value={form.email}
          onChange={(e) =>
            setForm({ ...form, email: e.target.value })
          }
        />

        <input
          type="password"
          required
          minLength={8}
          maxLength={100}
          autoComplete="new-password"
          className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream"
          placeholder="Password (at least 8 characters)"
          value={form.password}
          onChange={(e) =>
            setForm({ ...form, password: e.target.value })
          }
        />

        <select
          className="w-full border border-moss/20 rounded-xl px-4 py-3 bg-cream"
          value={form.role}
          onChange={(e) =>
            setForm({ ...form, role: e.target.value })
          }
        >
          <option value="STUDENT">I am a student</option>
          <option value="INSTRUCTOR">I am an instructor</option>
        </select>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-pine text-cream py-3 rounded-full disabled:opacity-60"
        >
          {loading ? "Creating account..." : "Join Learn Hub"}
        </button>
      </form>

      <p className="text-sm text-leaf mt-4">
        Already growing with us?{" "}
        <Link to="/login" className="text-pine underline">
          Log in
        </Link>
      </p>
    </div>
  );
}
