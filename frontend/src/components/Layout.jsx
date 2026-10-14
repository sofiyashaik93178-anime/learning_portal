import { Link, NavLink, Outlet, useNavigate } from "react-router-dom";
import { useAuth } from "../AuthContext";

export default function Layout() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex flex-col">
      <header className="sticky top-0 z-40 bg-cream/80 backdrop-blur-md border-b border-moss/15">
        <div className="max-w-6xl mx-auto px-5 h-16 flex items-center justify-between">
          <Link to="/" className="flex items-center gap-2 font-display text-2xl text-pine">
            <span className="text-3xl" aria-hidden="true">◆</span> Learn Hub
          </Link>
          <nav className="hidden md:flex items-center gap-6 text-sm font-medium text-leaf">
            <NavLink to="/courses" className={({ isActive }) => isActive ? "text-pine" : "hover:text-pine"}>Courses</NavLink>
            {user?.role === "STUDENT" && <NavLink to="/dashboard">My Learning</NavLink>}
            {(user?.role === "INSTRUCTOR" || user?.role === "ADMIN") && <NavLink to="/instructor">Teach</NavLink>}
            {user?.role === "ADMIN" && <NavLink to="/admin">Admin</NavLink>}
          </nav>
          <div className="flex items-center gap-3">
            {user ? (
              <>
                <span className="hidden sm:inline text-sm text-leaf">Hi, {user.name.split(" ")[0]}</span>
                <button
                  onClick={() => { logout(); navigate("/"); }}
                  className="text-sm px-3 py-1.5 rounded-full border border-moss/30 hover:bg-foam"
                >Log out</button>
              </>
            ) : (
              <>
                <Link to="/login" className="text-sm text-leaf hover:text-pine">Log in</Link>
                <Link to="/register" className="text-sm bg-pine text-cream px-4 py-2 rounded-full hover:bg-leaf">Join free</Link>
              </>
            )}
          </div>
        </div>
      </header>
      <main className="flex-1"><Outlet /></main>
      <footer className="bg-pine text-foam mt-16">
        <div className="max-w-6xl mx-auto px-5 py-10 grid md:grid-cols-3 gap-8">
          <div>
            <div className="font-display text-2xl mb-2">Learn Hub</div>
            <p className="text-mint text-sm leading-relaxed">Learn skills today, build a brighter tomorrow. Courses, practice, progress, and certificates in one place.</p>
          </div>
          <div className="text-sm space-y-1 text-mint">
            <p>Student · Instructor · Admin</p>
            <p>JWT-secured · Spring Boot · React</p>
          </div>
          <div className="text-sm text-mint">Made for learners who want to build real-world skills.</div>
        </div>
      </footer>
    </div>
  );
}
