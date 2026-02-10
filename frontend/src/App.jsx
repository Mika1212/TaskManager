import { useEffect, useState } from 'react';
import { getMe } from './api/userApi';

function App() {
    const [count, setCount] = useState(0);

    useEffect(() => {
        const token = localStorage.getItem('jwt');

        if (!token) {
            window.location.href = '/login';
            return;
        }

        getMe().catch(() => {
            localStorage.removeItem('jwt');
            window.location.href = '/login';
        });
    }, []);

    return (
        <>
            <h1>App</h1>
        </>
    );
}

export default App;