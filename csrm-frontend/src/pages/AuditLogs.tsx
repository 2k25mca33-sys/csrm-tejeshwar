import { useEffect, useState } from 'react';
import api from '../services/api';

export const AuditLogs = () => {
    const [logs, setLogs] = useState<any[]>([]);

    useEffect(() => {
        const fetchLogs = async () => {
            try {
                const res = await api.get('/audit');
                setLogs(res.data);
            } catch (error) {
                console.error(error);
            }
        };
        fetchLogs();
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">System Audit Logs</h1>
            
            <div className="bg-white rounded shadow p-4 overflow-x-auto max-h-96">
                <table className="min-w-full text-left text-sm">
                    <thead>
                        <tr className="border-b bg-gray-100">
                            <th className="p-2">Timestamp</th>
                            <th className="p-2">User</th>
                            <th className="p-2">Action</th>
                            <th className="p-2">Details</th>
                        </tr>
                    </thead>
                    <tbody>
                        {logs.map(log => (
                            <tr key={log.id} className="border-b hover:bg-gray-50">
                                <td className="p-2 whitespace-nowrap">{new Date(log.timestamp).toLocaleString()}</td>
                                <td className="p-2">{log.user?.username || 'SYSTEM'}</td>
                                <td className="p-2 font-mono">{log.action}</td>
                                <td className="p-2 text-gray-600">{log.details}</td>
                            </tr>
                        ))}
                    </tbody>
                </table>
                {logs.length === 0 && <p className="text-center p-4 text-gray-500">No audit logs found.</p>}
            </div>
        </div>
    );
};
