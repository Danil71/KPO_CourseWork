import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';

const EmployeesTableRow = ({ index, employee, onDelete, onEdit }) => {
    const handleAnchorClick = (event, action) => {
        event.preventDefault();
        action();
    };

    return (
        <tr data-testid={`row-${employee.name}`}>
            <th scope="row">{index + 1}</th>
            <td>{employee.name}</td>
            <td>{employee.speed ? employee.speed.toFixed(1) : '0'}</td> 
            <td>{employee.experience ? employee.experience.toFixed(1) : '0'}</td>
            <td>{employee.departmentId ? employee.departmentName : '-'}</td>
            
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

EmployeesTableRow.propTypes = {
    index: PropTypes.number,
    employee: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
};

export default EmployeesTableRow;