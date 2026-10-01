import { useEffect, useState } from 'react';
import api from '../services/api';

export const Resources = () => {
    const [resources, setResources] = useState<any[]>([]);
    const [selectedResource, setSelectedResource] = useState<any>(null);
    const [startTime, setStartTime] = useState('');
    const [endTime, setEndTime] = useState('');
    const [message, setMessage] = useState('');

    useEffect(() => {
        const fetchResources = async () => {
            try {
                const res = await api.get('/resources');
                setResources(res.data);
            } catch (err) {
                console.error('Failed to fetch resources');
            }
        };
        fetchResources();
    }, []);

    const handleBook = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            await api.post('/bookings', {
                resourceId: selectedResource.id,
                startTime: new Date(startTime).toISOString(),
                endTime: new Date(endTime).toISOString()
            });
            setMessage('Booking request submitted successfully!');
            setSelectedResource(null);
        } catch (err: any) {
            setMessage(err.response?.data || 'Booking failed');
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">Available Resources</h1>
            {message && <div className="mb-4 p-2 bg-blue-100 text-blue-800 rounded">{message}</div>}
            
            <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                {resources.map(resource => (
                    <div key={resource.id} className="bg-white p-4 rounded shadow border">
                        <h3 className="font-bold text-lg">{resource.name}</h3>
                        <p className="text-sm text-gray-600 mb-2">{resource.type}</p>
                        <p className="mb-2">{resource.description}</p>
                        <p className="text-sm font-semibold mb-4">Location: {resource.location}</p>
                        <button 
                            onClick={() => setSelectedResource(resource)}
                            className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600 w-full"
                        >
                            Book Resource
                        </button>
                    </div>
                ))}
            </div>

            {selectedResource && (
                <div className="fixed inset-0 bg-black bg-opacity-50 flex justify-center items-center">
                    <form onSubmit={handleBook} className="bg-white p-6 rounded shadow-lg w-96">
                        <h2 className="text-xl font-bold mb-4">Book {selectedResource.name}</h2>
                        <div className="mb-4">
                            <label className="block mb-1">Start Time</label>
                            <input 
                                type="datetime-local" 
                                value={startTime} 
                                onChange={e => setStartTime(e.target.value)} 
                                className="w-full border p-2 rounded" 
                                required 
                            />
                        </div>
                        <div className="mb-4">
                            <label className="block mb-1">End Time</label>
                            <input 
                                type="datetime-local" 
                                value={endTime} 
                                onChange={e => setEndTime(e.target.value)} 
                                className="w-full border p-2 rounded" 
                                required 
                            />
                        </div>
                        <div className="flex justify-end gap-2">
                            <button type="button" onClick={() => setSelectedResource(null)} className="px-4 py-2 bg-gray-300 rounded hover:bg-gray-400">Cancel</button>
                            <button type="submit" className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">Confirm Booking</button>
                        </div>
                    </form>
                </div>
            )}
        </div>
    );
};
