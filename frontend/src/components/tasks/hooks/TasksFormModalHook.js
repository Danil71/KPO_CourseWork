import { useState } from 'react';
import useModal from '../../modal/ModalHook';
import useTasksForm from './TasksFormHook';



const useTasksFormModal = (tasksChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(0);
    

    const {
        task,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    } = useTasksForm(currentId, tasksChangeHandle);

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
        currentTask: task,
        handleTaskChange: handleChange,
        handleFormSubmit: onSubmit,
        handleFormClose: onClose,
    };
};

export default useTasksFormModal;
