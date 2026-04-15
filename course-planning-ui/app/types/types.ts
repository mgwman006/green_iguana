
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

export interface SectionDto {
  id: number;
  teacherName: string;
  classroomName: string;
  capacity: number;
  enrolledCount: number;
  availableSeats: number;
  courseName : string;
  timeSlots: string[];
}

export interface CourseDto {
  id: number;
  code: string;
  name: string;
  description: string;
  credits: number;
  hoursPerWeek: number;

  specializationId: number;

  prerequisiteId: number | null;
  prerequisiteName: string | null;

  courseType: "core" | "elective";

  gradeLevelMin: number;
  gradeLevelMax: number;

  semesterOrder: number;
  sections: SectionDto[];

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
  enrollments: Enrollment[]
}

export type StudentState = {
  profile: StudentProfile | null;
  loading: boolean;
  error: string | null;
};

export interface Enrollment {
  id: number;
  section: SectionDto;
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

export type StudentAction =
  | { type: "FETCH_START" }
  | { type: "FETCH_SUCCESS"; payload: StudentProfile }
  | { type: "FETCH_ERROR"; payload: string }
  | { type: "LOGOUT" }
  | { type: "UPDATE_ENROLLMENTS"; payload: Enrollment[]}
  | { type: "ADD_ENROLLMENT"; payload: Enrollment };
