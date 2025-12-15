import PropTypes from 'prop-types';
import useEmployees from '../../employees/hooks/EmployeesHook.js';
import Input from '../../input/Input.jsx';
import Select from '../../input/Select.jsx';
import { PASSWORD_MESSAGE, PASSWORD_PATTERN_STRING, PHONE_PATTERN_STRING } from '../../utils/Constants.js';

const UsersForm = ({ user, handleChange}) => {
    
    const PASSWORD_PATTERN = new RegExp(PASSWORD_PATTERN_STRING);
    const PHONE_PATTERN = new RegExp(PHONE_PATTERN_STRING);

    const { employees } = useEmployees({size : 40});

    const ROLE_OPTIONS = [
            { id: 'DEVELOPER', name: 'Разработчик' },
            { id: 'MANAGER', name: 'Менеджер-проектов' },
            ];

    return (
        <>
            <Input
                name="email"
                label="Электронная почта"
                value={user.email}
                onChange={handleChange}
                className="mb-4"
                type="email"
                feedback="Введите корректный email"
                required
            />
            <Input
                name="password"
                label={"Пароль"}
                value={user.password ?? ''}
                onChange={handleChange}
                className="mb-4"
                type="password"
                pattern={PASSWORD_PATTERN_STRING}
                isInvalid={
                (user.password && !PASSWORD_PATTERN.test(user.password))
                }
                feedback={PASSWORD_MESSAGE}
                required
            />
            <Input
                name="phoneNumber"
                label="Номер телефона"
                value={user.phoneNumber}
                onChange={handleChange}
                className="mb-4"
                type="text"
                pattern={PHONE_PATTERN_STRING}
                isInvalid={(user.phoneNumber && !PHONE_PATTERN.test(user.phoneNumber))}
                feedback="Неккоректный формат номера телефона для стран СНГ"
                required
            />
            <Select
                name="role"
                label="Роль"
                values={ROLE_OPTIONS}
                value={user.role}
                onChange={handleChange}
                className="mb-4"
                required
                isInvalid={!user.role}
                feedback="Выберите роль пользователя"
            />
            <Select
                name="employeeId"
                label="Cотрудник"
                values={employees}
                value={user.employeeId}
                onChange={handleChange}
                className="mb-4"
                required
                feedback="Выберите сотрудника"
            />
        </>
    );
};

UsersForm.propTypes = {
    user: PropTypes.object,
    handleChange: PropTypes.func,
};

export default UsersForm;
