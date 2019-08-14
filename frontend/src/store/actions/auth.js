import axios from 'axios';

import * as actionTypes from './actionTypes';

export const authStart = () => {
    return {
        type: actionTypes.AUTH_START
    };
};

export const authSuccess = (token, userId,userRole) => {
    return {
        type: actionTypes.AUTH_SUCCESS,
        idToken: token,
        userId: userId,
        userRole: userRole
    };
};
export const login = (username,password,isSignUp) => {
    if (isSignUp) {
        return dispatch => {
            dispatch(authStart());
            let url = 'http://localhost:8080/users/authenticate';
            let authData = {
                username: username,
                password: password
            };
            let currentUserName = username;

            axios.post(url, authData)
                .then(response => {
                    const fakeExpiresIn = 3600;
                    const expirationDate = new Date(new Date().getTime() +
                        fakeExpiresIn * 1000);
                    localStorage.setItem('token', response.headers.authorization);
                    localStorage.setItem('expirationDate', expirationDate);
                    localStorage.setItem('userId',
                        // response.data.localId
                        currentUserName
                    );
                    dispatch(authSuccess(response.headers.authorization, currentUserName,response.headers.userRole));
                    dispatch(checkAuthTimeout(
                        // response.data.expiresIn
                        fakeExpiresIn
                    ));
                })
                .catch(err => {
                    dispatch(authFail(err.errors));
                });

        };
    }
};

export const authFail = (error) => {
    return {
        type: actionTypes.AUTH_FAIL,
        error: error
    };
};

export const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('expirationDate');
    localStorage.removeItem('userId');
    return {
        type: actionTypes.AUTH_LOGOUT
    };
};

export const checkAuthTimeout = (expirationTime) => {
    return dispatch => {
        setTimeout(() => {
            dispatch(logout());
        }, expirationTime * 1000);
    };
};

export const auth = (username, password, isSignup, firstName, lastName, email, phone) => {
    return dispatch => {
        dispatch(authStart());


        let url = 'http://localhost:8080/users/register';
        let currentUserName= username;
        let authData= {
            username: username,
            password: password,
            firstName: firstName,
            lastName: lastName,
            email: email,
            phone: phone
        };
        if (!isSignup) {
            url = 'http://localhost:8080/users/authenticate';
            authData= {
                username: username,
                password: password
            };
        }
      axios.post(url, authData)
            .then(response => {
                const fakeExpiresIn = 3600;
                const expirationDate = new Date(new Date().getTime() +
                    fakeExpiresIn* 1000);
                localStorage.setItem('token', response.headers.authorization);
                localStorage.setItem('expirationDate', expirationDate);
                localStorage.setItem('userId',
                    // response.data.localId
                    currentUserName
                );
                dispatch(authSuccess(response.headers.authorization, currentUserName,response.headers.userRole));
                dispatch(checkAuthTimeout(
                    // response.data.expiresIn
                    fakeExpiresIn
                ));
            })
          .then(response => {
              dispatch(login(username, password, isSignup));
          })
            .catch(err => {
                dispatch(authFail(err.errors));
            });

    };
};

export const setAuthRedirectPath = (path) => {
    return {
        type: actionTypes.SET_AUTH_REDIRECT_PATH,
        path: path
    };
};

export const authCheckState = () => {
    return dispatch => {
        const token = localStorage.getItem('token');
        if (!token) {
            dispatch(logout());
        } else {
            const expirationDate = new Date(localStorage.getItem('expirationDate'));
            if (expirationDate <= new Date()) {
                dispatch(logout());
            } else {
                const userId = localStorage.getItem('userId');
                dispatch(authSuccess(token, userId));
                dispatch(checkAuthTimeout((expirationDate.getTime() - new Date().getTime()) / 1000));
            }
        }
    };
};