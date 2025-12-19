import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const EmployeesTable = ({ children }) => {
    return (
        <Table className='mt-2' striped responsive hover>
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Имя сотрудника</th>
                    <th scope="col">Скорость</th>
                    <th scope="col">Опыт</th>
                    <th scope="col">Отдел</th>
                    <th scope="col" />
                    <th scope="col" />
                </tr>
            </thead>
            <tbody>
                {children}
            </tbody >
        </Table >
    );
};

EmployeesTable.propTypes = {
    children: PropTypes.node,
};

export default EmployeesTable;
