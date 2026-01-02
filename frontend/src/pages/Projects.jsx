import React, { useEffect, useState } from 'react';
import { api } from '../api/api';

export default function Projects() {
    const [projects, setProjects] = useState([]);

    useEffect(() => {
        const fetchProjects = async () => {
            const res = await api.get('http://localhost:8082/projects');
            setProjects(res.data);
        };
        fetchProjects();
    }, []);

    return (
        <div>
            <h2>Projects</h2>
            <ul>
                {projects.map(p => (
                    <li key={p.id}>{p.name}</li>
                ))}
            </ul>
        </div>
    );
}
