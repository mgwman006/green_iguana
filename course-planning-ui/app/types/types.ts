// Example TypeScript types for the application

export interface Course {
  id: number;
  code: string;
  name: string;
  credits: number;
  hoursPerWeek: number;
  prerequisiteId?: number;
  gradeLevel: {
    min: number;
    max: number;
  };
}

export interface Student {
  id: number;
  firstName: string;
  lastName: string;
  gradeLevel: number;
  email: string;
}

export interface CourseHistory {
  id: number;
  courseId: number;
  courseName: string;
  semesterId: number;
  status: 'passed' | 'failed';
}

export interface StudentProfile extends Student {
  gpa: number;
  creditsEarned: number;
  courseHistory: CourseHistory[];
}

export type StudentState = {
  profile: StudentProfile | null;
  loading: boolean;
  error: string | null;
};

export interface Enrollment {
  id: number;
  studentId: number;
  courseId: number;
  semesterId: number;
  status: 'enrolled' | 'completed' | 'dropped';
}

export interface ValidationError {
  type: 'prerequisite' | 'conflict' | 'max_courses' | 'other';
  message: string;
}

export type ApiResponse<T> = {
  success: boolean;
  data: T;
  message?: string;
};
