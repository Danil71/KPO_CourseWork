import { useEffect, useState } from 'react';
import SoftwaresApiService from '../service/SoftwaresApiService';

const useSoftware = (id) => {
    const emptySoftware = {
        id: '',
        name: '',
        description: '',
        startDate: '',
        endDate: ''
    };
    
    const [software, setSoftware] = useState({ ...emptySoftware });

    const getSoftwareById = async (softwareId = undefined) => {
        if (softwareId && softwareId > 0) {
            const data = await SoftwaresApiService.get(softwareId);
            setSoftware(data);
        } else {
            setSoftware({ ...emptySoftware });
        }
    };

    useEffect(() => {
        getSoftwareById(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return {
        software,
        setSoftware
    };
};

export default useSoftware;
