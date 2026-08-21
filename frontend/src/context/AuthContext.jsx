import { createContext, useContext, useState } from 'react';

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setToken] = useState(localStorage.getItem('token'));
  const [username, setUsername] = useState(localStorage.getItem('username'));

  function login(newToken, newUsername) {
    localStorage.setItem('token', newToken);
    localStorage.setItem('username', newUsername);
    setToken(newToken);
    setUsername(newUsername);
  }

  function logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('username');
    setToken(null);
    setUsername(null);
  }

  const isAuthenticated = !!token;

  return (
    <AuthContext.Provider value={{ token, username, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}