import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login, register } from '../../api/authApi';

export default function AuthForm({ mode }) {
    const [name, setName] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const navigate = useNavigate();

    const isLogin = mode === 'login';

    const handleSubmit = async e => {
        e.preventDefault();
        setError(null);

        try {
            const data = isLogin
                ? await login(email, password)
                : await register(name, email, password);

            localStorage.setItem('jwt', data.token);
            navigate('/');
        } catch {
            setError(isLogin ? 'Invalid email or password' : 'Registration failed');
        }
    };

    return (
        <form className="login-card" onSubmit={handleSubmit}>
            <h1>{isLogin ? 'Login' : 'Register'}</h1>

            {!isLogin && (
                <input
                    value={name}
                    onChange={e => setName(e.target.value)}
                    placeholder="Name"
                />
            )}

            <input
                value={email}
                onChange={e => setEmail(e.target.value)}
                placeholder="Email"
            />

            <input
                type="password"
                value={password}
                onChange={e => setPassword(e.target.value)}
                placeholder="Password"
            />

            <button type="submit">
                {isLogin ? 'Login' : 'Register'}
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
    );
}
