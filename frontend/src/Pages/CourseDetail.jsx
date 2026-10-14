import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";

export default function CourseDetail() {
  const { id } = useParams();
  const [course, setCourse] = useState(null);
  const [msg, setMsg] = useState("");
  const { user } = useAuth();
  const navigate = useNavigate();

  function load() {
    api.get(`/courses/${id}`).then((r) => setCourse(r.data));
  }
  useEffect(load, [id]);

  async function enroll() {
    if (!user) return navigate("/login");
    try {
      await api.post(`/courses/${id}/enroll`);
      load();
    } catch (e) {
      setMsg(e.response?.data?.message || "Could not enroll");
    }
  }

  if (!course) return <div className="p-12 text-leaf">Loading the grove…</div>;

  return (
    <div className="max-w-4xl mx-auto px-5 py-12">
      <p className="text-moss text-sm uppercase tracking-wide">{course.category} · {course.level}</p>
      <h1 className="font-display text-4xl text-pine mt-2">{course.title}</h1>
      <p className="text-leaf mt-4 leading-relaxed">{course.description}</p>
      <p className="text-sm text-moss mt-2">Instructor {course.instructorName} · pass score {course.passScore}%</p>

      <div className="mt-6 flex items-center gap-4">
        {!course.enrolled ? (
          <button onClick={enroll} className="bg-pine text-cream px-6 py-3 rounded-full">Enroll & start growing</button>
        ) : (
          <div className="flex-1">
            <div className="text-sm text-leaf mb-1">{course.progressPercent}% complete</div>
            <div className="h-2 bg-foam rounded-full overflow-hidden">
              <div className="h-full progress-bar" style={{ width: `${course.progressPercent}%` }} />
            </div>
          </div>
        )}
      </div>
      {msg && <p className="text-red-700 text-sm mt-2">{msg}</p>}

      <div className="mt-10 space-y-6">
        {course.modules?.map((m) => (
          <div key={m.id} className="bg-white/70 rounded-3xl p-5 border border-moss/10">
            <h3 className="font-display text-xl text-pine">{m.title}</h3>
            <ul className="mt-3 space-y-2">
              {m.lessons.map((l) => (
                <li key={l.id} className="flex items-center justify-between text-sm">
                  <span className={l.unlocked ? "text-pine" : "text-leaf/50"}>
                    {l.completed ? "✅" : l.unlocked ? "🟢" : "🔒"} {l.title}
                    {l.hasQuiz ? " · quiz" : ""}
                  </span>
                  {l.unlocked ? (
                    <Link to={`/learn/${l.id}`} className="text-moss hover:text-pine">Open</Link>
                  ) : (
                    <span className="text-leaf/40">Locked</span>
                  )}
                </li>
              ))}
            </ul>
          </div>
        ))}
      </div>

      {course.certificate && (
        <div className="mt-8 p-6 rounded-3xl bg-sand">
          You earned certificate <b>{course.certificate.certificateCode}</b>.{" "}
          <Link className="underline text-pine" to="/dashboard">View in your garden</Link>
        </div>
      )}
    </div>
  );
}
