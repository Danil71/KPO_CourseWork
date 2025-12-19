import { useState } from 'react';
import useDepartmentsForm from './DepartmentsFormHook';



const useDepartmentsFormModal = (departmentsChangeHandle) => {
    const [isFormModalShow, setIsFormModalShow] = useState(false);
    const [departmentIdForModal, setDepartmentIdForModal] = useState(0);
    
    const {
        department,
        validated,
        handleSubmit,
        handleChange,
        allAvailableTasks,
        initialTasks,
        handleSelectedTaskIdsChange,
        loading,
        error,
        resetValidity,
    } = useDepartmentsForm(departmentIdForModal, departmentsChangeHandle);

    const showFormModal = (id = null) => {
        setDepartmentIdForModal(id);
        resetValidity();
        setIsFormModalShow(true);
    };

    const handleFormClose = () => {
        setIsFormModalShow(false);
        setDepartmentIdForModal(null);
    };

    const handleFormSubmit = async (event) => {
        const success = await handleSubmit(event);
        if (success) {
            handleFormClose();
        }
    };

    return {
        isFormModalShow,
        isFormValidated: validated,
        showFormModal,
        currentDepartment: department,
        handleDepartmentChange: handleChange,
        handleFormSubmit,
        handleFormClose,
        allAvailableTasks,
        initialTasks,
        handleSelectedTaskIdsChange,
        isFormLoading: loading,
        formError: error,
    };
};

export default useDepartmentsFormModal;
