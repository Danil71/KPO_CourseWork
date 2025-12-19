import { useState } from 'react';
import toast from 'react-hot-toast';
import useModal from '../../modal/ModalHook';
import SoftwaresApiService from '../service/SoftwaresApiService';

const useSoftwaresDeleteModal = (softwaresChangeHandle) => {
    const { isModalShow, showModal, hideModal } = useModal();
    const [currentId, setCurrentId] = useState(0);

    const showModalDialog = (id) => {
        showModal();
        setCurrentId(id);
    };

    const onClose = () => {
        hideModal();
    };

    const onDelete = async () => {
        await SoftwaresApiService.delete(currentId);
        softwaresChangeHandle();
        toast.success('Элемент успешно удален', { id: 'SoftwaresTable' });
        onClose();
    };

    return {
        isDeleteModalShow: isModalShow,
        showDeleteModal: showModalDialog,
        handleDeleteConfirm: onDelete,
        handleDeleteCancel: onClose,
    };
};

export default useSoftwaresDeleteModal;
