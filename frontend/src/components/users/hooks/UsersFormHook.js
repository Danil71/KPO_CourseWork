import { useState } from 'react';
import toast from 'react-hot-toast';
import Store from '../../../store/Store';
import AuthApiService from '../../api/AuthApiService';
import UsersApiService from '../service/UsersApiService';
import useUser from './UserByIdHook';



const useUsersForm = (id, usersChangeHandle) => {
    const { user, setUser, initialEmail, initialEmployee } = useUser(id);

    const [validated, setValidated] = useState(false);

    const resetValidity = () => {
        setValidated(false);
    };

    const authApiService = new AuthApiService();

    const getUserObject = ({
    email,
    password,
    phoneNumber,
    role,
    employeeId
  }) => ({
    email,
    password,
    phoneNumber,
    role,
    employeeId
  });

    const handleChange = (event) => {
        const inputName = event.target.name;
        const inputValue = event.target.type === 'checkbox' ? event.target.checked : event.target.value;
        setUser({
            ...user,
            [inputName]: inputValue,
        });
    };

    const handleEmployeeChange = (newEmployeeId) => {
        setUser({
            ...user,
            employeeId: newEmployeeId
        });
    };

    const handleSubmit = async (event) => {
        const form = event.currentTarget;
        event.preventDefault();
        event.stopPropagation();
        const body = getUserObject(user);
        if (form.checkValidity()) {
            if (id === undefined) {
                await UsersApiService.create(body);
            } else {
                await authApiService.logoutDirect({ email: initialEmail, clientType: Store.CLIENT_TYPE });
                await UsersApiService.update(id, body);
            }
            if (usersChangeHandle) usersChangeHandle();
            toast.success('Элемент успешно сохранен', { id: 'UsersTable' });
            return true;
        }
        setValidated(true);
        return false;
    };

    return {
        user,
        validated,
        handleSubmit,
        handleChange,
        handleEmployeeChange,
        initialEmployee,
        resetValidity,
    };
};

export default useUsersForm;
