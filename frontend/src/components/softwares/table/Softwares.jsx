import PropTypes from 'prop-types';
import { useCallback, useState } from 'react';
import { Button, Form } from 'react-bootstrap';
import toast from 'react-hot-toast';
import DownloadButton from '../../input/DownloadButton.jsx';
import Input from '../../input/Input.jsx';
import MultiSelect from '../../input/MultiSelect.jsx';
import ModalConfirm from '../../modal/ModalConfirm.jsx';
import ModalForm from '../../modal/ModalForm.jsx';
import PaginationComponent from '../../pagination/Pagination.jsx';
import usePagination from '../../pagination/PaginationHook.js';
import SoftwaresForm from '../form/SoftwaresForm.jsx';
import useSoftwaresDeleteModal from '../hooks/SoftwaresDeleteModalHook.js';
import { useDateFilter, useNameFilter, useResetSearchParams, useTaskFilter } from '../hooks/SoftwaresFilterHook.js';
import useSoftwaresFormModal from '../hooks/SoftwaresFormModalHook.js';
import useSoftwares from '../hooks/SoftwaresHook.js';
import SoftwaresTable from './SoftwaresTable.jsx';
import SoftwaresTableRow from './SoftwaresTableRow.jsx';

const Softwares = () => {

    const {currentPage, handlePageChange} = usePagination();

    const {currentNameFilter, handleNameFilterChange} = useNameFilter();

    const [searchInput, setSearchInput] = useState(currentNameFilter);

    const handleSearchInputChange = (event) => {
        setSearchInput(event.target.value);
    };

    const { tasks, currentTaskFilter, handleTaskFilterChange } = useTaskFilter();

    console.log(tasks);

    const { currentStartDateFilter, currentEndDateFilter, handleDateFilterChange } = useDateFilter();

    const [startDateInput, setStartDateInput] = useState(currentStartDateFilter);

    const handleStartDateInputChange = (event) => {
        setStartDateInput(event.target.value);
    };

    const [endDateInput, setEndDateInput] = useState(currentEndDateFilter);

    const handleEndDateInputChange = (event) => {
        setEndDateInput(event.target.value);
    };

    const resetSearchParams = useResetSearchParams();

    const { softwares, handleSoftwaresChange, totalPages, clearFilters } = useSoftwares({page : currentPage, nameFilter: currentNameFilter, taskFilter: currentTaskFilter, startDateFilter: currentStartDateFilter, endDateFilter: currentEndDateFilter });

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

    const formatDateTimeForBackend = useCallback((dateString) => {
    if (!dateString) return null;
    try {
      const d = new Date(dateString);
      if (isNaN(d.getTime())) {
        toast.error('Произошла проблема при работе с датами');
        return null;
      }
      return d.toISOString();
    } catch (error) {
      toast.error(`Ошибка при работе с датам: ${error.message}`);
      return null;
    }
  }, []);

    const SOFTWARE_REPORT_URL = 'http://127.0.0.1:8080/api/v1/reports';
    return (
        <>
            <div className='d-flex'>
                <Form.Control type="text" name='name' value={searchInput} onChange={handleSearchInputChange} placeholder="Поиск" data-testid="soft-search-input"/>
                <Button variant='primary' className='m-0 ms-2' onClick={() => handleNameFilterChange(searchInput)} data-testid="soft-search-btn">Найти</Button>
            </div>
            <MultiSelect className='mt-2' options={tasks} label='Фильтр по задачам'
                     onChange={handleTaskFilterChange} />
            <div className='d-flex flex-column col-4'>
                <div className='d-flex justify-content-between'>
                    <Input name='startDateFilter' label='С' value={startDateInput} onChange={handleStartDateInputChange}
                                    type='datetime-local' required data-testid="filter-date-start"/>
                    <Input name='endDateFilter' label='До' value={endDateInput} onChange={handleEndDateInputChange}
                                    type='datetime-local' required data-testid="filter-date-end"/>
                </div>
                <Button variant='primary' className='m-0' onClick={() => handleDateFilterChange(formatDateTimeForBackend(startDateInput), formatDateTimeForBackend(endDateInput))} data-testid="filter-apply-btn">Применить</Button>
            </div>
            <div className='d-flex justify-content-end'>
                <Button variant='danger' className='my-2' onClick={() => {
                        clearFilters();
                        setSearchInput('');
                        resetSearchParams();}} data-testid="filter-clear-btn">Очистить фильтры</Button>
            </div>
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
                <Button variant='primary' className="fw-bold px-5 mb-5" onClick={() => showFormModal()} data-testid="soft-create-btn">
                    Добавить ПО
                </Button>
            </div>
            <PaginationComponent totalPages={totalPages} currentPage={currentPage} handlePageChange={handlePageChange} />

            <DownloadButton
                url={SOFTWARE_REPORT_URL}
                fileName="software_report.xlsx"
                buttonText="Скачать отчет ПО"
                variant="success"
                data-testid="soft-download-wrapper"
            />
            
            <ModalConfirm show={isDeleteModalShow}
                onConfirm={handleDeleteConfirm} onClose={handleDeleteCancel}
                title='Удаление' message='Удалить элемент?' />
            <ModalForm show={isFormModalShow} validated={isFormValidated}
                onSubmit={handleFormSubmit} onClose={handleFormClose}
                title='Редактирование' saveBtnTestId="soft-save-btn">
                <SoftwaresForm software={currentSoftware} handleChange={handleSoftwareChange} />
            </ModalForm>
        </>
    );
};

Softwares.propTypes = {
    handleLinesChange: PropTypes.func,
}

export default Softwares;
