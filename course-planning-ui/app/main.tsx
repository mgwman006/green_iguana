import React from "react";
import ReactDOM from "react-dom/client";
import App from "./App";
import { StudentProvider } from "./store/student/StudentContext";

const rootElement = document.getElementById("root");

if (!rootElement) {
  throw new Error('Failed to find the root element');
}

const root = ReactDOM.createRoot(rootElement);

root.render(
  <React.StrictMode>
    <StudentProvider>
      <App />
    </StudentProvider>
  </React.StrictMode>
);
