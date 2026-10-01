import { useEffect, useState } from 'react';
import api from '../services/api';

export const UserManagement = () => {
    const [users, setUsers] = useState<any[]>([]);

    const fetchUsers = async () => {
        try {
            const res = await api.get('/admin/users');
            setUsers(res.data);
        } catch (error) {
            console.error(error);
        }
    };

    useEffect(() => {
        fetchUsers();
    }, []);

    const approveUser = async (id: number) => {
        try {
            await api.put(`/admin/users/${id}/approve`);
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    const rejectUser = async (id: number) => {
        try {
            await api.put(`/admin/users/${id}/reject`);
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    const updateRole = async (id: number, role: string) => {
        try {
            await api.put(`/admin/users/${id}/role?role=${role}`);
            fetchUsers();
        } catch (error) {
            console.error(error);
        }
    };

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">User Management</h1>
            <div className="bg-white rounded shadow p-4 overflow-x-auto">
                <table className="min-w-full text-left">
                    <thead>
                        <tr className="border-b">
                            <th className="p-2">ID</th>
                            <th className="p-2">Username</th>
                            <th className="p-2">Role</th>
                            <th className="p-2">Status</th>
                            <th className="p-2">Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {users.map(u => (
                            <tr key={u.id} className="border-b hover:bg-gray-50">
                                <td className="p-2">{u.id}</td>
                                <td className="p-2">{u.username}</td>
                                <td className="p-2">
                                    <select 
                                        className="border p-1 rounded"
                                        value={u.role}
                                        onChange={(e) => updateRole(u.id, e.target.value)}
                                    >
                                        <option value="ROLE_STUDENT">Student</option>
                                        <option value="ROLE_FACULTY">Faculty</option>
                                        <option value="ROLE_ADMIN">Admin</option>
                                    </select>
                                </td>
                                <td className="p-2">
                                    <span className={`px-2 py-1 rounded text-sm ${u.status === 'APPROVED' ? 'bg-green-100 text-green-800' : u.status === 'PENDING' ? 'bg-yellow-100 text-yellow-800' : 'bg-red-100 text-red-800'}`}>
                                        {u.status}
                                    </span>
                                </td>
                                <td className="p-2 space-x-2">
                                    {u.status === 'PENDING' && (
                                        <>
                                            <button onClick={() => approveUser(u.id)} className="text-green-600 font-semibold hover:underline">Approve</button>
                                            <button onClick={() => rejectUser(u.id)} className="text-red-600 font-semibold hover:underline">Reject</button>
                                        </>
                                    )}
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </table>
            </div>
        </div>
    );
};
