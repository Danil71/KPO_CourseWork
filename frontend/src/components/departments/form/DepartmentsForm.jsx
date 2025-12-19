import PropTypes from 'prop-types';
import Input from '../../input/Input.jsx';
import MultiSelect from '../../input/MultiSelect.jsx';

const DepartmentsForm = ({ 
    department, 
    handleChange, 
    allAvailableTasks, 
    initialTasks, 
    handleSelectedTaskIdsChange 
}) => {
    return (
        <>
            <Input 
                name='name' 
                label='Название' 
                value={department.name} 
                onChange={handleChange}
                type='text' 
                required 
                data-testid="dept-name"
            />
            <Input 
                name='specialty' 
                label='Специальность (код)' 
                value={department.specialty} 
                onChange={handleChange}
                type='number' 
                required 
                data-testid="dept-specialty"
            />
            <Input 
                name='efficiency' 
                label='Эффективность' 
                value={department.efficiency} 
                onChange={handleChange}
                type='number' 
                required 
                data-testid="dept-efficiency"
            />
            <MultiSelect
                name="taskSelection"
                label="Связанные задачи:"
                options={allAvailableTasks ?? []}
                selectedValues={initialTasks ?? []}
                onChange={handleSelectedTaskIdsChange}
            />
        </>
    );
};

DepartmentsForm.propTypes = {
    department: PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
        name: PropTypes.string,
        efficiency: PropTypes.oneOfType([PropTypes.number, PropTypes.string]),
        specialty: PropTypes.string,
        taskIds: PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])), // Теперь ожидаем taskIds
        tasks: PropTypes.arrayOf(PropTypes.shape({
            id: PropTypes.number.isRequired,
            description: PropTypes.string.isRequired, // Важно: у задачи должно быть поле description
            // ... другие поля Task DTO
        })),
    }).isRequired,
    handleChange: PropTypes.func.isRequired,
    allAvailableTasks: PropTypes.arrayOf(PropTypes.shape({
        id: PropTypes.oneOfType([PropTypes.number, PropTypes.string]).isRequired,
        description: PropTypes.string.isRequired,
    })).isRequired,
    selectedTaskIds: PropTypes.arrayOf(PropTypes.oneOfType([PropTypes.number, PropTypes.string])).isRequired,
    handleSelectedTaskIdsChange: PropTypes.func.isRequired,
};

export default DepartmentsForm;