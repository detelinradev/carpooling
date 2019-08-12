import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';
//
// export const fetchImageDriverSuccess = (driverImage,modelId) => {
//     return {
//         type: actionTypes.FETCH_DRIVER_IMAGE_SUCCESS,
//         driverImage: driverImage,
//         modelId:modelId
//     };
// };
//
// export const fetchImagePassengerSuccess = (passengerImage,modelId) => {
//     return {
//         type: actionTypes.FETCH_PASSENGER_IMAGE_SUCCESS,
//         passengerImage: passengerImage,
//         modelId:modelId
//     };
// };
//
// export const fetchImageCommentSuccess = (commentImage,modelId) => {
//     return {
//         type: actionTypes.FETCH_COMMENT_IMAGE_SUCCESS,
//         commentImage: commentImage,
//         modelId:modelId
//     };
// };
//
// export const fetchImageCarSuccess = (carImage) => {
//     return {
//         type: actionTypes.FETCH_CAR_IMAGE_SUCCESS,
//         carImage: carImage
//     };
// };
//
// export const fetchImageDriverFail = (error) => {
//     return {
//         type: actionTypes.FETCH_DRIVER_IMAGE_FAIL,
//         error: error
//     };
// };
//
// export const fetchImagePassengerFail = (error) => {
//     return {
//         type: actionTypes.FETCH_PASSENGER_IMAGE_FAIL,
//         error: error
//     };
// };
//
// export const fetchImageCommentFail = (error) => {
//     return {
//         type: actionTypes.FETCH_COMMENT_IMAGE_FAIL,
//         error: error
//     };
// };
//
// export const fetchImageCarFail = (error) => {
//     return {
//         type: actionTypes.FETCH_CAR_IMAGE_FAIL,
//         error: error
//     };
// };
//
// export const fetchImageUserStart = () => {
//     return {
//         type: actionTypes.FETCH_USER_IMAGE_START
//     };
// };
//
// export const fetchImageCarStart = () => {
//     return {
//         type: actionTypes.FETCH_CAR_IMAGE_START
//     };
// };
//
// export const fetchImageUser = (token, userId, userType,modelId) => {
//     return dispatch => {
//         dispatch(fetchImageUserStart());
//         const headers = {
//             "Content-Type": "application/json",
//             'Authorization': token
//         };
//         fetch('http://localhost:8080/users/avatar/' + userId, {headers})
//             .then(response => response.blob())
//             .then(blob => URL.createObjectURL(blob))
//             .then(res => {
//                 if (userType === 'passenger')
//                     dispatch(fetchImagePassengerSuccess(res,modelId));
//                 if (userType === 'driver')
//                     dispatch(fetchImageDriverSuccess(res,modelId));
//                 if (userType === 'comment')
//                     dispatch(fetchImageCommentSuccess(res,modelId));
//             })
//             .catch(err => {
//                 if (userType === 'passenger')
//                     dispatch(fetchImagePassengerFail(err));
//                 if (userType === 'driver')
//                     dispatch(fetchImageDriverFail(err));
//                 if (userType === 'comment')
//                     dispatch(fetchImageCommentFail(err));
//             });
//     };
// };

// export const fetchImageCar = (token, userId) => {
//     return dispatch => {
//         dispatch(fetchImageCarStart());
//         const headers = {
//             "Content-Type": "application/json",
//             'Authorization': token
//         };
//         fetch('http://localhost:8080/users/avatar/car/' + userId, {headers})
//             .then(response => response.blob())
//             .then(blob => URL.createObjectURL(blob))
//             .then(res => {
//                 dispatch(fetchImageCarSuccess(res))
//             })
//             .catch(err => {
//                 dispatch(fetchImageCarFail(err));
//             });
//     };
// };