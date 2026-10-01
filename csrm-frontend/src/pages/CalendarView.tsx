import { useEffect, useState } from 'react';
import api from '../services/api';

export const CalendarView = () => {
    const [bookings, setBookings] = useState<any[]>([]);

    useEffect(() => {
        const fetchBookings = async () => {
            try {
                // Fetch all bookings for the calendar view
                const res = await api.get('/bookings/my');
                setBookings(res.data);
            } catch (err) {
                console.error('Failed to fetch bookings');
            }
        };
        fetchBookings();
    }, []);

    // Group bookings by date
    const groupedBookings = bookings.reduce((acc: any, booking: any) => {
        const date = new Date(booking.startTime).toLocaleDateString();
        if (!acc[date]) acc[date] = [];
        acc[date].push(booking);
        return acc;
    }, {});

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">Booking Calendar</h1>
            <div className="space-y-6">
                {Object.keys(groupedBookings).length === 0 ? (
                    <p className="text-gray-500">No bookings scheduled.</p>
                ) : (
                    Object.keys(groupedBookings).map(date => (
                        <div key={date} className="bg-white p-4 rounded shadow border-l-4 border-blue-500">
                            <h2 className="text-xl font-bold mb-4 border-b pb-2">{date}</h2>
                            <div className="space-y-3">
                                {groupedBookings[date].map((booking: any) => (
                                    <div key={booking.id} className="flex justify-between items-center bg-gray-50 p-3 rounded">
                                        <div>
                                            <span className="font-bold">{booking.resourceName}</span>
                                            <span className="text-gray-600 text-sm ml-2">
                                                {new Date(booking.startTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})} - 
                                                {new Date(booking.endTime).toLocaleTimeString([], {hour: '2-digit', minute:'2-digit'})}
                                            </span>
                                        </div>
                                        <span className={`px-2 py-1 rounded text-xs text-white ${booking.status === 'ACTIVE' ? 'bg-green-500' : booking.status === 'UPCOMING' ? 'bg-blue-500' : booking.status === 'COMPLETED' ? 'bg-gray-500' : 'bg-red-500'}`}>
                                            {booking.status}
                                        </span>
                                    </div>
                                ))}
                            </div>
                        </div>
                    ))
                )}
            </div>
        </div>
    );
};
