import { useState } from 'react';
import { useNavigate, useLocation, Link } from 'react-router-dom';
import apiClient from '../api/client';
import { useAuth } from '../context/AuthContext';

function Login() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();
  const { login } = useAuth();

  const justRegistered = location.state?.justRegistered;

  async function handleSubmit(e) {
    e.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      const response = await apiClient('/auth/login', {
        method: 'POST',
        body: JSON.stringify( {username, password }),
      });

      login(response.token, response.username);
      navigate('/albums');
    } catch (err) {
      setError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 px-4">
      <div className="w-full max-w-md bg-slate-800 rounded-xl shadow-lg p-8">
        <h1 className="text-2xl font-bold text-white mb-2 text-center">
          Welcome back
        </h1>

        {justRegistered && (
          <p className="text-green-400 text-sm bg-green-950/50 border border-green-900 rounded-lg px-3 py-2 mb-4 text-center">
            Account created! Please log in.
          </p>
        )}

        <form onSubmit={handleSubmit} className="space-y-4 mt-4">
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-slate-300 mb-1">
              Username
            </label>
            <input
              id="username"
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
              className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div>
            <label htmlFor="password" className="block text-sm font-medium text-slate-300 mb-1">
              Password
            </label>
            <input
              id="password"
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
              className="w-full px-3 py-2 bg-slate-700 border border-slate-600 rounded-lg text-white focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          {error && (
            <p className="text-red-400 text-sm bg-red-950/50 border border-red-900 rounded-lg px-3 py-2">
              {error}
            </p>
          )}

          <button
            type="submit"
            disabled={isSubmitting}
            className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-800 disabled:cursor-not-allowed text-white font-medium py-2 rounded-lg transition-colors"
          >
            {isSubmitting ? 'Logging in...' : 'Log in'}
          </button>
        </form>

        <p className="text-slate-400 text-sm text-center mt-6">
          Don't have an account?{' '}
          <Link to="/register" className="text-blue-400 hover:text-blue-300">
            Register
          </Link>
        </p>
      </div>
    </div>
  )
}

export default Login;