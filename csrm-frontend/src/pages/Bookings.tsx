import { useEffect, useState } from 'react';
import api from '../services/api';

export const Bookings = () => {
    const [bookings, setBookings] = useState<any[]>([]);

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                const res = await api.get('/bookings/my');
                setBookings(res.data);
            } catch (err) {
                console.error('Failed to fetch bookings');
            }
        };
        fetchBookings();
    }, []);

    const handleDelete = async (id: number) => {
        if(window.confirm('Are you sure you want to cancel this booking?')) {
            try {
                await api.delete(`/bookings/${id}`);
                setBookings(bookings.filter(b => b.id !== id));
            } catch (err) {
                alert('Failed to delete booking');
            }
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-4">My Bookings</h1>
            <div className="overflow-x-auto">
                <table className="min-w-full bg-white border shadow">
                    <thead>
                        <tr className="bg-gray-200">
                            <th className="py-2 px-4 border">Resource</th>
                            <th className="py-2 px-4 border">Start Time</th>
                            <th className="py-2 px-4 border">End Time</th>
                            <th className="py-2 px-4 border">Status</th>
                            <th className="py-2 px-4 border">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {bookings.map(booking => (
                            <tr key={booking.id} className="text-center">
                                <td className="py-2 px-4 border">{booking.resourceName}</td>
                                <td className="py-2 px-4 border">{new Date(booking.startTime).toLocaleString()}</td>
                                <td className="py-2 px-4 border">{new Date(booking.endTime).toLocaleString()}</td>
                                <td className="py-2 px-4 border">
                                    <span className={`px-2 py-1 rounded text-white ${booking.status === 'APPROVED' ? 'bg-green-500' : booking.status === 'PENDING' ? 'bg-yellow-500' : 'bg-red-500'}`}>
                                        {booking.status}
                                    </span>
                                </td>
                                <td className="py-2 px-4 border">
                                    <button onClick={() => handleDelete(booking.id)} className="bg-red-500 text-white px-3 py-1 rounded hover:bg-red-600">Cancel</button>
                                </td>
                            </tr>
                        ))}
                        {bookings.length === 0 && (
                            <tr>
                                <td colSpan={5} className="py-4 border text-gray-500">No bookings found.</td>
                            </tr>
                        )}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
