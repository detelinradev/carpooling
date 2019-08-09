import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    users: [],
    loading: false,
    driverImage:null,
    passengerImage:null,
    commentImage:null,
    carImage:null
};

const fetchImageUserStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchImageCarStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchImageDriverSuccess = ( state, action ) => {
    return updateObject( state, {
        driverImage: action.driverImage,
        loading: false
    } );
};

const fetchImagePassengerSuccess = ( state, action ) => {
    return updateObject( state, {
        passengerImage: action.passengerImage,
        loading: false
    } );
};
const fetchImageCommentSuccess = ( state, action ) => {
    return updateObject( state, {
        commentImage: action.commentImage,
        loading: false
    } );
};

const fetchImageCarSuccess = ( state, action ) => {
    return updateObject( state, {
        carImage: action.carImage,
        loading: false
    } );
};

const fetchImageDriverFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchImagePassengerFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchImageCommentFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchImageCarFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.FETCH_USER_IMAGE_START: return fetchImageUserStart( state, action );
        case actionTypes.FETCH_CAR_IMAGE_START: return fetchImageCarStart( state, action );
        case actionTypes.FETCH_DRIVER_IMAGE_SUCCESS: return fetchImageDriverSuccess( state, action );
        case actionTypes.FETCH_PASSENGER_IMAGE_SUCCESS: return fetchImagePassengerSuccess( state, action );
        case actionTypes.FETCH_COMMENT_IMAGE_SUCCESS: return fetchImageCommentSuccess( state, action );
        case actionTypes.FETCH_CAR_IMAGE_SUCCESS: return fetchImageCarSuccess( state, action );
        case actionTypes.FETCH_DRIVER_IMAGE_FAIL: return fetchImageDriverFail( state, action );
        case actionTypes.FETCH_PASSENGER_IMAGE_FAIL: return fetchImagePassengerFail( state, action );
        case actionTypes.FETCH_COMMENT_IMAGE_FAIL: return fetchImageCommentFail( state, action );
        case actionTypes.FETCH_CAR_IMAGE_FAIL: return fetchImageCarFail( state, action );
        default: return state;
    }
};

export default reducer;