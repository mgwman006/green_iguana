import Home from "./components/Home";

import Dashboard from "./components/Dasboard";
import LogIn from "./components/LogIn";
import CourseCatalog from "./components/course/CourseCatalog";
import CourseList from "./components/course/CourseList";
import CourseDetails from "./components/course/CourseDetails";

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
      }
    ]
  }
  
];

export default routes;
