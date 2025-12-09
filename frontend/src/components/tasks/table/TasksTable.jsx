import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const TasksTable = ({ children }) => {
    return (
        <Table className='mt-2' striped responsive hover>
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Описание</th>
                    <th scope="col">Сложность</th>
                    <th scope="col">Дата начала разработки</th>
                    <th scope="col">Дата конца разработки</th>
                    <th scope="col">Часы</th>
                    <th scope="col">ПО</th>
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

TasksTable.propTypes = {
    children: PropTypes.node,
};

export default TasksTable;
