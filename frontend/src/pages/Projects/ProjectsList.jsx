import { useEffect, useState } from 'react';
import { api } from '../../api/api';

export default function ProjectsList() {
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        api.get('/projects').then(res => setProjects(res.data));
    }, []);

    return (
        <ul>
            {projects.map(p => (
                <li key={p.id}>{p.name}</li>
            ))}
        </ul>
    );
}
