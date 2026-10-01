import { useEffect, useState } from 'react';
import api from '../services/api';
import { useAuth } from '../context/AuthContext';

export const ServiceRequests = () => {
    const { user } = useAuth();
    const [requests, setRequests] = useState<any[]>([]);
    const [description, setDescription] = useState('');
    const [message, setMessage] = useState('');

    const fetchRequests = async () => {
        try {
            const res = await api.get('/service-requests');
            setRequests(res.data);
        } catch (error) {
            console.error('Failed to fetch service requests', error);
        }
    };

    useEffect(() => {
        fetchRequests();
    }, []);

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.post('/service-requests', { description });
            setMessage('Request submitted successfully.');
            setDescription('');
            fetchRequests();
        } catch (error: any) {
            setMessage(error.response?.data?.message || 'Failed to submit request.');
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">Campus Service Requests</h1>
            
            {message && <div className="mb-4 p-2 bg-blue-100 text-blue-800 rounded">{message}</div>}

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                {user?.role !== 'ROLE_ADMIN' && (
                    <div className="md:col-span-1 bg-white p-6 rounded shadow self-start">
                        <h2 className="text-xl font-bold mb-4">Submit New Request</h2>
                        <form onSubmit={handleSubmit}>
                            <textarea 
                                className="w-full border p-2 rounded mb-4" 
                                rows={4}
                                placeholder="Describe your request (e.g., Broken locker, PC issue in Lab)..." 
                                value={description}
                                onChange={e => setDescription(e.target.value)}
                                required
                            />
                            <button type="submit" className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 font-semibold">
                                Submit Request
                            </button>
                        </form>
                    </div>
                )}

                <div className={`${user?.role === 'ROLE_ADMIN' ? 'md:col-span-3' : 'md:col-span-2'} bg-white p-6 rounded shadow`}>
                    <h2 className="text-xl font-bold mb-4">Request History</h2>
                    {requests.length === 0 ? (
                        <p className="text-gray-500">No service requests found.</p>
                    ) : (
                        <div className="space-y-4 max-h-96 overflow-y-auto">
                            {requests.map(req => (
                                <div key={req.id} className="p-4 border rounded bg-gray-50 flex flex-col sm:flex-row justify-between sm:items-center">
                                    <div>
                                        {user?.role === 'ROLE_ADMIN' && <p className="font-semibold text-blue-600">{req.user?.username}</p>}
                                        <p className="text-gray-800">{req.description}</p>
                                        <p className="text-xs text-gray-500 mt-1">{new Date(req.createdAt).toLocaleString()}</p>
                                    </div>
                                    <span className={`mt-2 sm:mt-0 px-3 py-1 rounded text-xs font-bold ${req.status === 'PENDING' ? 'bg-yellow-200 text-yellow-800' : req.status === 'RESOLVED' ? 'bg-green-200 text-green-800' : 'bg-gray-200 text-gray-800'}`}>
                                        {req.status}
                                    </span>
                                </div>
                            ))}
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};
