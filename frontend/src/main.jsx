import 'bootstrap/dist/css/bootstrap.min.css';
import ReactDOM from 'react-dom/client';
import { RouterProvider, createBrowserRouter } from 'react-router-dom';
import App from './App.jsx';
import { StoreProvider } from './components/users/StoreContext.jsx';
import './index.css';
import DepartmentsPage from './pages/DepartmentsPage.jsx';
import EmployeesPage from './pages/EmployeesPage.jsx';
import ErrorPage from './pages/ErrorPage.jsx';
import Homepage from './pages/Homepage.jsx';
import SoftwaresPage from './pages/SoftwaresPage.jsx';
import TasksPage from './pages/TasksPage.jsx';
import UsersPage from './pages/UsersPage.jsx';


const routes = [
  {
    index: true,
    path: '/',
    element: <Homepage />
  },
  {
    path: '/admin/software',
    element: <SoftwaresPage />
  },
  {
    path: '/admin/task',
    element: <TasksPage />
  },
  {
    path: '/admin/employee',
    element: <EmployeesPage />
  },
  {
    path: '/admin/department',
    element: <DepartmentsPage />
  },
  {
    path: '/admin/user',
    element: <UsersPage />
  },
];

const router = createBrowserRouter([
  {
    path: '/',
    element: <App routes={routes} />,
    children: routes,
    errorElement: <ErrorPage />,
  },
]);

ReactDOM.createRoot(document.getElementById('root')).render(
  <StoreProvider>
    <RouterProvider router={router} />
  </StoreProvider>
);
