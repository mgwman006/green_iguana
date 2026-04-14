import Home from "./components/Home";

import Dashboard from "./components/Dasboard";
import LogIn from "./components/LogIn";

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
      }
      // {
      //   path:"bookings",
      //   Component: BookingsDashboard,
      //   children : [
      //     {
      //       path: "",
      //       Component: LogIn
      //     },
      //     {
      //       path: ":bookingId",
      //       Component: BookingDetails
      //     }
      //   ]
      // }
    ]
  }
  
];

export default routes;
