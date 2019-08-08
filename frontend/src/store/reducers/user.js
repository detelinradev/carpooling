import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    users: [],
    loading: false,
    userImage:"",
    carImage:null
};

const fetchImageUserStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchImageCarStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchImageUserSuccess = ( state, action ) => {
    return updateObject( state, {
        userImage: action.userImage,
        loading: false
    } );
};

const fetchImageCarSuccess = ( state, action ) => {
    return updateObject( state, {
        carImage: action.carImage,
        loading: false
    } );
};

const fetchImageUserFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchImageCarFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.FETCH_USER_IMAGE_START: return fetchImageUserStart( state, action );
        case actionTypes.FETCH_CAR_IMAGE_START: return fetchImageCarStart( state, action );
        case actionTypes.FETCH_USER_IMAGE_SUCCESS: return fetchImageUserSuccess( state, action );
        case actionTypes.FETCH_CAR_IMAGE_SUCCESS: return fetchImageCarSuccess( state, action );
        case actionTypes.FETCH_USER_IMAGE_FAIL: return fetchImageUserFail( state, action );
        case actionTypes.FETCH_CAR_IMAGE_FAIL: return fetchImageCarFail( state, action );
        default: return state;
    }
};

export default reducer;