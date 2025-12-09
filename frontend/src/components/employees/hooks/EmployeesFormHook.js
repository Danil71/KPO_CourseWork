import { useState } from 'react';
import toast from 'react-hot-toast';
import EmployeesApiService from '../service/EmployeesApiService';
import useEmployee from './EmployeeByIdHook';



const useEmployeesForm = (id, employeesChangeHandle) => {
    const { employee, setEmployee } = useEmployee(id);

    const [validated, setValidated] = useState(false);

    const resetValidity = () => {
        setValidated(false);
    };

    const getEmployeeObject = (formData) => {
        const name = formData.name;
        const description = formData.description;
        const speed = formData.speed;
        const experience = formData.experience;
        const departmentId = formData.departmentId;

        return {
            name: name,
            description: description,
            speed: speed,
            experience: experience,
            departmentId: departmentId
        };
    };

    const handleChange = (event) => {
        const inputName = event.target.name;
        const inputValue = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
        setEmployee({
            ...employee,
            [inputName]: inputValue,
        });
    };

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();
        const body = getEmployeeObject(employee);
        if (form.checkValidity()) {
            if (id === undefined) {
                await EmployeesApiService.create(body);
            } else {
                await EmployeesApiService.update(id, body);
            }
            if (employeesChangeHandle) employeesChangeHandle();
            toast.success('Элемент успешно сохранен', { id: 'EmployeesTable' });
            return true;
        }
        setValidated(true);
        return false;
    };

    return {
        employee,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    };
};

export default useEmployeesForm;
