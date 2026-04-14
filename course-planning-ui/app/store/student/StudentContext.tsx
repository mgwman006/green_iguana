import React, { createContext, useContext, useReducer } from "react";
import { studentReducer } from "./studentReducer";
import { StudentState } from "../../types/types";

type StudentContextType = {
  state: StudentState;
  dispatch: React.Dispatch<any>;
};

const StudentContext = createContext<StudentContextType | undefined>(undefined);

const initialState: StudentState = {
  profile: null,
  loading: false,
  error: null,
};

export const StudentProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [state, dispatch] = useReducer(studentReducer, initialState);

  return (
    <StudentContext.Provider value={{ state, dispatch }}>
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