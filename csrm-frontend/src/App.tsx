import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import { Navbar } from './components/Navbar';
import { Login } from './pages/Login';
import { Register } from './pages/Register';
import { Dashboard } from './pages/Dashboard';
import { Resources } from './pages/Resources';
import { Bookings } from './pages/Bookings';
import { ProtectedRoute } from './components/ProtectedRoute';

import { UserManagement } from './pages/UserManagement';
import { Reports } from './pages/Reports';
import { AuditLogs } from './pages/AuditLogs';
import { Notifications } from './pages/Notifications';
import { CalendarView } from './pages/CalendarView';
import { ServiceRequests } from './pages/ServiceRequests';

function App() {
  return (
    <Router>
      <AuthProvider>
        <div className="min-h-screen bg-gray-50">
          <Navbar />
          <Routes>
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            
            <Route element={<ProtectedRoute />}>
              <Route path="/dashboard" element={<Dashboard />} />
              <Route path="/resources" element={<Resources />} />
              <Route path="/bookings" element={<Bookings />} />
              <Route path="/calendar" element={<CalendarView />} />
              <Route path="/notifications" element={<Notifications />} />
              <Route path="/services" element={<ServiceRequests />} />
              
              <Route path="/admin" element={<Navigate to="/admin/users" replace />} />
              <Route path="/admin/users" element={<UserManagement />} />
              <Route path="/admin/reports" element={<Reports />} />
              <Route path="/admin/audit" element={<AuditLogs />} />

              <Route path="/" element={<Navigate to="/dashboard" replace />} />
            </Route>
          </Routes>
        </div>
      </AuthProvider>
    </Router>
  );
}

export default App;
