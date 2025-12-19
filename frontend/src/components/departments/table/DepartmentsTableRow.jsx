import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';

const DepartmentsTableRow = ({
    index, department, onDelete, onEdit,
}) => {
    const handleAnchorClick = (event, action) => {
        event.preventDefault();
        action();
    };

    return (
        <tr data-testid={`row-${department.name}`}>
            <th scope="row">{index}</th>
            <td>{department.name}</td>
            <td>{department.efficiency}</td>
            <td>{department.specialty}</td>
            <td>
                <a href="#" onClick={(event) => handleAnchorClick(event, onEdit)} data-testid="edit-btn">
                    <PencilFill />
                </a>
            </td>
            <td>
                <a href="#" onClick={(event) => handleAnchorClick(event, onDelete)} data-testid="delete-btn">
                    <Trash3 />
                </a>
            </td>
        </tr>
    );
};

DepartmentsTableRow.propTypes = {
    index: PropTypes.number,
    department: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
    onEditInPage: PropTypes.func,
};

export default DepartmentsTableRow;