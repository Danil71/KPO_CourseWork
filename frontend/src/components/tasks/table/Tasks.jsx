import PropTypes from 'prop-types';
import { Button } from 'react-bootstrap';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import PaginationComponent from '../../pagination/Pagination.jsx';
import usePagination from '../../pagination/PaginationHook.js';
import TasksForm from '../form/TasksForm.jsx';
import useTasksDeleteModal from '../hooks/TasksDeleteModalHook.js';
import useTasksFormModal from '../hooks/TasksFormModalHook.js';
import useTasks from '../hooks/TasksHook.js';
import TasksTable from './TasksTable.jsx';
import TasksTableRow from './TasksTableRow.jsx';

const Tasks = () => {

    const {currentPage, handlePageChange} = usePagination();

    const { tasks, handleTasksChange, totalPages } = useTasks({page : currentPage});

    const {
        isDeleteModalShow,
        showDeleteModal,
        handleDeleteConfirm,
        handleDeleteCancel,
    } = useTasksDeleteModal(handleTasksChange);

    const {
        isFormModalShow,
        isFormValidated,
        showFormModal,
        currentTask,
        handleTaskChange,
        handleFormSubmit,
        handleFormClose,
    } = useTasksFormModal(handleTasksChange);
    return (
        <>
            <TasksTable>
                {
                    tasks.map((task, index) =>
                        <TasksTableRow key={task.id}
                            index={index} task={task}
                            onDelete={() => showDeleteModal(task.id)}
                            onEdit={() => showFormModal(task.id)}
                        />)
                }
            </TasksTable>
            <div className="d-flex justify-content-center">
                <Button variant='primary' className="fw-bold px-5 mb-5" onClick={() => showFormModal()} data-testid="task-create-btn">
                    Добавить задачу
                </Button>
            </div>
            <PaginationComponent totalPages={totalPages} currentPage={currentPage} handlePageChange={handlePageChange} />
            <ModalConfirm show={isDeleteModalShow}
                onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel}
                title='Удаление' message='Удалить элемент?' />
            <ModalForm show={isFormModalShow} validated={isFormValidated}
                onSubmit={handleFormSubmit} onClose={handleFormClose}
                title='Редактирование' saveBtnTestId="task-save-btn">
                <TasksForm task={currentTask} handleChange={handleTaskChange} />
            </ModalForm>
        </>
    );
};

Tasks.propTypes = {
    handleLinesChange: PropTypes.func,
}

export default Tasks;
