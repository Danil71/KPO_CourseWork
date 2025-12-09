import PropTypes from 'prop-types';
import { Table } from 'react-bootstrap';

const SoftwaresTable = ({ children }) => {
    return (
        <Table className='mt-2' striped responsive hover>
            <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Название ПО</th>
                    <th scope="col">Описание</th>
                    <th scope="col">Дата начала разработки</th>
                    <th scope="col">Дата конца разработки</th>
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

SoftwaresTable.propTypes = {
    children: PropTypes.node,
};

export default SoftwaresTable;
