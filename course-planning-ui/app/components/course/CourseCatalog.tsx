import { Outlet } from "react-router-dom";


export default function CourseCatalog() {
  
  return (
    <div style={{margin:10}}>
        <h1>Course Catalog</h1>
        <Outlet/>
    </div>
  );
}