import { createContext, useContext, useEffect, useState } from "react";
import api from "./api";

const AuthContext = createContext(null);

function readSavedUser() {
  try {
    const raw = localStorage.getItem("lh_user");
    return raw ? JSON.parse(raw) : null;
  } catch {
    localStorage.removeItem("lh_user");
    localStorage.removeItem("lh_token");
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(readSavedUser);

  useEffect(() => {
    const token = localStorage.getItem("lh_token");
    if (!token) return;
    let active = true;
    api.get("/auth/me").then((r) => {
      if (!active) return;
      setUser(r.data);
      localStorage.setItem("lh_user", JSON.stringify(r.data));
    }).catch(() => {
      if (!active) return;
      localStorage.removeItem("lh_token");
      localStorage.removeItem("lh_user");
      setUser(null);
    });
    return () => { active = false; };
  }, []);

  function loginSuccess(data) {
    if (!data?.token || !data?.user) throw new Error("The backend returned an invalid login response.");
    localStorage.setItem("lh_token", data.token);
    localStorage.setItem("lh_user", JSON.stringify(data.user));
    setUser(data.user);
  }

  function logout() {
    localStorage.removeItem("lh_token");
    localStorage.removeItem("lh_user");
    setUser(null);
  }

  return <AuthContext.Provider value={{ user, loginSuccess, logout, setUser }}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
