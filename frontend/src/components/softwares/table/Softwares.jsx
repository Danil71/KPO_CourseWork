import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import PaginationComponent from '../../pagination/Pagination.jsx';
import usePagination from '../../pagination/PaginationHook.js';
import SoftwaresForm from '../form/SoftwaresForm.jsx';
import useSoftwaresDeleteModal from '../hooks/SoftwaresDeleteModalHook.js';
import useSoftwaresFormModal from '../hooks/SoftwaresFormModalHook.js';
import useSoftwares from '../hooks/SoftwaresHook.js';
import SoftwaresTable from './SoftwaresTable.jsx';
import SoftwaresTableRow from './SoftwaresTableRow.jsx';

const Softwares = () => {

    const {currentPage, handlePageChange} = usePagination();

    const { softwares, handleSoftwaresChange, totalPages} = useSoftwares({page : currentPage});

    const {
        isDeleteModalShow,
        showDeleteModal,
        handleDeleteConfirm,
        handleDeleteCancel,
    } = useSoftwaresDeleteModal(handleSoftwaresChange);

    const {
        isFormModalShow,
        isFormValidated,
        showFormModal,
        currentSoftware,
        handleSoftwareChange,
        handleFormSubmit,
        handleFormClose,
    } = useSoftwaresFormModal(handleSoftwaresChange);

    return (
        <>
            <SoftwaresTable>
                {
                    softwares.map((software, index) =>
                        <SoftwaresTableRow key={software.id}
                            index={index} software={software}
                            onDelete={() => showDeleteModal(software.id)}
                            onEdit={() => showFormModal(software.id)}
                        />)
                }
            </SoftwaresTable>
            <div className="d-flex justify-content-center">
                <Button variant='primary' className="fw-bold px-5 mb-5" onClick={() => showFormModal()}>
                    Добавить ПО
                </Button>
            </div>
            <PaginationComponent totalPages={totalPages} currentPage={currentPage} handlePageChange={handlePageChange} />

            <ModalConfirm show={isDeleteModalShow}
                onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel}
                title='Удаление' message='Удалить элемент?' />
            <ModalForm show={isFormModalShow} validated={isFormValidated}
                onSubmit={handleFormSubmit} onClose={handleFormClose}
                title='Редактирование'>
                <SoftwaresForm software={currentSoftware} handleChange={handleSoftwareChange} />
            </ModalForm>
        </>
    );
};

Softwares.propTypes = {
    handleLinesChange: PropTypes.func,
}

export default Softwares;
