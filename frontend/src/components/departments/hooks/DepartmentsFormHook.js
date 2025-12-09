import { useCallback, useState } from 'react';
import toast from 'react-hot-toast';
import DepartmentsApiService from '../service/DepartmentsApiService';
import useDepartment from './DepartmentByIdHook';



const useDepartmentsForm = (id, departmentsChangeHandle) => {

    const {
        department,
        setDepartment,
        allAvailableTasks,
        initialTasks,
        loading,
        error,
    } = useDepartment(id);

    const [validated, setValidated] = useState(false);

    const resetValidity = useCallback(() => {
        setValidated(false);
    }, []);

    const handleChange = useCallback((event) => {
        const { name, value, type } = event.target;

        const newValue = type === 'number' ? Number(value) : value;

        setDepartment(prevDepartment => ({
            ...prevDepartment,
            [name]: newValue,
        }));
    }, [department]);


    const handleSelectedTaskIdsChange = useCallback((newSelectedIds) => {
        setDepartment(prevDepartment => ({
            ...prevDepartment,
            taskIds: newSelectedIds,
        }));
    }, [setDepartment]);

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();

        setValidated(true);

        if (!form.checkValidity()) {
            toast.error('Пожалуйста, заполните все обязательные поля.', { id: 'DepartmentsFormError' });
            return false;
        }

        const payload = {
            name: department.name,
            efficiency: department.efficiency,
            specialty: department.specialty,
            taskIds: department.taskIds,
        };

        if (department.id !== null && department.id !== '') {
            payload.id = department.id;
        }

        try {
            if (payload.id === undefined) {
                await DepartmentsApiService.create(payload);
            } else {
                await DepartmentsApiService.update(payload.id, payload);
            }

            toast.success('Элемент успешно сохранен', { id: 'DepartmentsFormSuccess' });
            if (departmentsChangeHandle) departmentsChangeHandle();

            return true;
        } catch (error) {
            console.error("Ошибка при сохранении:", error);
            toast.error(`Ошибка при сохранении: ${error.message || 'Неизвестная ошибка'}`, { id: 'DepartmentsFormError' });
            return false;
        }
    };


    return {
        department,
        validated,
        handleSubmit,
        handleChange,
        handleSelectedTaskIdsChange,
        allAvailableTasks,
        initialTasks,
        loading,
        error,
        resetValidity,
    };
};

export default useDepartmentsForm;
