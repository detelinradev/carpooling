import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const fetchImageDriverSuccess = (driverImage) => {
    return {
        type: actionTypes.FETCH_DRIVER_IMAGE_SUCCESS,
        driverImage: driverImage
    };
};

export const fetchImagePassengerSuccess = (passengerImage) => {
    return {
        type: actionTypes.FETCH_PASSENGER_IMAGE_SUCCESS,
        passengerImage: passengerImage
    };
};

export const fetchImageCommentSuccess = (commentImage) => {
    return {
        type: actionTypes.FETCH_COMMENT_IMAGE_SUCCESS,
        commentImage: commentImage
    };
};

export const fetchImageCarSuccess = (carImage) => {
    return {
        type: actionTypes.FETCH_CAR_IMAGE_SUCCESS,
        carImage: carImage
    };
};

export const fetchImageDriverFail = (error) => {
    return {
        type: actionTypes.FETCH_DRIVER_IMAGE_FAIL,
        error: error
    };
};

export const fetchImagePassengerFail = (error) => {
    return {
        type: actionTypes.FETCH_PASSENGER_IMAGE_FAIL,
        error: error
    };
};

export const fetchImageCommentFail = (error) => {
    return {
        type: actionTypes.FETCH_COMMENT_IMAGE_FAIL,
        error: error
    };
};

export const fetchImageCarFail = (error) => {
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

export const fetchImageUser = (token, userId, userType) => {
    return dispatch => {
        dispatch(fetchImageUserStart());
        console.log(1234);
        const headers = {
            "Content-Type": "application/json",
            'Authorization': token
        };
        fetch('http://localhost:8080/users/avatar/' + userId, {headers})
            .then(response => response.blob())
            .then(blob => URL.createObjectURL(blob))
            .then(res => {
                if (userType === 'passenger')
                    dispatch(fetchImagePassengerSuccess(res));
                if (userType === 'driver')
                    dispatch(fetchImageDriverSuccess(res));
                if (userType === 'comment')
                    dispatch(fetchImageCommentSuccess(res));
            })
            .catch(err => {
                if (userType === 'passenger')
                    dispatch(fetchImagePassengerFail(err));
                if (userType === 'driver')
                    dispatch(fetchImageDriverFail(err));
                if (userType === 'comment')
                    dispatch(fetchImageCommentFail(err));
            });
    };
};

export const fetchImageCar = (token, userId) => {
    return dispatch => {
        dispatch(fetchImageCarStart());
        const headers = {
            "Content-Type": "application/json",
            'Authorization': token
        };
        fetch('http://localhost:8080/users/avatar/car/' + userId, {headers})
            .then(response => response.blob())
            .then(blob => URL.createObjectURL(blob))
            .then(res => {
                dispatch(fetchImageCarSuccess(res))
            })
            .catch(err => {
                dispatch(fetchImageCarFail(err));
            });
    };
};