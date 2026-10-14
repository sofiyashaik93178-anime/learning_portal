# Learn Hub frontend

## Run
1. Start the backend first on `http://127.0.0.1:8080` (see `../backend/README.md`).
2. Open a terminal in this `frontend` folder.
3. Run `npm install`, then `npm run dev`.
4. Open `http://127.0.0.1:5173`.

Vite proxies `/api` requests to `http://127.0.0.1:8080` by default. To use another backend port, copy `.env.example` to `.env` and change `VITE_BACKEND_TARGET`.

If the browser shows `ECONNREFUSED` in the Vite terminal, the backend is not running at the configured target.
