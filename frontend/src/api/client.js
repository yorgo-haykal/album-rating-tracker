const API_BASE_URL = 'http://localhost:8080/api';

async function apiClient(endpoint, options={}) {
  const token = localStorage.getItem('token');

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers,
  });

  if (!response.ok) {
    const errorBody = await response.json().catch(() => ({ message: 'An error occurred' }));
    throw new Error(errorBody.message || `Request failed with status ${response.status}`);
  }

  const text = await response.text();
  return text ? JSON.parse(text) : null;
}

export default apiClient;