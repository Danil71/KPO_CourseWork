import { useEffect, useState } from 'react';
import EmployeesApiService from '../service/EmployeesApiService';

const useEmployee = (id) => {
    const emptyEmployee = {
        id: '',
        name: '',
        speed: '',
        experience: '',
        departmentId: ''
    };
    
    const [employee, setEmployee] = useState({ ...emptyEmployee });

    const getEmployeeById = async (employeeId = undefined) => {
        if (employeeId && employeeId > 0) {
            const data = await EmployeesApiService.get(employeeId);
            setEmployee(data);
            console.log(data);
        } else {
            setEmployee({ ...emptyEmployee });
        }
    };

    useEffect(() => {
        getEmployeeById(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return {
        employee,
        setEmployee
    };
};

export default useEmployee;
