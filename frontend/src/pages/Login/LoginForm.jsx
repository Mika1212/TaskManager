import { useState } from 'react';
import { login } from '../../api/authApi';
import './Login.css';

export default function LoginForm() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async e => {
        e.preventDefault();
        const data = await login(email, password);
        localStorage.setItem('jwt', data.token);
        window.location.href = '/projects';
    };

    return (
        <form>
            <input value={email} onChange={e => setEmail(e.target.value)} />
            <input value={password} onChange={e => setPassword(e.target.value)} />
            <button>Login</button>
        </form>
    );
}
