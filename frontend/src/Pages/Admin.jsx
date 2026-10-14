import { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";
import api from "../api";
import { useAuth } from "../AuthContext";

export default function Admin() {
  const { user } = useAuth();
  const [data, setData] = useState(null);

  useEffect(() => {
    api.get("/admin/dashboard").then((r) => setData(r.data));
  }, []);

  if (!user) return <Navigate to="/login" />;
  if (user.role !== "ADMIN") return <Navigate to="/" />;
  if (!data) return <div className="p-12">Loading…</div>;

  return (
    <div className="max-w-5xl mx-auto px-5 py-12">
      <h1 className="font-display text-4xl text-pine">Admin canopy</h1>
      <div className="grid grid-cols-2 md:grid-cols-4 gap-4 mt-8">
        {[
          ["Users", data.totalUsers],
          ["Students", data.students],
          ["Courses", data.totalCourses],
          ["Enrollments", data.totalEnrollments],
        ].map(([l, n]) => (
          <div key={l} className="bg-white/80 rounded-2xl p-4 border border-moss/10">
            <div className="text-xs text-moss uppercase">{l}</div>
            <div className="font-display text-3xl text-pine">{n}</div>
          </div>
        ))}
      </div>
      <h2 className="font-display text-2xl mt-10 text-pine">People on the platform</h2>
      <table className="w-full mt-4 text-sm">
        <thead><tr className="text-left text-moss"><th className="py-2">Name</th><th>Email</th><th>Role</th></tr></thead>
        <tbody>
          {data.users.map((u) => (
            <tr key={u.id} className="border-t border-moss/10">
              <td className="py-2">{u.name}</td>
              <td>{u.email}</td>
              <td>{u.role}</td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}
