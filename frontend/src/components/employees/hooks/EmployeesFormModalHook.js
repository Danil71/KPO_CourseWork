import { useState } from 'react';
import useModal from '../../modal/ModalHook';
import useEmployeesForm from './EmployeesFormHook';



const useEmployeesFormModal = (employeesChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(0);
    

    const {
        employee,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    } = useEmployeesForm(currentId, employeesChangeHandle);

    const showModalDialog = (id) => {
        setCurrentId(id);
        resetValidity();
        showModal();
    };

    const onClose = () => {
        setCurrentId(-1);
        hideModal();
    };

    const onSubmit = async (event) => {
        if (await handleSubmit(event)) {
            onClose();
        }
    };

    return {
        isFormModalShow: isModalShow,
        isFormValidated: validated,
        showFormModal: showModalDialog,
        currentEmployee: employee,
        handleEmployeeChange: handleChange,
        handleFormSubmit: onSubmit,
        handleFormClose: onClose,
    };
};

export default useEmployeesFormModal;
