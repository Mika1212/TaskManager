import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login, register } from '../../api/authApi';
import './Auth.css';

export default function AuthForm({ mode }) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [loading, setLoading] = useState(false);

    const navigate = useNavigate();
    const isLogin = mode === 'login';

    const handleSubmit = async e => {
        e.preventDefault();
        setError(null);
        setLoading(true);

        try {
            const data = isLogin
                ? await login(email, password)
                : await register(name, email, password);

            localStorage.setItem('jwt', data.token);
            navigate('/');
        } catch {
            setError(isLogin ? 'Invalid email or password' : 'Registration failed');
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="login-page">
            <form className="login-card" onSubmit={handleSubmit}>
                <h1>{isLogin ? 'Welcome Back!' : 'Create Account'}</h1>

                {!isLogin && (
                    <input
                        value={name}
                        onChange={e => setName(e.target.value)}
                        placeholder="Name"
                        required
                    />
                )}

                <input
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    placeholder="Email"
                    required
                />

                <input
                    type="password"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    placeholder="Password"
                    required
                />

                <button type="submit" disabled={loading}>
                    {loading
                        ? 'Please wait...'
                        : isLogin
                            ? 'Login'
                            : 'Register'}
                </button>

                {error && <div className="error">{error}</div>}

                <div className="auth-link">
                    {isLogin ? (
                        <>
                            Don't have an account? <Link to="/register">Register</Link>
                        </>
                    ) : (
                        <>
                            Already have an account? <Link to="/login">Login</Link>
                        </>
                    )}
                </div>
            </form>
        </div>
    );
}
