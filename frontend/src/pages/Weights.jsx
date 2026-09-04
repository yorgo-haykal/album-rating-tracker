import { useState, useEffect } from 'react';
import apiClient from '../api/client';
import Navbar from '../components/Navbar';

const CRITERIA = [
  { key: 'songwritingWeight', label: 'Songwriting' },
  { key: 'productionWeight', label: 'Production' },
  { key: 'cohesionWeight', label: 'Cohesion' },
  { key: 'tracklistWeight', label: 'Tracklist' },
  { key: 'replayValueWeight', label: 'Replay Value' },
  { key: 'emotionalImpactWeight', label: 'Emotional Impact' },
];

function Weights() {
  const [weights, setWeights] = useState(null);
  const [isLoading, setIsLoading] = useState(true);
  const [loadError, setLoadError] = useState('');

  const [isSubmitting, setIsSubmitting] = useState(false);
  const [submitError, setSubmitError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  useEffect(() => {
    async function loadWeights() {
      try {
        const data = await apiClient('/weights');
        setWeights(data);
      } catch (err) {
        setLoadError(err.message);
      } finally {
        setIsLoading(false);
      }
    }
    loadWeights();
  }, []);

  function handleChange(key, value) {
    setSuccessMessage('');
    setWeights((prev) => ({ ...prev, [key]: parseFloat(value) || 0 }));
  }

  const total = weights
    ? CRITERIA.reduce((sum, { key }) => sum + (weights[key] || 0), 0)
    : 0;
  const roundedTotal = Math.round(total * 100) / 100;
  const isValidTotal = Math.abs(roundedTotal - 100) <= 0.01;

  async function handleSubmit(e) {
    e.preventDefault();
    setSubmitError('');
    setSuccessMessage('');
    setIsSubmitting(true);

    try {
      const updated = await apiClient('/weights', {
        method: 'PUT',
        body: JSON.stringify(weights),
      });
      setWeights(updated);
      setSuccessMessage('Weights updated! New totals will apply to all albums.');
    } catch (err) {
      setSubmitError(err.message);
    } finally {
      setIsSubmitting(false);
    }
  }

  return (
    <div className="min-h-screen bg-slate-900">
      <Navbar />

      <div className="max-w-xl mx-auto px-6 py-8">
        <h1 className="text-2xl font-bold text-white mb-2">Scoring Weights</h1>
        <p className="text-slate-400 text-sm mb-6">
          Adjust how much each category contributes to an album's overall score. Weights must add up to 100%.
        </p>

        {isLoading && <p className="text-slate-400">Loading...</p>}

        {loadError && (
          <p className="text-red-400 bg-red-950/50 border border-red-900 rounded-lg px-4 py-3">
            {loadError}
          </p>
        )}

        {weights && (
          <form onSubmit={handleSubmit} className="bg-slate-800 rounded-lg p-5 space-y-4">
            {CRITERIA.map(({ key, label }) => (
              <div key={key} className="flex items-center gap-3">
                <label className="text-slate-300 text-sm w-36 flex-shrink-0">{label}</label>
                <input
                  type="number"
                  min="0"
                  max="100"
                  step="0.01"
                  value={weights[key]}
                  onChange={(e) => handleChange(key, e.target.value)}
                  className="flex-1 px-3 py-1.5 bg-slate-700 border border-slate-600 rounded text-white text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                <span className="text-slate-500 text-sm w-4">%</span>
              </div>
            ))}

            <div
              className={`flex items-center justify-between pt-3 border-t border-slate-700 text-sm font-medium ${
                isValidTotal ? 'text-green-400' : 'text-amber-400'
              }`}
            >
              <span>Total</span>
              <span>{roundedTotal.toFixed(2)}% {isValidTotal ? '✓' : '(must equal 100%)'}</span>
            </div>

            {submitError && (
              <p className="text-red-400 text-sm bg-red-950/50 border border-red-900 rounded-lg px-3 py-2">
                {submitError}
              </p>
            )}

            {successMessage && (
              <p className="text-green-400 text-sm bg-green-950/50 border border-green-900 rounded-lg px-3 py-2">
                {successMessage}
              </p>
            )}

            <button
              type="submit"
              disabled={!isValidTotal || isSubmitting}
              className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-blue-800 disabled:cursor-not-allowed text-white font-medium py-2 rounded-lg transition-colors"
            >
              {isSubmitting ? 'Saving...' : 'Save Weights'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

export default Weights;