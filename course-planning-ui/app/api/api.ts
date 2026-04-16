import axios from 'axios';
import {ApiResponse, CourseDto, Enrollment, SemesterDto, StudentProfile} from '../types/types';

const apiUrl = import.meta.env.VITE_API_URL;

export const apiClient = axios.create({
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
  getAll: async (grade?: number, semesterOrder?: number) => {
    const res = await apiClient.get<ApiResponse<CourseDto[]>>("/courses", {
      params: { grade, semesterOrder },
    });

    return handleResponse(res.data);
  },
  getById: async (id: number) => {
    const res =  await apiClient.get<ApiResponse<CourseDto>>(`/courses/${id}`);
    return handleResponse(res.data);
  }
};

export const studentsApi = {
  getById: async (id: number): Promise<StudentProfile> => {
    const res = await apiClient.get<ApiResponse<StudentProfile>>(`/students/${id}/profile`);
    return handleResponse(res.data);
  },

  getSchedule: (id: number) => apiClient.get(`/students/${id}/schedule`),
};

export const enrollmentsApi = {
  enroll: async (studentId: number, sectionId: number) =>
  {
    const res = await apiClient.post<ApiResponse<Enrollment>>("/enrollments/enroll", {studentId, sectionId });
    return handleResponse(res.data);
  } 
};

export const semesterApi = {
  getActiveSemester: async () =>
  {
    const res = await apiClient.get<ApiResponse<SemesterDto>>("/semesters");
    return handleResponse(res.data);
  }
};

export function handleResponse<T>(response: ApiResponse<T>): T {
  if (!response.success) {
    throw new Error(response.message || "Request failed");
  }

  return response.data;
}
