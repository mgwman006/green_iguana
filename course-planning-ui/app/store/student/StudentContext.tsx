import React, {createContext, useContext, useEffect, useMemo, useReducer} from "react";
import { studentReducer } from "./studentReducer";
import { StudentState } from "../../types/types";
import { STUDENT_STORAGE_KEY } from "../../utilities/constant";

type StudentContextType = {
  state: StudentState;
  dispatch: React.Dispatch<any>;
};

const StudentContext = createContext<StudentContextType | undefined>(undefined);

//load initial state from local storage
const loadInitialState = (): StudentState => {
  try {
    const stored = localStorage.getItem(STUDENT_STORAGE_KEY);
    if (stored) {
      const parsed = JSON.parse(stored);
      return {
        profile: parsed.profile,
        loading: false,
        error: null,
      };
    }
  } catch (e) {
    console.error("Failed to load state", e);
  }

  return {
    profile: null,
    loading: false,
    error: null,
  };
};

export const StudentProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(studentReducer, undefined,loadInitialState);

  useEffect(() => {
    try {
      if(state.profile)
      {
        localStorage.setItem(STUDENT_STORAGE_KEY, JSON.stringify(state));
      }
    } catch (e) {
      console.error("Failed to save state", e);
    }
  }, [state.profile]);

  const value = useMemo(() => {
    return { state, dispatch };
  }, [state, dispatch]);

  return (
    <StudentContext.Provider value={value}>
      {children}
    </StudentContext.Provider>
  );
};

export const useStudent = () => {
  const context = useContext(StudentContext);
  if (!context) {
    throw new Error("useStudent must be used within StudentProvider");
  }
  return context;
};