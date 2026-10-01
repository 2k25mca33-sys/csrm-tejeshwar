import axios from 'axios';

const api = axios.create({
    baseURL: 'http://localhost:8080/api',
});

// Request Interceptor: Automatically attach JWT
api.interceptors.request.use((config) => {
    const token = localStorage.getItem('token');
    if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

// Response Interceptor: Global Error Handling
api.interceptors.response.use(
    (response) => {
        // Any status code that lies within the range of 2xx causes this function to trigger
        return response;
    },
    (error) => {
        // Any status codes that falls outside the range of 2xx causes this function to trigger
        if (error.response) {
            const status = error.response.status;
            
            switch (status) {
                case 401:
                    console.error('401 Unauthorized - JWT Expired or Invalid');
                    localStorage.removeItem('token');
                    localStorage.removeItem('user');
                    window.location.href = '/login';
                    break;
                case 403:
                    console.error('403 Forbidden - You do not have permission to access this resource');
                    break;
                case 400:
                    console.error('400 Bad Request - Invalid syntax or validation failed');
                    break;
                case 404:
                    console.error('404 Not Found - The requested resource could not be found');
                    break;
                case 409:
                    console.error('409 Conflict - Resource state conflict (e.g., booking overlap)');
                    break;
                case 500:
                    console.error('500 Internal Server Error - An unexpected error occurred on the server');
                    break;
                default:
                    console.error(`Unhandled Error Status: ${status}`);
            }
        } else {
            console.error('Network Error - Failed to communicate with the server');
        }
        
        return Promise.reject(error);
    }
);

export default api;
