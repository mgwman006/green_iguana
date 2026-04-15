import Home from "./components/Home";

import Dashboard from "./components/Dasboard";
import LogIn from "./components/LogIn";
import CourseCatalog from "./components/course/CourseCatalog";
import CourseList from "./components/course/CourseList";
import CourseDetails from "./components/course/CourseDetails";
import EnrollmentPage from "./components/enrollment/EnrollmentPage";
import EnrollmentList from "./components/enrollment/EnrollmentList";

const routes = [
  {
    path: "/",
    Component: Home,
    children: [
      {
        path:"",
        Component: LogIn
      },
      {
        path:"dashboard",
        Component: Dashboard
      },
      {
        path:"courses",
        Component: CourseCatalog,
        children:[
          {
            path: "",
            Component: CourseList
          },
          {
            path: ":courseId",
            Component: CourseDetails
          }
        ]
      },
      {
        path:"enrollments",
        Component: EnrollmentPage,
        children:[
          {
            path: "",
            Component: EnrollmentList
          }
        ]
      }
    ]
  }
  
];

export default routes;
