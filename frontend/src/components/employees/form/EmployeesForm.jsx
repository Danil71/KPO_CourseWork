import PropTypes from 'prop-types';
import useDepartments from '../../departments/hooks/DepartmentsHook.js';
import Input from '../../input/Input.jsx';
import Select from '../../input/Select.jsx';

const EmployeesForm = ({ employee, handleChange }) => {
    
    const { departments } = useDepartments();

    return (
        <>
            <Input 
                name='name' 
                label='ФИО' 
                value={employee.name} 
                onChange={handleChange}
                type='text' 
                required 
                data-testid="emp-name"
            />

            <Select values={departments} name='departmentId' label='Отдел' value={employee.departmentId} onChange={handleChange}
                required />
        </>
    );
};

EmployeesForm.propTypes = {
    employee: PropTypes.object,
    handleChange: PropTypes.func,
};

export default EmployeesForm;