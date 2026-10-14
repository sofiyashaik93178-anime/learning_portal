import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";

const thumbs = {
  java: "☕",
  python: "🐍",
  c: "🔷",
  cpp: "⚙️",
  javascript: "🟨",
  htmlcss: "🌐",
  mysql: "🗄️",
  dsa: "🧩",
};

export default function Home() {
  const [stats, setStats] = useState({ students: 0, courses: 0, instructors: 0, enrollments: 0 });
  const [courses, setCourses] = useState([]);
  const { loginSuccess } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    api.get("/public/stats").then((r) => setStats(r.data)).catch(() => {});
    api.get("/courses").then((r) => setCourses(r.data.slice(0, 4))).catch(() => {});
  }, []);

  async function demo(email, password, path) {
    const { data } = await api.post("/auth/login", { email, password });
    loginSuccess(data);
    navigate(path);
  }

  return (
    <div>
      <section className="relative overflow-hidden">
        <div className="max-w-6xl mx-auto px-5 pt-16 pb-20 grid md:grid-cols-2 gap-12 items-center">
          <div>
            <p className="uppercase tracking-[0.2em] text-moss text-xs font-semibold mb-4">LEARN HUB · YOUR LEARNING JOURNEY</p>
            <h1 className="font-display text-5xl md:text-6xl leading-[1.1] text-pine">
              Good Morning, Sofiya!
            </h1>
            <p className="mt-6 text-lg text-leaf/90 leading-relaxed max-w-xl">
              Keep learning, keep growing. You're doing great!
            </p>
            <div className="mt-8 flex flex-wrap gap-3">
              <Link to="/courses" className="bg-pine text-cream px-6 py-3 rounded-full font-medium shadow-soft hover:bg-leaf">
                Explore Courses
              </Link>
              <Link to="/register" className="border border-moss/40 px-6 py-3 rounded-full text-pine hover:bg-foam">
                Create your free account
              </Link>
            </div>
            <div className="mt-10 grid grid-cols-3 gap-4 max-w-md">
              {[
                [stats.courses, "available courses"],
                [stats.students, "students learning"],
                [stats.enrollments, "enrollments"],
              ].map(([n, l]) => (
                <div key={l}>
                  <div className="font-display text-3xl text-pine">{n}</div>
                  <div className="text-xs text-moss uppercase tracking-wide">{l}</div>
                </div>
              ))}
            </div>
          </div>
          <div className="relative">
            <div className="rounded-[2.2rem] bg-gradient-to-br from-pine to-moss p-8 text-cream shadow-soft min-h-[360px] flex flex-col justify-between">
              <div>
                <p className="text-mint text-sm">Today’s learning path</p>
                <h2 className="font-display text-3xl mt-2">Learning today for a brighter tomorrow</h2>
                <ul className="mt-6 space-y-3 text-foam/90">
                  <li>Enroll → follow modules in order</li>
                  <li>📖 Complete lessons at your pace</li>
                  <li>✅ Pass quizzes (70%) or retry with practice</li>
                  <li>🏅 Earn a certificate with a unique ID</li>
                </ul>
              </div>
              <p className="text-sm text-mint">Built with Spring Boot, React, JWT & MySQL — but it feels like a walk in the woods.</p>
            </div>
          </div>
        </div>
      </section>

      <section className="max-w-6xl mx-auto px-5">
        <div className="flex items-end justify-between mb-6">
          <h2 className="font-display text-3xl text-pine">Recommended for You</h2>
          <Link to="/courses" className="text-moss hover:text-pine text-sm">View All Courses →</Link>
        </div>
        <div className="grid sm:grid-cols-2 lg:grid-cols-4 gap-5">
          {courses.map((c) => (
            <Link key={c.id} to={`/courses/${c.id}`} className="bg-white/70 border border-moss/10 rounded-3xl p-5 hover:-translate-y-1 transition shadow-soft">
              <div className="text-4xl mb-3">{thumbs[c.thumbnail] || ""}</div>
              <div className="text-xs uppercase tracking-wide text-moss">{c.category} · {c.level}</div>
              <h3 className="font-display text-xl mt-1 text-pine">{c.title}</h3>
              <p className="text-sm text-leaf/80 mt-2 line-clamp-3">{c.description}</p>
            </Link>
          ))}
        </div>
      </section>

      <section className="max-w-6xl mx-auto px-5 mt-20 grid md:grid-cols-3 gap-6">
        {[
          ["01", "Discover", "Explore practical courses designed to build real skills."],
          ["02", "Keep Learning", "Follow lessons step by step at your own pace."],
          ["03", "Achieve", "Practice, track your progress, and earn certificates."],
        ].map((s) => (
          <div key={s[0]} className="rounded-3xl bg-foam/70 p-7 border border-moss/10">
            <div className="text-sage font-display text-3xl">{s[0]}</div>
            <h3 className="font-display text-2xl text-pine mt-2">{s[1]}</h3>
            <p className="text-leaf mt-2">{s[2]}</p>
          </div>
        ))}
      </section>

      <section className="max-w-6xl mx-auto px-5 mt-20">
        <div className="rounded-[2rem] bg-sand/80 border border-moss/15 p-8 md:p-10">
          <h2 className="font-display text-3xl text-pine mb-2">Explore demo accounts</h2>
          <p className="text-leaf mb-6">Use these accounts for your viva / last-date demo. No setup required.</p>
          <div className="grid md:grid-cols-3 gap-4">
            <button onClick={() => demo("student@learnhub.com", "Student@123", "/dashboard")} className="text-left bg-white rounded-2xl p-5 hover:shadow-soft">
              <div className="text-2xl">🎒</div>
              <div className="font-semibold text-pine mt-1">Student</div>
              <div className="text-xs text-moss mt-1">student@learnhub.com · Student@123</div>
            </button>
            <button onClick={() => demo("instructor@learnhub.com", "Instructor@123", "/instructor")} className="text-left bg-white rounded-2xl p-5 hover:shadow-soft">
              <div className="text-2xl">👩‍🏫</div>
              <div className="font-semibold text-pine mt-1">Instructor</div>
              <div className="text-xs text-moss mt-1">instructor@learnhub.com · Instructor@123</div>
            </button>
            <button onClick={() => demo("admin@learnhub.com", "Admin@123", "/admin")} className="text-left bg-white rounded-2xl p-5 hover:shadow-soft">
              <div className="text-2xl">🛡️</div>
              <div className="font-semibold text-pine mt-1">Admin</div>
              <div className="text-xs text-moss mt-1">admin@learnhub.com · Admin@123</div>
            </button>
          </div>
        </div>
      </section>
    </div>
  );
}
