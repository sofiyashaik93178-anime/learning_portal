import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";

export default function Instructor() {
  const { user } = useAuth();
  const [data, setData] = useState(null);
  const [form, setForm] = useState({ title: "", description: "", category: "Skills", level: "Beginner", durationHours: 4, thumbnail: "leaf" });

  function load() {
    api.get("/instructor/dashboard").then((r) => setData(r.data));
  }
  useEffect(load, []);

  async function create(e) {
    e.preventDefault();
    await api.post("/instructor/courses", form);
    setForm({ ...form, title: "", description: "" });
    load();
  }

  if (!user) return <Navigate to="/login" />;

  return (
    <div className="max-w-5xl mx-auto px-5 py-12">
      <h1 className="font-display text-4xl text-pine">Instructor grove</h1>
      <p className="text-leaf mt-2">Create courses. Students will follow the learning path you plant.</p>

      <form onSubmit={create} className="mt-8 grid md:grid-cols-2 gap-3 bg-white/70 p-6 rounded-3xl border border-moss/10">
        <input className="border border-moss/20 rounded-xl px-3 py-2 bg-cream" placeholder="Course title" value={form.title} onChange={(e) => setForm({ ...form, title: e.target.value })} required />
        <input className="border border-moss/20 rounded-xl px-3 py-2 bg-cream" placeholder="Category" value={form.category} onChange={(e) => setForm({ ...form, category: e.target.value })} />
        <textarea className="md:col-span-2 border border-moss/20 rounded-xl px-3 py-2 bg-cream" placeholder="Description" value={form.description} onChange={(e) => setForm({ ...form, description: e.target.value })} />
        <button className="bg-pine text-cream rounded-full py-2 md:col-span-2">Create course</button>
      </form>

      <div className="mt-8 space-y-3">
        {data?.courses?.map((c) => (
          <div key={c.id} className="bg-foam/60 rounded-2xl px-5 py-4 flex justify-between">
            <div>
              <div className="font-display text-xl text-pine">{c.title}</div>
              <div className="text-xs text-moss">{c.category} · {c.enrollments} enrolled</div>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
