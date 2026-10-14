import { BrowserRouter, Routes, Route } from "react-router-dom";
import { AuthProvider } from "./AuthContext";
import Layout from "./components/Layout";
import Home from "./Pages/Home";
import Login from "./Pages/Login";
import Register from "./Pages/Register";
import Courses from "./Pages/Courses";
import CourseDetail from "./Pages/CourseDetail";
import LessonPage from "./Pages/LessonPage";
import Dashboard from "./Pages/Dashboard";
import Instructor from "./Pages/Instructor";
import Admin from "./Pages/Admin";

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route path="/courses" element={<Courses />} />
            <Route path="/courses/:id" element={<CourseDetail />} />
            <Route path="/learn/:id" element={<LessonPage />} />
            <Route path="/dashboard" element={<Dashboard />} />
            <Route path="/instructor" element={<Instructor />} />
            <Route path="/admin" element={<Admin />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}
