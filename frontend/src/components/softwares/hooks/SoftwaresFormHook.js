import { useState } from 'react';
import toast from 'react-hot-toast';
import SoftwaresApiService from '../service/SoftwaresApiService';
import useSoftware from './SoftwareByIdHook';



const useSoftwaresForm = (id, softwaresChangeHandle) => {
    const { software, setSoftware } = useSoftware(id);

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

    const getSoftwareObject = (formData) => {
        const name = formData.name;
        const description = formData.description;
        const startDate = toIsoString(formData.startDate);
        const endDate = toIsoString(formData.endDate);

        return {
            name: name,
            description: description,
            startDate: startDate,
            endDate: endDate,
        };
    };

    const handleChange = (event) => {
        const inputName = event.target.name;
        const inputValue = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
        setSoftware({
            ...software,
            [inputName]: inputValue,
        });
    };

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();
        const body = getSoftwareObject(software);
        if (form.checkValidity()) {
            if (id === undefined) {
                await SoftwaresApiService.create(body);
            } else {
                await SoftwaresApiService.update(id, body);
            }
            if (softwaresChangeHandle) softwaresChangeHandle();
            toast.success('Элемент успешно сохранен', { id: 'SoftwaresTable' });
            return true;
        }
        setValidated(true);
        return false;
    };

    return {
        software,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    };
};

export default useSoftwaresForm;
