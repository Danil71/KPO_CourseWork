import { useState } from 'react';
import toast from 'react-hot-toast';
import TasksApiService from '../service/TasksApiService';
import useTask from './TaskByIdHook';



const useTasksForm = (id, tasksChangeHandle) => {
    const { task, setTask } = useTask(id);

    const [validated, setValidated] = useState(false);

    const resetValidity = () => {
        setValidated(false);
    };

    const toIsoString = (date) => {
    if (!date) 
      return '';
    if (typeof date === 'string') {
      const d = new Date(date);
      if (!isNaN(d)) return d.toISOString();
      return date; 
    }
    if (date instanceof Date) {
      return date.toISOString();
    }
    return '';
  };

    const getTaskObject = (formData) => {
        const description = formData.description;
        const difficulty = formData.difficulty;
        const startDate = toIsoString(formData.startDate);
        const endDate = toIsoString(formData.endDate);
        const hours = formData.hours;
        const softwareId = parseInt(formData.softwareId, 10);

        return {
            description: description,
            difficulty: difficulty,
            startDate: startDate,
            endDate: endDate,
            hours: hours,
            softwareId: softwareId
        };
    };

    const handleChange = (event) => {
        const inputName = event.target.name;
        const inputValue = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
        setTask({
            ...task,
            [inputName]: inputValue,
        });
    };

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();
        const body = getTaskObject(task);
        if (form.checkValidity()) {
            if (id === undefined) {
                await TasksApiService.create(body);
            } else {
                await TasksApiService.update(id, body);
            }
            if (tasksChangeHandle) tasksChangeHandle();
            toast.success('Элемент успешно сохранен', { id: 'TasksTable' });
            return true;
        }
        setValidated(true);
        return false;
    };

    return {
        task,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    };
};

export default useTasksForm;
