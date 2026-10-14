import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../api";

const thumbs = { java: "☕", python: "🐍", c: "🔷", cpp: "⚙️", javascript: "🟨", htmlcss: "🌐", mysql: "🗄️", dsa: "🧩" };

export default function Courses() {
  const [courses, setCourses] = useState([]);
  const [q, setQ] = useState("");

  useEffect(() => {
    api.get("/courses").then((r) => setCourses(r.data));
  }, []);

  const filtered = courses.filter((c) =>
    (c.title + c.category + c.description).toLowerCase().includes(q.toLowerCase())
  );

  return (
    <div className="max-w-6xl mx-auto px-5 py-12">
      <h1 className="font-display text-4xl text-pine">Find a course that feels like sunlight</h1>
      <input
        className="mt-6 w-full max-w-lg border border-moss/20 rounded-full px-5 py-3 bg-white/80"
        placeholder="Search Java, Python, C, C++…"
        value={q}
        onChange={(e) => setQ(e.target.value)}
      />
      <div className="grid sm:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
        {filtered.map((c) => (
          <Link key={c.id} to={`/courses/${c.id}`} className="bg-white/80 rounded-3xl p-6 border border-moss/10 hover:-translate-y-1 transition shadow-soft">
            <div className="text-4xl">{thumbs[c.thumbnail] || "🌿"}</div>
            <div className="text-xs text-moss uppercase mt-3">{c.category} · {c.level} · {c.durationHours}h</div>
            <h2 className="font-display text-2xl text-pine mt-1">{c.title}</h2>
            <p className="text-sm text-leaf mt-2 line-clamp-3">{c.description}</p>
            <p className="text-xs text-moss mt-4">with {c.instructorName}</p>
          </Link>
        ))}
      </div>
    </div>
  );
}
