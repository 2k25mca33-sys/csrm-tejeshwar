import { useEffect, useState } from 'react';
import api from '../services/api';

export const Reports = () => {
    const [utilization, setUtilization] = useState<any>(null);
    const [userActivity, setUserActivity] = useState<any[]>([]);

    useEffect(() => {
        const fetchReports = async () => {
            try {
                const utilRes = await api.get('/admin/reports/utilization');
                setUtilization(utilRes.data);
                
                const activityRes = await api.get('/admin/reports/user-activity');
                setUserActivity(activityRes.data);
            } catch (error) {
                console.error(error);
            }
        };
        fetchReports();
    }, []);

    return (
        <div className="p-8">
            <h1 className="text-2xl font-bold mb-6">Reports & Analytics</h1>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 mb-8">
                <div className="bg-white p-6 rounded shadow">
                    <h2 className="text-xl font-semibold mb-4">System Utilization</h2>
                    {utilization && (
                        <div className="space-y-2 text-lg">
                            <p><strong>Total Resources:</strong> {utilization.totalResources}</p>
                            <p><strong>Total Bookings:</strong> {utilization.totalBookings}</p>
                            <p><strong>Utilization Rate:</strong> {(utilization.utilizationRate * 100).toFixed(1)}%</p>
                        </div>
                    )}
                </div>

                <div className="bg-white p-6 rounded shadow overflow-y-auto max-h-64">
                    <h2 className="text-xl font-semibold mb-4">User Activity</h2>
                    <table className="min-w-full text-left">
                        <thead>
                            <tr className="border-b">
                                <th className="pb-2">User</th>
                                <th className="pb-2">Bookings Count</th>
                            </tr>
                        </thead>
                        <tbody>
                            {userActivity.map((activity, i) => (
                                <tr key={i} className="border-b hover:bg-gray-50">
                                    <td className="py-2">{activity[0]}</td>
                                    <td className="py-2">{activity[1]}</td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    );
};
