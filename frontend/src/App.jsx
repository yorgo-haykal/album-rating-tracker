import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import AlbumList from './pages/AlbumList';
import AddAlbum from './pages/AddAlbum';
import Weights from './pages/Weights';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route
            path="/albums"
            element={
              <ProtectedRoute>
                <AlbumList />
              </ProtectedRoute>
            }
          />
          <Route
            path="/albums/add"
            element={
              <ProtectedRoute>
                <AddAlbum />
              </ProtectedRoute>
            }
          />
          <Route
            path="/weights"
            element={
              <ProtectedRoute>
                <Weights />
              </ProtectedRoute>
            }
          />
          <Route path="/" element={<Navigate to="/albums" replace />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;