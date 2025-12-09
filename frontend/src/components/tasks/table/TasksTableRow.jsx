import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';
import formatDateTime from '../../utils/Formatter';

const TasksTableRow = ({
    index, task, onDelete, onEdit,
}) => {
    const handleAnchorClick = (event, action) => {
        event.preventDefault();
        action();
    };

    return (
        <tr>
            <th scope="row">{index + 1}</th>
            <td>{task.description}</td>
            <td>{task.difficulty}</td>
            <td>{formatDateTime(task.startDate)}</td>
            <td>{formatDateTime(task.endDate)}</td>
            <td>{task.hours}</td>
            <td>{task.softwareName}</td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onEdit)}><PencilFill /></a></td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onDelete)}><Trash3 /></a></td>
        </tr>
    );
};

TasksTableRow.propTypes = {
    index: PropTypes.number,
    task: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
    onEditInPage: PropTypes.func,
};

export default TasksTableRow;
