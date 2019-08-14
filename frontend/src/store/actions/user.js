import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const updateUserSuccess = () => {
    return {
        type: actionTypes.UPDATE_USER_SUCCESS,
    };
};

export const userFinishUpdate = ( userUpdated ) => {
    return {
        type: actionTypes.USER_FINISH_UPDATE,
        userUpdated:userUpdated
    };
};

export const userCreated = ( userCreated ) => {
    return {
        type: actionTypes.USER_CREATED,
        userCreated:userCreated
    };
};

export const updateUserFail = ( error ) => {
    return {
        type: actionTypes.UPDATE_USER_FAIL,
        error: error
    };
};

export const updateUserStart = () => {
    return {
        type: actionTypes.UPDATE_USER_START
    };
};

export const updateUser = (UserData, token ) => {

    return dispatch => {
        dispatch( updateUserStart() );
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.put ( '/users' ,UserData,{headers}
        )
            .then( response => {
                dispatch( updateUserSuccess() );
            } )
            .catch( error => {
                dispatch( updateUserFail( error ) );
            } );
    };
};

export const showFullUser = ( user) => {
    return {
        type: actionTypes.SHOW_FULL_USER,
        user:user,
    };
};

export const fetchUsersSuccess = ( users ) => {
    return {
        type: actionTypes.FETCH_USERS_SUCCESS,
        users: users
    };
};

export const fetchUserSuccess = ( user ) => {
    return {
        type: actionTypes.FETCH_USER_SUCCESS,
        user: user,
    };
};

export const fetchUsersFail = ( error ) => {
    return {
        type: actionTypes.FETCH_USERS_FAIL,
        error: error
    };
};

export const fetchUserFail = ( error ) => {
    return {
        type: actionTypes.FETCH_USER_FAIL,
        error: error
    };
};

export const fetchUsersStart = () => {
    return {
        type: actionTypes.FETCH_USERS_START
    };
};

export const fetchUserStart = () => {
    return {
        type: actionTypes.FETCH_USER_START
    };
};

export const fetchUsers = (token,queryParams) => {
    return dispatch => {

        dispatch(fetchUsersStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( 'http://localhost:8080/users' + queryParams, {headers})
            .then( res => {
                const fetchedUsers = [];
                for ( let key in res.data ) {
                    fetchedUsers.push( {
                        ...res.data[key],
                        id: key
                    } );
                }
                dispatch(fetchUsersSuccess(fetchedUsers));
            } )
            .catch( err => {
                dispatch(fetchUsersFail(err));
            } );
    };
};

export const fetchUser = (token,userId) => {
    return dispatch => {
        dispatch(fetchUserStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( '/users/' +userId , {headers})
            .then( res => {
                dispatch(fetchUserSuccess(res.data))
            })
            .catch( err => {
                dispatch(fetchUserFail(err));
            } );
    };
};