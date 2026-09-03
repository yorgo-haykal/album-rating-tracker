import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import apiClient from '../api/client';
import Navbar from '../components/Navbar';

const CRITERIA = [
  { key: 'songwritingScore', label: 'Songwriting' },
  { key: 'productionScore', label: 'Production' },
  { key: 'cohesionScore', label: 'Cohesion' },
  { key: 'tracklistScore', label: 'Tracklist' },
  { key: 'replayValueScore', label: 'Replay Value' },
  { key: 'emotionalImpactScore', label: 'Emotional Impact' },
];

function AddAlbum() {
  const navigate = useNavigate();

  // Search state
  const [searchTitle, setSearchTitle] = useState('');
  const [searchArtist, setSearchArtist] = useState('');
  const [searchResults, setSearchResults] = useState([]);
  const [isSearching, setIsSearching] = useState(false);
  const [searchError, setSearchError] = useState('');

  // Form state
  const [title, setTitle] = useState('');
  const [artist, setArtist] = useState('');
  const [genre, setGenre] = useState('');
  const [releaseYear, setReleaseYear] = useState('');
  const [scores, setScores] = useState({
    songwritingScore: 5,
    productionScore: 5,
    cohesionScore: 5,
    tracklistScore: 5,
    replayValueScore: 5,
    emotionalImpactScore: 5,
  });

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState('');

  async function handleSearch(e) {
    e.preventDefault();
    setSearchError('');
    setIsSearching(true);

    const params = new URLSearchParams();
    if (searchTitle.trim()) params.set('albumTitle', searchTitle.trim());
    if (searchArtist.trim()) params.set('artist', searchArtist.trim());

    try {
      const results = await apiClient(`/musicbrainz/search?${params.toString()}`);
      setSearchResults(results);
    } catch (err) {
      setSearchError(err.message);
    } finally {
      setIsSearching(false);
    }
  }

  function handleSelectResult(result) {
    setTitle(result.title);
    setArtist(result.artist);
    setReleaseYear(result.releaseYear ? String(result.releaseYear) : '');
    // Genre isn't provided by MusicBrainz search results, so the user fills it in manually.
    setSearchResults([]);
  }

  function handleScoreChange(key, value) {
    setScores((prev) => ({ ...prev, [key]: parseFloat(value) }));
  }

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitError('');
    setIsSubmitting(true);

    try {
      await apiClient('/albums', {
        method: 'POST',
        body: JSON.stringify({
          title,
          artist,
          genre: genre || null,
          releaseYear: releaseYear ? parseInt(releaseYear, 10) : null,
          ...scores,
        }),
      });
      navigate('/albums');
    } catch (err) {
      setSubmitError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  const canSubmit = title.trim() && artist.trim();

  return (
    <div className="min-h-screen bg-slate-900">
      <Navbar />

      <div className="max-w-2xl mx-auto px-6 py-8">
        <h1 className="text-2xl font-bold text-white mb-6">Add an Album</h1>

        {/* Search section */}
        <div className="bg-slate-800 rounded-lg p-5 mb-6">
          <h2 className="text-white font-semibold mb-3">Search MusicBrainz</h2>
          <form onSubmit={handleSearch} className="flex flex-wrap gap-3 items-end">
            <div className="flex-1 min-w-[150px]">
              <label className="block text-xs text-slate-400 mb-1">Album title</label>
              <input
                type="text"
                value={searchTitle}
                onChange={(e) => setSearchTitle(e.target.value)}
                placeholder="e.g. Currents"
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div className="flex-1 min-w-[150px]">
              <label className="block text-xs text-slate-400 mb-1">Artist</label>
              <input
                type="text"
                value={searchArtist}
                onChange={(e) => setSearchArtist(e.target.value)}
                placeholder="e.g. Tame Impala"
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <button
              type="submit"
              disabled={isSearching}
              className="bg-blue-600 hover:bg-blue-700 disabled:bg-blue-800 text-white text-sm font-medium px-4 py-1.5 rounded transition-colors"
            >
              {isSearching ? 'Searching...' : 'Search'}
            </button>
          </form>

          {searchError && (
            <p className="text-red-400 text-sm mt-3">{searchError}</p>
          )}

          {searchResults.length > 0 && (
            <div className="mt-4 space-y-2 max-h-64 overflow-y-auto">
              {searchResults.map((result) => (
                <button
                  key={result.musicbrainzId}
                  onClick={() => handleSelectResult(result)}
                  className="w-full flex items-center gap-3 bg-slate-700 hover:bg-slate-600 rounded-lg p-2 text-left transition-colors"
                >
                  <img
                    src={result.coverArtUrl}
                    alt=""
                    className="w-12 h-12 rounded object-cover bg-slate-600 flex-shrink-0"
                    onError={(e) => { e.target.style.display = 'none'; }}
                  />
                  <div>
                    <p className="text-white text-sm font-medium">{result.title}</p>
                    <p className="text-slate-400 text-xs">
                      {result.artist}
                      {result.releaseYear && ` · ${result.releaseYear}`}
                    </p>
                  </div>
                </button>
              ))}
            </div>
          )}
        </div>

        {/* Album details form */}
        <form onSubmit={handleSubmit} className="bg-slate-800 rounded-lg p-5 space-y-4">
          <h2 className="text-white font-semibold">Album Details</h2>

          <div className="grid grid-cols-2 gap-3">
            <div>
              <label className="block text-xs text-slate-400 mb-1">Title *</label>
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                required
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-xs text-slate-400 mb-1">Artist *</label>
              <input
                type="text"
                value={artist}
                onChange={(e) => setArtist(e.target.value)}
                required
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-xs text-slate-400 mb-1">Genre</label>
              <input
                type="text"
                value={genre}
                onChange={(e) => setGenre(e.target.value)}
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
            <div>
              <label className="block text-xs text-slate-400 mb-1">Release year</label>
              <input
                type="number"
                value={releaseYear}
                onChange={(e) => setReleaseYear(e.target.value)}
                className="w-full px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              />
            </div>
          </div>

          <div className="pt-2 border-t border-slate-700 space-y-3">
            <h3 className="text-slate-300 text-sm font-medium">Scores (0–10)</h3>
            {CRITERIA.map(({ key, label }) => (
              <div key={key} className="flex items-center gap-3">
                <label className="text-slate-400 text-sm w-32 flex-shrink-0">{label}</label>
                <input
                  type="range"
                  min="0"
                  max="10"
                  step="0.5"
                  value={scores[key]}
                  onChange={(e) => handleScoreChange(key, e.target.value)}
                  className="flex-1"
                />
                <span className="text-white text-sm font-medium w-10 text-right">
                  {scores[key]}
                </span>
              </div>
            ))}
          </div>

          {submitError && (
            <p className="text-red-400 text-sm bg-red-950/50 border border-red-900 rounded-lg px-3 py-2">
              {submitError}
            </p>
          )}

          <button
            type="submit"
            disabled={!canSubmit || isSubmitting}
            className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-800 disabled:cursor-not-allowed text-white font-medium py-2 rounded-lg transition-colors"
          >
            {isSubmitting ? 'Saving...' : 'Save Album'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AddAlbum;