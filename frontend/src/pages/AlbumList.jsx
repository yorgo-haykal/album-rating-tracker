import { useCallback, useEffect , useState } from "react";
import { Link } from "react-router-dom";
import apiClient from "../api/client";
import AlbumCard from "../components/AlbumCard";
import Navbar from "../components/NavBar";
import AlbumFilters from "../components/AlbumFilters";

function AlbumList() {
  const [albums, setAlbums] = useState([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState("");

  const [genreFilter, setGenreFilter] = useState("");
  const [artistFilter, setArtistFilter] = useState("");
  const [sortBy, setSortBy] = useState("dateAdded");
  const [order, setOrder] = useState("desc");


  const fetchAlbums = useCallback(async () => {
    setIsLoading(true);
    setError('');

    const params = new URLSearchParams();
    if (genreFilter.trim()) params.set('genre', genreFilter.trim());
    if (artistFilter.trim()) params.set('artist', artistFilter.trim());
    params.set('sortBy', sortBy);
    params.set('order', order);

    try {
      const data = await apiClient(`/albums?${params.toString()}`);
      setAlbums(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setIsLoading(false);
    }
  }, [genreFilter, artistFilter, sortBy, order]);

  useEffect(() => {
    fetchAlbums();
  }, [fetchAlbums]);

  function handleAlbumDeleted(deletedId) {
    setAlbums((prev) => prev.filter((album) => album.id !== deletedId));
  }

  return (
    <div className="min-h-screen bg-slate-900">
      <Navbar />

      <div className="max-w-5xl mx-auto px-6 py-8">
        
        <AlbumFilters
          genreFilter={genreFilter}
          setGenreFilter={setGenreFilter}
          artistFilter={artistFilter}
          setArtistFilter={setArtistFilter}
          sortBy={sortBy}
          setSortBy={setSortBy}
          order={order}
          setOrder={setOrder}
        />

        {/* Content */}
        {isLoading && (
          <p className="text-slate-400 text-center py-12">Loading albums...</p>
        )}

        {error && (
          <p className="text-red-400 bg-red-950/50 border border-red-900 rounded-lg px-4 py-3">
            {error}
          </p>
        )}

        {!isLoading && !error && albums.length === 0 && (
          <div className="text-center py-16">
            <p className="text-slate-400 mb-4">No albums yet.</p>
            <Link
              to="/albums/add"
              className="text-blue-400 hover:text-blue-300 font-medium"
            >
              Add your first album
            </Link>
          </div>
        )}

        {!isLoading && !error && albums.length > 0 && (
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4">
            {albums.map((album) => (
              <AlbumCard key={album.id} album={album} onDelete={handleAlbumDeleted} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
  
}

export default AlbumList;