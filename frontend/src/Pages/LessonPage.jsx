import { useEffect, useState } from "react";
import { Link, useNavigate, useParams } from "react-router-dom";
import api from "../api";

export default function LessonPage() {
  const { id } = useParams();
  const [lesson, setLesson] = useState(null);
  const [answers, setAnswers] = useState({});
  const [result, setResult] = useState(null);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    setResult(null);
    api.get(`/lessons/${id}`).then((r) => setLesson(r.data)).catch((e) => setError(e.response?.data?.message || "Locked"));
  }, [id]);

  async function complete() {
    await api.post(`/lessons/${id}/complete`);
    navigate(`/courses/${lesson.courseId}`);
  }

  async function submitQuiz(e) {
    e.preventDefault();
    const { data } = await api.post(`/quizzes/${lesson.quiz.id}/submit`, answers);
    setResult(data);
    if (data.passed) setTimeout(() => navigate(`/courses/${lesson.courseId}`), 1600);
  }

  if (error) return <div className="max-w-2xl mx-auto p-12 text-leaf">{error} <Link to="/courses" className="underline">Back</Link></div>;
  if (!lesson) return <div className="p-12">Opening lesson…</div>;

  return (
    <div className="max-w-3xl mx-auto px-5 py-12">
      <p className="text-moss text-sm">{lesson.courseTitle} · {lesson.moduleTitle}</p>
      <h1 className="font-display text-4xl text-pine mt-2">{lesson.title}</h1>
      <div className="prose mt-6 bg-white/70 rounded-3xl p-6 border border-moss/10 text-leaf leading-relaxed"
        dangerouslySetInnerHTML={{ __html: lesson.content }} />

      {lesson.quiz && (
        <form onSubmit={submitQuiz} className="mt-8 space-y-6">
          <h2 className="font-display text-2xl text-pine">{lesson.quiz.title}</h2>
          {lesson.quiz.questions.map((q) => (
            <div key={q.id} className="bg-foam/60 rounded-2xl p-4">
              <p className="font-medium text-pine">{q.questionText}</p>
              {["A", "B", "C", "D"].map((opt) => (
                <label key={opt} className="flex gap-2 mt-2 text-sm">
                  <input type="radio" name={`q${q.id}`} required
                    onChange={() => setAnswers({ ...answers, [q.id]: opt })} />
                  {q[`option${opt}`]}
                </label>
              ))}
            </div>
          ))}
          <button className="bg-pine text-cream px-6 py-3 rounded-full">Submit quiz</button>
        </form>
      )}

      {result && (
        <div className={`mt-6 p-5 rounded-2xl ${result.passed ? "bg-mint" : "bg-sand"}`}>
          <p className="font-display text-2xl text-pine">{result.score.toFixed(0)}% {result.passed ? "— you passed!" : "— not yet"}</p>
          <p className="text-leaf mt-1">{result.passed ? "The next lesson is unlocking." : result.practice}</p>
        </div>
      )}

      {!lesson.quiz && (
        <button onClick={complete} className="mt-8 bg-pine text-cream px-6 py-3 rounded-full">Mark complete & continue</button>
      )}
    </div>
  );
}
