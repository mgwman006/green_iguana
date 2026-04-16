import Home from "./components/Home";

import Dashboard from "./components/student/Dashboard";
import LogIn from "./components/LogIn";
import CourseCatalog from "./components/course/CourseCatalog";
import CourseList from "./components/course/CourseList";
import CourseDetails from "./components/course/CourseDetails";
import EnrollmentPage from "./components/enrollment/EnrollmentPage";
import EnrollmentList from "./components/enrollment/EnrollmentList";
import ProfileCard from "./components/student/ProfileCard";

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
      },
      {
        path: "profile",
        Component: ProfileCard
      }
    ]
  }
  
];

export default routes;
