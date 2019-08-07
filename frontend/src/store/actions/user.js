import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const fetchImageUserSuccess = ( userImage ) => {
    return {
        type: actionTypes.FETCH_USER_IMAGE_SUCCESS,
        userImage: userImage
    };
};

export const fetchImageCarSuccess = ( carImage ) => {
    return {
        type: actionTypes.FETCH_CAR_IMAGE_SUCCESS,
        carImage: carImage
    };
};

export const fetchImageUserFail = ( error ) => {
    return {
        type: actionTypes.FETCH_USER_IMAGE_FAIL,
        error: error
    };
};

export const fetchImageCarFail = ( error ) => {
    return {
        type: actionTypes.FETCH_CAR_IMAGE_FAIL,
        error: error
    };
};

export const fetchImageUserStart = () => {
    return {
        type: actionTypes.FETCH_USER_IMAGE_START
    };
};

export const fetchImageCarStart = () => {
    return {
        type: actionTypes.FETCH_CAR_IMAGE_START
    };
};

export const fetchImageUser = (token,userId) => {
    return dispatch => {
        dispatch(fetchImageUserStart());
        console.log(1);
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( 'http://localhost:8080/users/avatar/' + userId, {headers})
            .then(response => response.blob())
            .then(blob =>  URL.createObjectURL(blob))
            .then( res => {
                dispatch(fetchImageUserSuccess(res));
            })
            .catch( err => {
                dispatch(fetchImageUserFail(err));
            } );
    };
};

export const fetchImageCar = (token,userId) => {
    return dispatch => {
        dispatch(fetchImageCarStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( '/users/avatar/car/' +userId , {headers})
            .then(response => response.blob())
            .then(blob =>  URL.createObjectURL(blob))
            .then( res => {
                dispatch(fetchImageCarSuccess(res))
            })
            .catch( err => {
                dispatch(fetchImageCarFail(err));
            } );
    };
};