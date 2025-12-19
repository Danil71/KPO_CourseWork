import { useEffect, useState } from 'react';
import SoftwaresApiService from '../service/SoftwaresApiService';

const useSoftwares = ({page, nameFilter, taskFilter, startDateFilter, endDateFilter}) => {
    const [softwares, setSoftwares] = useState([]);
    const [totalPages, setTotalPages] = useState(0);
    const [softwaresRefresh, setSoftwaresRefresh] = useState(false);
    const handleSoftwaresChange = () => setSoftwaresRefresh(!softwaresRefresh);
    const clearFilters = () => {nameFilter, taskFilter, startDateFilter, endDateFilter = null;}

    const getSoftwares = async () => {
        const params = new URLSearchParams();

        if (page) {
            params.append('page', page);
        }
        if (nameFilter) {
            params.append('searchInfo', nameFilter);
        }

        if (taskFilter) {
                params.append('taskIds', taskFilter);
        }

        if (startDateFilter) {
            params.append('startDateFrom', startDateFilter);
        }
        if (endDateFilter) {
            params.append('endDateTo', endDateFilter);
        }

        const queryString = params.toString();
        const expand = queryString ? `?${queryString}` : '';

        console.log("Сформированный URL-запрос:", expand);

        const data = await SoftwaresApiService.getAll(expand);
        setSoftwares(data.items ?? []);
        setTotalPages(data.totalPages ?? 0);
    };

    useEffect(() => {
        getSoftwares();
    }, [softwaresRefresh, page, nameFilter, taskFilter, startDateFilter, endDateFilter]);

    return {
        softwares,
        totalPages,
        handleSoftwaresChange,
        clearFilters
    };
};

export default useSoftwares;
