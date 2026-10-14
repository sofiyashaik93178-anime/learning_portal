import { useEffect, useState } from "react";
import { Link, Navigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";

export default function Dashboard() {
  const { user } = useAuth();
  const [data, setData] = useState(null);

  useEffect(() => {
    api.get("/student/dashboard").then((r) => setData(r.data));
  }, []);

  if (!user) return <Navigate to="/login" />;
  if (!data) return <div className="p-12">Gathering your garden…</div>;

  return (
    <div className="max-w-6xl mx-auto px-5 py-12">
      <h1 className="font-display text-4xl text-pine">My learning garden</h1>
      <p className="text-leaf mt-2">Enrolled courses, progress, quiz history, certificates.</p>

      <div className="grid md:grid-cols-2 gap-5 mt-8">
        {data.courses.length === 0 && (
          <p>No courses yet. <Link className="underline" to="/courses">Browse the grove</Link>.</p>
        )}
        {data.courses.map((c) => (
          <Link key={c.id} to={`/courses/${c.id}`} className="bg-white/80 rounded-3xl p-6 border border-moss/10">
            <h3 className="font-display text-2xl text-pine">{c.title}</h3>
            <p className="text-xs text-moss mt-1">{c.status}</p>
            <div className="h-2 bg-foam rounded-full mt-3 overflow-hidden">
              <div className="h-full progress-bar" style={{ width: `${c.progressPercent}%` }} />
            </div>
            <p className="text-sm mt-2 text-leaf">{c.progressPercent}% complete</p>
          </Link>
        ))}
      </div>

      <h2 className="font-display text-2xl text-pine mt-12">Quiz results</h2>
      <ul className="mt-3 space-y-2">
        {data.quizAttempts.map((a) => (
          <li key={a.id} className="text-sm bg-foam/50 rounded-xl px-4 py-3">
            {a.quizTitle} — {a.score}% {a.passed ? "✅" : "🔁 retry"}
          </li>
        ))}
        {data.quizAttempts.length === 0 && <li className="text-leaf text-sm">No attempts yet.</li>}
      </ul>

      <h2 className="font-display text-2xl text-pine mt-12">Certificates</h2>
      <div className="grid md:grid-cols-2 gap-4 mt-3">
        {data.certificates.map((c) => (
          <div key={c.id} className="rounded-3xl border-2 border-clay bg-cream p-6">
            <p className="text-xs uppercase tracking-widest text-moss">Learn Hub certificate</p>
            <h3 className="font-display text-xl mt-2">{c.courseTitle}</h3>
            <p className="text-sm mt-2">Awarded to {c.studentName}</p>
            <p className="font-mono text-xs mt-3">ID {c.certificateCode}</p>
          </div>
        ))}
        {data.certificates.length === 0 && <p className="text-leaf text-sm">Finish a course path to bloom a certificate.</p>}
      </div>
    </div>
  );
}
