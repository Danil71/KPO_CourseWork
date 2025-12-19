import { observer } from "mobx-react-lite";
import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { Link, useNavigate } from 'react-router-dom';

import { useContext } from "react";
import StoreContext from './users/StoreContext.jsx';

const Navigation = observer(() => {

    const navigate = useNavigate();
    const { store } = useContext(StoreContext);

    const handleLogout = async () => {
        await store.logout();
        navigate('/', { replace: false });
    };

    return (
        <header className="text-center">
             <Navbar bg="dark" data-bs-theme="dark">
                <Container fluid>
                    <Navbar.Brand>Компания. Управление разработкой ПО</Navbar.Brand>
                    <Nav className="ms-auto">
                        <NavDropdown 
                            title={'Управление'} 
                            data-testid="nav-management-dropdown"
                        >
                            <NavDropdown.Item 
                                as={Link} 
                                to="/admin/software" 
                                data-testid="nav-link-software" 
                            >
                                ПО
                            </NavDropdown.Item>
                            
                            <NavDropdown.Item 
                                as={Link} 
                                to="/admin/task" 
                                data-testid="nav-link-task"
                            >
                                Задачи
                            </NavDropdown.Item>
                            
                            <NavDropdown.Item 
                                as={Link} 
                                to="/admin/department" 
                                data-testid="nav-link-department"
                            >
                                Отделы
                            </NavDropdown.Item>
                            
                            {store.isAdmin && (
                                <>
                                    <NavDropdown.Item 
                                        as={Link} 
                                        to="/admin/user" 
                                        data-testid="nav-link-user"
                                    >
                                        Пользователи
                                    </NavDropdown.Item>
                                    <NavDropdown.Item 
                                        as={Link} 
                                        to="/admin/employee" 
                                        data-testid="nav-link-employee"
                                    >
                                        Сотрудники
                                    </NavDropdown.Item>
                                </>
                            )}
                        </NavDropdown>
                    </Nav>
                    {store.isAuth && (
                        <Nav className="ms-3 d-none d-lg-flex order-lg-3">
                        <NavDropdown 
                            title={store.user.email} 
                            align="end"
                            data-testid="nav-user-dropdown"
                        >
                            <NavDropdown.Item 
                                onClick={handleLogout}
                                data-testid="logout-btn"
                            >
                            Выйти из аккаунта
                            </NavDropdown.Item>
                        </NavDropdown>
                        </Nav>
                    )}
                </Container>
            </Navbar>
        </header>
    );
});

export default Navigation;