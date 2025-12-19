import { useState } from 'react';
import useModal from '../../modal/ModalHook';
import useSoftwaresForm from './SoftwaresFormHook';



const useSoftwaresFormModal = (softwaresChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(0);
    

    const {
        software,
        validated,
        handleSubmit,
        handleChange,
        resetValidity,
    } = useSoftwaresForm(currentId, softwaresChangeHandle);

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
        currentSoftware: software,
        handleSoftwareChange: handleChange,
        handleFormSubmit: onSubmit,
        handleFormClose: onClose,
    };
};

export default useSoftwaresFormModal;
