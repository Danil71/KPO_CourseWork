import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import DepartmentsForm from '../form/DepartmentsForm.jsx';
import useDepartmentsDeleteModal from '../hooks/DepartmentsDeleteModalHook.js';
import useDepartmentsFormModal from '../hooks/DepartmentsFormModalHook.js';
import useDepartments from '../hooks/DepartmentsHook.js';
import DepartmentsTable from './DepartmentsTable.jsx';
import DepartmentsTableRow from './DepartmentsTableRow.jsx';

const Departments = () => {

    const { departments, handleDepartmentsChange } = useDepartments();

    const {
        isDeleteModalShow,
        showDeleteModal,
        handleDeleteConfirm,
        handleDeleteCancel,
    } = useDepartmentsDeleteModal(handleDepartmentsChange);

    const {
        isFormModalShow,
        isFormValidated,
        showFormModal,
        currentDepartment,
        handleDepartmentChange,
        handleFormSubmit,
        handleFormClose,
        allAvailableTasks,
        initialTasks,
        handleSelectedTaskIdsChange,
    } = useDepartmentsFormModal(handleDepartmentsChange);

    return (
        <>
            <DepartmentsTable>
                {
                    departments.map((currentDepartment, index) =>
                        <DepartmentsTableRow key={currentDepartment.id}
                            index={index} department={currentDepartment}
                            onDelete={() => showDeleteModal(currentDepartment.id)}
                            onEdit={() => showFormModal(currentDepartment.id)}
                        />)
                }
            </DepartmentsTable>
            <div className="d-flex justify-content-center">
                <Button variant='primary' className="fw-bold px-5 mb-5" onClick={() => showFormModal()}>
                    Добавить отдел
                </Button>
            </div>
            <ModalConfirm show={isDeleteModalShow}
                onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel}
                title='Удаление' message='Удалить элемент?' />
            <ModalForm show={isFormModalShow} validated={isFormValidated}
                onSubmit={handleFormSubmit} onClose={handleFormClose}
                title='Редактирование'>
                <DepartmentsForm
                    department={currentDepartment}
                    handleChange={handleDepartmentChange}
                    allAvailableTasks={allAvailableTasks}
                    initialTasks={initialTasks}
                    handleSelectedTaskIdsChange={handleSelectedTaskIdsChange}
                />
            </ModalForm>
        </>
    );
};

Departments.propTypes = {
    handleLinesChange: PropTypes.func,
}

export default Departments;
