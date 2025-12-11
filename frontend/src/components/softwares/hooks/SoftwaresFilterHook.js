import { useSearchParams } from 'react-router-dom';
import useTasks from '../../tasks/hooks/TasksHook';


const useNameFilter = () => {
    const filterName = 'name';

    const [searchParams, setSearchParams] = useSearchParams();

    const handleNameFilterChange = (name) => {
        if (name) {
            searchParams.set(filterName, name);
        } else {
            searchParams.delete(filterName);
        }
        setSearchParams(searchParams);
    };

    return {
        currentNameFilter: searchParams.get(filterName) || '',
        handleNameFilterChange,
    };
};

const useDateFilter = () => {
    const startDateFilterName = 'startDate';
    const endDateFilterName = 'endDate';

    const [searchParams, setSearchParams] = useSearchParams();

    const handleDateFilterChange = (startDate, endDate) => {
        if (startDate && endDate) {
            searchParams.set(startDateFilterName, startDate);
            searchParams.set(endDateFilterName, endDate);
        } else if (startDate) {
            searchParams.set(startDateFilterName, startDate);
            searchParams.delete(endDateFilterName);
        } else if (endDate) {
            searchParams.set(endDateFilterName, endDate);
            searchParams.delete(startDateFilterName);
        } else {
            searchParams.delete(startDateFilterName);
            searchParams.delete(endDateFilterName);
        }
        setSearchParams(searchParams);
    };

    return {
        currentStartDateFilter: searchParams.get(startDateFilterName) || '',
        currentEndDateFilter: searchParams.get(endDateFilterName) || '',
        handleDateFilterChange,
    };
};

const useTaskFilter = () => {
    const filterName = 'taskIds';

    const [searchParams, setSearchParams] = useSearchParams();

    const { tasks } = useTasks({size : 40});

    const handleTaskFilterChange = async (taskIds) => {

        console.log(taskIds);
        if (taskIds) {
            searchParams.set(filterName, taskIds);
        } else {
            searchParams.delete(filterName);
        }

        setSearchParams(searchParams);
    };

    return {
        tasks,
        currentTaskFilter: searchParams.get(filterName) || '',
        handleTaskFilterChange,
    };
};

const useResetSearchParams = () => {
    const [, setSearchParams] = useSearchParams();

    const resetSearchParams = () => {
        const newParams = new URLSearchParams();
        setSearchParams(newParams);
    };

    return resetSearchParams;
};

export { useDateFilter, useNameFilter, useResetSearchParams, useTaskFilter };

