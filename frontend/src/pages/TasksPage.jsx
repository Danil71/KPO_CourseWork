import Tasks from "../components/tasks/table/Tasks";

const TasksPage = () => {
    return (
        <>
            <div className="container-lg table-responsive">
                <h3>Задачи</h3>
                <Tasks />
            </div>
        </>
    );
};

export default TasksPage;