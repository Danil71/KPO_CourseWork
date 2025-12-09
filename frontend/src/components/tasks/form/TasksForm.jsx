import PropTypes from 'prop-types';
import { useState } from 'react';
import Input from '../../input/Input.jsx';
import Select from '../../input/Select.jsx';
import TextArea from '../../input/TextArea.jsx';
import useSoftwares from '../../softwares/hooks/SoftwaresHook.js';

const TasksForm = ({ task, handleChange }) => {

    const [isEndDateInvalid, setIsEndDateInvalid] = useState(false);

    const { softwares } = useSoftwares(40);

    const validateDates = (start, end) => {
        if (start && end) {
            const isInvalid = new Date(end) < new Date(start);
            setIsEndDateInvalid(isInvalid);
        } else {
            setIsEndDateInvalid(false);
        }
    };

    return (
        <>
            <TextArea
                name='description'
                label='Описание'
                value={task.description}
                onChange={handleChange}
                type='textarea'
                required
            />
            <Input name='difficulty' label='Сложность' value={task.difficulty} onChange={handleChange}
                type='number' required />
            <Input
                name='startDate'
                label='Дата начала'
                value={task.startDate}
                onChange={(e) => {
                    validateDates(e.target.value, task.endDate);
                    handleChange(e);
                }}
                type='datetime-local'
                required
            />
            <Input
                name='endDate'
                label='Дата окончания'
                value={task.endDate}
                onChange={(e) => {
                    validateDates(task.startDate, e.target.value);
                    handleChange(e);
                }}
                type='datetime-local'
                isInvalid={isEndDateInvalid}
                required
            />
            <Input name='hours' label='Часы' value={task.hours} onChange={handleChange}
                type='number' required />

            <Select values={softwares} name='softwareId' label='ПО' value={task.softwareId} onChange={handleChange}
                required />
        </>
    );
};

TasksForm.propTypes = {
    task: PropTypes.object,
    handleChange: PropTypes.func,
};

export default TasksForm;
