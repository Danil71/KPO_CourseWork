import { useEffect, useState } from 'react';
import UsersApiService from '../service/UsersApiService';

const useUser = (id) => {
  const emptyUser = {
        id: '',
        email: '',
        password: '',
        phoneNumber: '',
        role: 'DEVELOPER',
        employeeId: '',
        employeeName: ''
    };
    
    const [user, setUser] = useState({ ...emptyUser });
    const [initialEmployee, setInitialEmployee] = useState(null);
    const [initialEmail, setInitialEmail] = useState(null);

    const getUserById = async (userId = undefined) => {
        if (userId && userId > 0) {
            const data = await UsersApiService.get(userId);
            setUser(data);
            setInitialEmployee({ value: data.employeeId, label: data.employeeName });
            setInitialEmail(data.email);
        } else {
            setUser({ ...emptyUser });
        }
    };

    useEffect(() => {
        getUserById(id);
    // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);

    return {
        user,
        setUser,
        initialEmployee,
        initialEmail
    };
};

export default useUser;
