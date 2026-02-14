import { Routes, Route, Navigate } from 'react-router-dom';
import AuthForm from './pages/auth/AuthForm';
import ProtectedRoute from './components/ProtectedRoute';

function Home() {
    return <h1>App</h1>;
}

function App() {
    return (
        <Routes>
            <Route path="/login" element={<AuthForm mode="login" />} />
            <Route path="/register" element={<AuthForm mode="register" />} />

            <Route
                path="/"
                element={
                    <ProtectedRoute>
                        <Home />
                    </ProtectedRoute>
                }
            />

            <Route path="*" element={<Navigate to="/" />} />
        </Routes>
    );
}

export default App;
