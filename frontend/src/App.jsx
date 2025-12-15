import { observer } from "mobx-react-lite";
import { useContext, useEffect } from 'react';
import { Container } from 'react-bootstrap';
import { Toaster } from 'react-hot-toast';
import { Outlet } from 'react-router-dom';
import Footer from './components/Footer';
import Navigation from './components/Navigation';
import LoginModal from './components/modal/LoginModal.jsx';
import OtpVerificationModal from './components/modal/OtpVerificationModal.jsx';
import StoreContext from './components/users/StoreContext.jsx';

const App = observer(({ routes }) => {
 useEffect(() => {
    if (localStorage.getItem('token')) {
      store.checkAuth()
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const { store } = useContext(StoreContext);

  if (!store.isAuth) {
    return (
      <>
        <LoginModal
          show={!store.isOtpSent}
        />
        <OtpVerificationModal
          show={store.isOtpSent}
        />
        <Toaster position='top-center'
          reverseOrder={true}
          toastOptions={{
            duration: 4000,
          }} />
      </>
    );
  }

  return (
      <>
      <Navigation routes={routes}></Navigation>
      <Container className="d-flex flex-column flex-grow-1 my-1" as="main" fluid="md">
        <Outlet />
      </Container>
      <Footer />
      <Toaster position='top-center'
        reverseOrder={true}
        toastOptions={{
          duration: 4000,
        }} />
      </>
  );
});
export default App
