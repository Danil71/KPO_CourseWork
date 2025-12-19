import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const DepartmentsTable = ({ children }) => {
    return (
        <Table className='mt-2' striped responsive hover>
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Название отдела</th>
                    <th scope="col">Эффективность</th>
                    <th scope="col">Направление</th>
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

DepartmentsTable.propTypes = {
    children: PropTypes.node,
};

export default DepartmentsTable;
