import { useNavigate } from 'react-router-dom';
import { clearAuth } from '../../api/authHelpers';
import './Home.css';

export default function Home() {
    const navigate = useNavigate();

    const handleLogout = () => {
        clearAuth();
        navigate('/login');
    };

    return (
        <div className="home-page">
            <h1>Home</h1>
            <button className="logout-button" onClick={handleLogout}>
                Logout
            </button>
        </div>
    );
}
