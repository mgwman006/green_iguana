// src/store/student/studentActions.ts

import { studentsApi } from "../api/api";
import { StudentAction } from "../store/student/studentReducer";


export const fetchStudentProfile = async (dispatch: any, studentId: number) => {
  dispatch({ type: "FETCH_START" });

  try {
    const profile = await studentsApi.getById(studentId);

    dispatch({
      type: "FETCH_SUCCESS",
      payload: profile,
    });
  } catch (err: any) {
    dispatch({
      type: "FETCH_ERROR",
      payload: err.message || err,
    });
  }
};