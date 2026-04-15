import { StudentAction, StudentState } from "../../types/types";


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

    case "UPDATE_ENROLLMENTS":
      return {
      ...state,
        profile: state.profile
          ? {
              ...state.profile,
              enrollments: action.payload
            }:null
      };

    case "ADD_ENROLLMENT":
      if (!state.profile) return state;

      return {
        ...state,
        profile: {
          ...state.profile,
          enrollments: [
            ...(state.profile.enrollments || []),
            action.payload
          ]
        }
      };
    
    default:
      return state;
  }
};