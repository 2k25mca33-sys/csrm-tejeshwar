import { useEffect, useState } from 'react';
import api from '../services/api';

export const Notifications = () => {
    const [notifications, setNotifications] = useState<any[]>([]);

    const fetchNotifications = async () => {
        try {
            const res = await api.get('/notifications');
            setNotifications(res.data);
        } catch (error) {
            console.error('Failed to fetch notifications', error);
        }
    };

    useEffect(() => {
        fetchNotifications();
    }, []);

    const markAsRead = async (id: number) => {
        try {
            await api.put(`/notifications/${id}/read`);
            setNotifications(notifications.map(n => n.id === id ? { ...n, read: true } : n));
        } catch (error) {
            console.error('Failed to mark read', error);
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">Notifications</h1>
            <div className="max-w-2xl">
                {notifications.length === 0 ? (
                    <p className="text-gray-500">You have no notifications.</p>
                ) : (
                    <div className="space-y-4">
                        {notifications.map(notif => (
                            <div key={notif.id} className={`p-4 rounded shadow border-l-4 flex justify-between items-center ${notif.read ? 'bg-white border-gray-300' : 'bg-blue-50 border-blue-500'}`}>
                                <div>
                                    <p className={`${notif.read ? 'text-gray-700' : 'text-black font-semibold'}`}>{notif.message}</p>
                                    <p className="text-xs text-gray-500 mt-1">{new Date(notif.createdAt).toLocaleString()}</p>
                                </div>
                                {!notif.read && (
                                    <button onClick={() => markAsRead(notif.id)} className="text-blue-600 text-sm hover:underline font-semibold ml-4">
                                        Mark Read
                                    </button>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
};
