import { StudentProfile, StudentState } from "../../types/types";

export type StudentAction =
  | { type: "FETCH_START" }
  | { type: "FETCH_SUCCESS"; payload: StudentProfile }
  | { type: "FETCH_ERROR"; payload: string }
  | { type: "LOGOUT" };

export const studentReducer = (state: StudentState, action: StudentAction): StudentState => {
  switch (action.type) {
    case "FETCH_START":
      return { ...state, loading: true, error: null };

    case "FETCH_SUCCESS":
      return { profile: action.payload, loading: false, error: null };

    case "FETCH_ERROR":
      return { profile: null, loading: false, error: action.payload };

    case "LOGOUT":
      return { profile: null, loading: false, error: null };

    default:
      return state;
  }
};