import axios from 'axios';
import { jwtDecode } from 'jwt-decode';
import { makeAutoObservable } from "mobx";
import toast from 'react-hot-toast';
import { API_URL } from '../components/api/ApiClient';
import AuthApiService from '../components/api/AuthApiService';

export default class Store {
    user = {};
    userEmail = '';
    isOtpSent = false;
    isAuth = false;
    isLoading = false;

    static CLIENT_TYPE = 'WEB';

    constructor() {
        makeAutoObservable(this);
        this.authApiService = new AuthApiService();
    }

    setAuth(bool) {
        this.isAuth = bool;
    }

    setUser(user) {
        this.user = user;
    }

    setLoading(bool) {
        this.isLoading = bool;
    }

    setOtpSent(bool) {
        this.isOtpSent = bool;
    }

    setUserEmail(email) {
        this.userEmail = email;
    }

    get isAdmin() {
        return this.user?.role === 'MANAGER';
    }

    get isRegularUser() {
        return this.user?.role === 'DELEVOPER';
    } 
    
    resetAuthState() {
        this.setUser({});
        this.setUserEmail('');
        this.setOtpSent(false);
        this.setAuth(false);
        localStorage.removeItem('token');
    }

    async login(email, password) {
        try {
            await this.authApiService.login({ email, password, clientType: Store.CLIENT_TYPE });
            this.setUserEmail(email);
            this.setOtpSent(true);
        } catch (e) {
            if (e?.status === 401 && (e?.error == 'BAD_CREDENTIALS' || e?.error == 'USER_NOT_FOUND')) {
                toast.error('Были введены неверные данные!  Пожалуйста проверьте свою электронную почту или пароль');
            } else {
                toast.error('Произошла ошибка: ' + e?.error + ' - ' + e?.message);
            }
            this.setOtpSent(false);
            this.setUserEmail('');
        }
    }

    async sendOtp(email) {
        try {
            await this.authApiService.sendOtp({ email });
            toast.success('Проверьте вашу электронную почту');
        } catch (e) {
            toast.error('Ошибка при отправке OTP: ' + e?.error + ' - ' + e?.message);
        }
    }

    async invalidateOtp(email) {
        try {
            await this.authApiService.invalidateOtp({ email });
        } catch (e) {
            toast.error('Ошибка при сбросе OTP: ' + e?.error + ' - ' + e?.message);
        }
    }

    async verifyOtp(email, otp) {
        try {
            const otpInt = parseInt(otp, 10);
            if (isNaN(otpInt)) {
                toast.error('Некорректный одноразовый пароль');
                return;
            }

            const data = await this.authApiService.verifyOtp({ email, oneTimePassword: otpInt });

            if (!data.accessToken) {
                throw new Error('No access token received!');
            }

            localStorage.setItem('token', data.accessToken);
            const user = this.decodeUserFromToken(data.accessToken);

            this.setUser(user);
            this.setAuth(true);
            this.setOtpSent(false);
            this.setUserEmail('');
        } catch (e) {
            if (e?.status === 401 && e?.error === 'INVALID_OTP') {
              toast.error('Неверный одноразовый пароль. Попробуйте ещё раз');
            } else {
                toast.error('Произошла ошибка: ' + e?.error + ' - ' + e?.message);
            }
        }
    }

    async logout() {
    try {
        const email = this.user?.email;
        if (!email) {
            throw new Error('Cannot logout: email not available.');
        }
        await this.authApiService.logout({ email });
        localStorage.removeItem('token');
        this.setAuth(false);
        this.setUser({});
    } catch (e) {
        toast.error('Произошла ошибка: ' + e?.error + ' - ' + e?.message);
        console.error(e);
    }
}

    async checkAuth() {
      this.setLoading(true);
      try {
        const data = await axios.put(
            `${API_URL}/auth/refresh-token`,
            null,
            {
                withCredentials: true,
                headers: {
                Accept: 'application/json'
                }
            }
        );

        localStorage.setItem('token', data.data.accessToken);
        const user = this.decodeUserFromToken(data.data.accessToken);

        this.setUser(user);
        this.setAuth(true);
      } catch (e) {
        console.error(e);
        this.setAuth(false);
        this.setUser({});
      } finally {
        this.setLoading(false);
      }
    }

    decodeUserFromToken(token) {
        try {
            const decoded = jwtDecode(token);
            return {
                id: decoded.user_id,
                email: decoded.email,
                accountCreatedAt: decoded.account_creation_timestamp,
                role: decoded.role,
            };
        } catch (e) {
            toast.error('Произошла ошибка: ' + e?.error + ' - ' + e?.message);
            return {};
        }
    }
}