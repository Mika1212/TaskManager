import { api } from './api';

export const getMe = async () => {
    const res = await api.get('/me');
    return res.data;
};
