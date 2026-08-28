import { useState } from 'react';

const CRITERIA = [
  { key: 'songwritingScore', label: 'Songwriting' },
  { key: 'productionScore', label: 'Production' },
  { key: 'cohesionScore', label: 'Cohesion' },
  { key: 'tracklistScore', label: 'Tracklist' },
  { key: 'replayValueScore', label: 'Replay Value' },
  { key: 'emotionalImpactScore', label: 'Emotional Impact' },
];

function AlbumCard({ album }) {
  const [isExpanded, setIsExpanded] = useState(false);

  return (
    <div
      onClick={() => setIsExpanded(!isExpanded)}
      className="bg-slate-800 rounded-lg p-5 border border-slate-700 hover:border-slate-600 transition-colors cursor-pointer"
    >
      <div className="flex items-start justify-between mb-2">
        <div>
          <h2 className="text-white font-semibold">{album.title}</h2>
          <p className="text-slate-400 text-sm">{album.artist}</p>
        </div>
        <span className="bg-blue-600 text-white text-sm font-bold px-2 py-1 rounded">
          {album.weightedTotal}
        </span>
      </div>

      <div className="flex items-center justify-between">
        <div className="flex gap-2 text-xs text-slate-500">
          {album.genre && <span>{album.genre}</span>}
          {album.releaseYear && <span>· {album.releaseYear}</span>}
        </div>
        <span className="text-slate-500 text-xs">
          {isExpanded ? '▲ hide scores' : '▼ show scores'}
        </span>
      </div>

      {isExpanded && (
        <div className="mt-4 pt-4 border-t border-slate-700 space-y-2">
          {CRITERIA.map(({ key, label }) => (
            <div key={key} className="flex items-center justify-between text-sm">
              <span className="text-slate-400">{label}</span>
              <div className="flex items-center gap-2 flex-1 max-w-[60%] ml-4">
                <div className="flex-1 h-1.5 bg-slate-700 rounded-full overflow-hidden">
                  <div
                    className="h-full bg-blue-500 rounded-full"
                    style={{ width: `${(album[key] / 10) * 100}%` }}
                  />
                </div>
                <span className="text-white font-medium w-8 text-right">
                  {album[key]}
                </span>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}

export default AlbumCard;