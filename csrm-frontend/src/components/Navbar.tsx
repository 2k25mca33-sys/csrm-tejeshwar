import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export const Navbar = () => {
    const { user, logout } = useAuth();

    return (
        <nav className="bg-blue-600 text-white p-4 flex justify-between items-center shadow-md">
            <Link to="/dashboard" className="text-xl font-bold">CSRM System</Link>
            <div>
                {user ? (
                    <div className="flex gap-4 items-center">
                        <span>{user.username} ({user.role.replace('ROLE_', '')})</span>
                        <Link to="/resources" className="hover:underline">Resources</Link>
                        <Link to="/bookings" className="hover:underline">My Bookings</Link>
                        {user.role === 'ROLE_ADMIN' && (
                            <Link to="/admin" className="hover:underline">Admin Panel</Link>
                        )}
                        <button onClick={logout} className="bg-red-500 px-3 py-1 rounded hover:bg-red-600">Logout</button>
                    </div>
                ) : (
                    <div className="flex gap-4">
                        <Link to="/login" className="hover:underline">Login</Link>
                        <Link to="/register" className="hover:underline">Register</Link>
                    </div>
                )}
            </div>
        </nav>
    );
};
