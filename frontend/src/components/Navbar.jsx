import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

function Navbar() {
  const { username, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login');
  }

  return (
    <nav className="bg-slate-800 border-b border-slate-700 px-6 py-4 flex items-center justify-between">
      <h1 className="text-xl font-bold text-white">Album Rating Tracker</h1>
      <div className="flex items-center gap-4">
        <span className="text-slate-400 text-sm">Hi, {username}</span>
        <Link to="/albums/add" className="text-blue-400 hover:text-blue-300 text-sm font-medium">
          + Add Album
        </Link>
        <Link to="/weights" className="text-blue-400 hover:text-blue-300 text-sm font-medium">
          Weights
        </Link>
        <button
          onClick={handleLogout}
          className="text-slate-400 hover:text-white text-sm font-medium"
        >
          Log out
        </button>
      </div>
    </nav>
  );
}

export default Navbar;