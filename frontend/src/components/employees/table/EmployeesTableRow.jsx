import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';


const EmployeesTableRow = ({
    index, employee, onDelete, onEdit,
}) => {
    const handleAnchorClick = (event, action) => {
        event.preventDefault();
        action();
    };

    return (
        <tr>
            <th scope="row">{index + 1}</th>
            <td>{employee.name}</td>
            <td>{employee.speed}</td>
            <td>{employee.experience}</td>
            <td>{employee.departmentName}</td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onEdit)}><PencilFill /></a></td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onDelete)}><Trash3 /></a></td>
        </tr>
    );
};

EmployeesTableRow.propTypes = {
    index: PropTypes.number,
    employee: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
    onEditInPage: PropTypes.func,
};

export default EmployeesTableRow;
