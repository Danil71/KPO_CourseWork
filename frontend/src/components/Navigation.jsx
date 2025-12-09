import { Container, Nav, Navbar, NavDropdown } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const Navigation = () => {

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
                            <NavDropdown.Item as={Link} to="/admin/employee">Сотрудники</NavDropdown.Item>

                        </NavDropdown>
                    </Nav>
                </Container>
            </Navbar>
        </header>

    );
};

export default Navigation;