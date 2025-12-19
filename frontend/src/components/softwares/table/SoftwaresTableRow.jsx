import PropTypes from 'prop-types';
import { PencilFill, Trash3 } from 'react-bootstrap-icons';
import formatDateTime from '../../utils/Formatter';

const SoftwaresTableRow = ({
    index, software, onDelete, onEdit,
}) => {
    const handleAnchorClick = (event, action) => {
        event.preventDefault();
        action();
    };

    return (
        <tr data-testid={`row-${software.name}`}>
            <th scope="row">{index + 1}</th>
            <td>{software.name}</td>
            <td>{software.description}</td>
            <td>{formatDateTime(software.startDate)}</td>
            <td>{formatDateTime(software.endDate)}</td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onEdit)} data-testid="edit-btn"><PencilFill /></a></td>
            <td><a href="#" onClick={(event) => handleAnchorClick(event, onDelete)} data-testid="delete-btn"><Trash3 /></a></td>
        </tr>
    );
};

SoftwaresTableRow.propTypes = {
    index: PropTypes.number,
    software: PropTypes.object,
    onDelete: PropTypes.func,
    onEdit: PropTypes.func,
    onEditInPage: PropTypes.func,
};

export default SoftwaresTableRow;
