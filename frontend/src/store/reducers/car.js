import * as actionTypes from '../actions/actionTypes';
import {updateObject} from '../../shared/utility';

const initialState = {
    carCreated: 'no'

};

const createCarSuccess = (state, action) => {
    return updateObject(state, {
        carCreated: 'yes',
        loading: false
    });
};
const createCarFail = (state, action) => {
    return updateObject(state, {
        carCreated: 'error',
        loading: false
    });
};

const carFinishCreate = (state,action)=>{
    return updateObject(state, {
        carCreated: 'no',
    });
};

const createCarStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.CREATE_CAR_SUCCESS:  return createCarSuccess(state, action);
        case actionTypes.CREATE_CAR_FINISH:  return carFinishCreate(state, action);
        case actionTypes.CREATE_CAR_FAIL: return createCarFail(state, action);
        case actionTypes.CREATE_CAR_START: return createCarStart(state, action);
        default: return state;
    }
};

export default reducer;