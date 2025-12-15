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
                    <Navbar.Brand>Компания. Расчет скорости разработки ПО</Navbar.Brand>
                    <Nav className="ms-auto">
                        <NavDropdown title= {'Управление'}>

                            <NavDropdown.Item as={Link} to="/admin/software">ПО</NavDropdown.Item>
                            <NavDropdown.Item as={Link} to="/admin/task">Задачи</NavDropdown.Item>
                            <NavDropdown.Item as={Link} to="/admin/department">Отделы</NavDropdown.Item>
                            {store.isAdmin && (
                                <>
                                    <NavDropdown.Item as={Link} to="/admin/user">Пользователи</NavDropdown.Item>
                                    <NavDropdown.Item as={Link} to="/admin/employee">Сотрудники</NavDropdown.Item>
                                </>
                            )}
                        </NavDropdown>
                    </Nav>
                    {store.isAuth && (
                        <Nav className="ms-3 d-none d-lg-flex order-lg-3">
                            
                        <NavDropdown title={store.user.email} align="end">
                            <NavDropdown.Item onClick={handleLogout}>
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