import { useEffect, useState } from 'react';
import TasksApiService from '../../tasks/service/TasksApiService';
import DepartmentsApiService from '../service/DepartmentsApiService';


const useDepartment = (id) => {
    const emptyDepartment = {
        id: '',
        name: '',
        efficiency: '',
        specialty: '',
        taskIds: [],
        tasks: [],
    };

    const [initialTasks, setInitialTasks] = useState([]);

    const [department, setDepartment] = useState({ ...emptyDepartment });
    const [allAvailableTasks, setAllAvailableTasks] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchDepartmentData = async (departmentId = null) => {
        setLoading(true);
        setError(null);
        try {

            const fetchedAllTasks = await TasksApiService.getAll("?size=40");
            setAllAvailableTasks(fetchedAllTasks.items ?? []);
            console.log(fetchedAllTasks);
            
            if (departmentId && departmentId > 0) {
                const data = await DepartmentsApiService.get(departmentId);

                setDepartment(data);
                
                setInitialTasks(
                    data.tasks?.map(task => ({
                    value: task?.id || 0,
                    label: task?.description || 'N/A'
                    })) || []
                );
            } else {

                setDepartment({ ...emptyDepartment });
            }
        } catch (err) {
            setError(err);
            console.error("Ошибка при загрузке данных отдела:", err);

            setDepartment({ ...emptyDepartment });
            setAllAvailableTasks([]);
        } finally {
            setLoading(false);
        }
    };


    useEffect(() => {
        fetchDepartmentData(id);
    }, [id]);

    return {
        department,
        setDepartment,
        allAvailableTasks,
        initialTasks,
        loading,
        error,
        refetchDepartmentData: fetchDepartmentData,
    };
};

export default useDepartment;
