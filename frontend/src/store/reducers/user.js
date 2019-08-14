import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    users: [],
    user:null,
    loading: false,
    userUpdated:false
};

const showFullUser = ( state, action ) => {
    return updateObject( state, {
        user: action.user,
    } );
};

const userFinishUpdate = (state,action) =>{
    return updateObject( state, {
        userUpdated: action.userUpdated,
    } );
};


const updateUserFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const updateUserStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const dismountAdmin = ( state, action ) => {
    return updateObject( state, {
        users: [],
    } );
};

const updateUserSuccess = ( state, action ) => {
    return updateObject( state, {
        loading: false,
        userUpdated: true,
    } );
};


const fetchUsersStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchUserStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchUsersSuccess = ( state, action ) => {
    return updateObject( state, {
        users: action.users,
        loading: false
    } );
};

const fetchUserSuccess = ( state, action ) => {
    return updateObject( state, {
        user: action.user,
        loading: false
    } );
};

const fetchUsersFail = ( state, action ) => {
    return updateObject( state, {
        loading: false ,
        users:[]
    } );
};

const fetchUserFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};


const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.UPDATE_USER_START: return updateUserStart( state, action );
        case actionTypes.UPDATE_USER_SUCCESS: return updateUserSuccess( state, action );
        case actionTypes.UPDATE_USER_FAIL: return updateUserFail( state, action );
        case actionTypes.USER_FINISH_UPDATE: return userFinishUpdate( state, action );
        case actionTypes.FETCH_USERS_START: return fetchUsersStart( state, action );
        case actionTypes.FETCH_USER_START: return fetchUserStart( state, action );
        case actionTypes.FETCH_USERS_SUCCESS: return fetchUsersSuccess( state, action );
        case actionTypes.FETCH_USER_SUCCESS: return fetchUserSuccess( state, action );
        case actionTypes.FETCH_USERS_FAIL: return fetchUsersFail( state, action );
        case actionTypes.FETCH_USER_FAIL: return fetchUserFail( state, action );
        case actionTypes.SHOW_FULL_USER: return showFullUser(state, action);
        case actionTypes.DISMOUNT_ADMIN: return dismountAdmin(state, action);
        default: return state;
    }
};

export default reducer;