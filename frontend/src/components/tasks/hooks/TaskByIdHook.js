import { useEffect, useState } from 'react';
import TasksApiService from '../service/TasksApiService';

const useTask = (id) => {
    const emptyTask = {
        id: '',
        description: '',
        difficulty: '',
        startDate: '',
        endDate: '',
        hours: '',
        softwareId: ''
    };
    
    const [task, setTask] = useState({ ...emptyTask });

    const getTaskById = async (taskId = undefined) => {
        if (taskId && taskId > 0) {
            const data = await TasksApiService.get(taskId);
            setTask(data);
        } else {
            setTask({ ...emptyTask });
        }
    };

    useEffect(() => {
        getTaskById(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return {
        task,
        setTask
    };
};

export default useTask;
