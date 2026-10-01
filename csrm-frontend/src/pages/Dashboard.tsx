import { useAuth } from '../context/AuthContext';
import { Link } from 'react-router-dom';

export const Dashboard = () => {
    const { user } = useAuth();

    return (
        <div className="p-8">
            <h1 className="text-3xl font-bold mb-6">Welcome to CSRM, {user?.username}</h1>
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                <div className="bg-white p-6 rounded shadow">
                    <h2 className="text-xl font-semibold mb-2">Resources</h2>
                    <p className="text-gray-600 mb-4">Browse and book available campus resources.</p>
                    <Link to="/resources" className="text-blue-600 hover:underline">View Resources</Link>
                </div>
                <div className="bg-white p-6 rounded shadow">
                    <h2 className="text-xl font-semibold mb-2">My Bookings</h2>
                    <p className="text-gray-600 mb-4">View and manage your current resource bookings.</p>
                    <Link to="/bookings" className="text-blue-600 hover:underline">View Bookings</Link>
                </div>
                {user?.role === 'ROLE_ADMIN' && (
                    <div className="bg-white p-6 rounded shadow">
                        <h2 className="text-xl font-semibold mb-2">Admin Panel</h2>
                        <p className="text-gray-600 mb-4">Manage resources, users, and all bookings.</p>
                        <Link to="/admin" className="text-blue-600 hover:underline">Go to Admin</Link>
                    </div>
                )}
            </div>
        </div>
    );
};
