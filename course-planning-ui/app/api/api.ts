import axios from 'axios';
import { ApiResponse, StudentProfile } from '../types/types';

const apiUrl = import.meta.env.VITE_API_URL;

export const apiClient = axios.create({
  baseURL: `${apiUrl}/api/v1`, 
  headers: {
    'Content-Type': 'application/json',
  } ,
});

export const puplicApi = axios.create({
  baseURL: `${apiUrl}/api/v1`, 
  headers: {
    'Content-Type': 'application/json',
  } ,
});

export const privateApi = axios.create({
  baseURL: `${apiUrl}/api/v1`, 
  headers: {
    'Content-Type': 'application/json',
  } ,
});

apiClient.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response) {
      return Promise.reject(
        error.response.data?.message || "Server error occurred"
      );
    }

    if (error.request) {
      return Promise.reject("No response from server");
    }

    return Promise.reject("Unexpected error");
  }
);

export const coursesApi = {
  getAll: () => apiClient.get('/courses'),
  getById: (id: number) => apiClient.get(`/courses/${id}`),
};

export const studentsApi = {
  getById: async (id: number): Promise<StudentProfile> => {
    const res = await apiClient.get<ApiResponse<StudentProfile>>(`/students/${id}/profile`);
    return handleResponse(res.data);
  },
  
  getSchedule: (id: number) => apiClient.get(`/students/${id}/schedule`),
};

export const enrollmentsApi = {
  enroll: (studentId: number, sectionId: number) =>
    apiClient.post('/enrollments', { studentId, sectionId }),
};

export function handleResponse<T>(response: ApiResponse<T>): T {
  if (!response.success) {
    throw new Error(response.message || "Request failed");
  }

  return response.data;
}
