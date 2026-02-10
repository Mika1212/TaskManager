import { useState } from 'react';
import { login } from '../../api/authApi';
import './Login.css';

export default function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);

    const handleSubmit = async e => {
        e.preventDefault();
        setError(null);

        try {
            const data = await login(email, password);
            localStorage.setItem('jwt', data.token);
            window.location.href = '/projects';
        } catch (e) {
            setError('Invalid email or password');
        }
    };

    return (
        <form className="login-card" onSubmit={handleSubmit}>
            <h1>Login</h1>

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

            <button type="submit">Login</button>

            {error && <div className="error">{error}</div>}
        </form>
    );
}
