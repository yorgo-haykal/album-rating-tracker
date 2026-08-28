function AlbumFilters({ genreFilter, setGenreFilter, artistFilter, setArtistFilter, sortBy, setSortBy, order, setOrder }) {
  return (
    <div className="bg-slate-800 rounded-lg p-4 mb-6 flex flex-wrap gap-4 items-end">
      <div>
        <label className="block text-xs text-slate-400 mb-1">Filter by genre</label>
        <input
          type="text"
          value={genreFilter}
          onChange={(e) => setGenreFilter(e.target.value)}
          placeholder="e.g. Rock"
          className="px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div>
        <label className="block text-xs text-slate-400 mb-1">Filter by artist</label>
        <input
          type="text"
          value={artistFilter}
          onChange={(e) => setArtistFilter(e.target.value)}
          placeholder="e.g. Tame Impala"
          className="px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        />
      </div>

      <div>
        <label className="block text-xs text-slate-400 mb-1">Sort by</label>
        <select
          value={sortBy}
          onChange={(e) => setSortBy(e.target.value)}
          className="px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="dateAdded">Date added</option>
          <option value="score">Score</option>
          <option value="artist">Artist</option>
          <option value="genre">Genre</option>
        </select>
      </div>

      <div>
        <label className="block text-xs text-slate-400 mb-1">Order</label>
        <select
          value={order}
          onChange={(e) => setOrder(e.target.value)}
          className="px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
        >
          <option value="desc">Descending</option>
          <option value="asc">Ascending</option>
        </select>
      </div>
    </div>
  );
}

export default AlbumFilters;