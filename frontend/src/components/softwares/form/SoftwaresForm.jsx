import PropTypes from 'prop-types';
import { useState } from 'react';
import Input from '../../input/Input.jsx';
import TextArea from '../../input/TextArea.jsx';

const SoftwaresForm = ({ software, handleChange }) => {

    const [isEndDateInvalid, setIsEndDateInvalid] = useState(false);

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
            <Input name='name' label='Название' value={software.name} onChange={handleChange}
                type='text' required />
            <TextArea
                name='description'
                label='Описание'
                value={software.description}
                onChange={handleChange}
                type='textarea'
                required
            />
            <Input
                name='startDate'
                label='Дата начала'
                value={software.startDate}
                onChange={(e) => {
                    validateDates(e.target.value, software.endDate);
                    handleChange(e);
                }}
                type='datetime-local'
                required
            />
            <Input
                name='endDate'
                label='Дата окончания'
                value={software.endDate}
                onChange={(e) => {
                    validateDates(software.startDate, e.target.value);
                    handleChange(e);
                }}
                type='datetime-local'
                isInvalid={isEndDateInvalid}
                required
            />
        </>
    );
};

SoftwaresForm.propTypes = {
    software: PropTypes.object,
    handleChange: PropTypes.func,
};

export default SoftwaresForm;
