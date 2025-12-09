import { useEffect, useState } from 'react';
import TasksApiService from '../service/TasksApiService';

const useTasks = ({page, size}) => {
    const [tasks, setTasks] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [tasksRefresh, setTasksRefresh] = useState(false);
    const handleTasksChange = () => setTasksRefresh(!tasksRefresh);

    const getTasks = async () => {
        let expand = `?`;
        if (page) expand = `${expand}page=${page}`;
        if (size) expand = `${expand}size=${size}`;
        const data = await TasksApiService.getAll(expand);
        setTasks(data.items ?? []);
        setTotalPages(data.totalPages ?? 0);
        console.log(data);
    };

    useEffect(() => {
        getTasks();
    }, [tasksRefresh, page]);

    return {
        tasks,
        totalPages,
        handleTasksChange
    };
};

export default useTasks;
