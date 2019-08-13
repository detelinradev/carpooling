import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    carCreated: false

};

const createCarSuccess = ( state, action ) => {
    return updateObject( state, {
        carCreated: true,
        loading: false
    } );
};


const reducer = ( state = initialState, action ) => {
    if (action.type === actionTypes.CREATE_CAR_SUCCESS) {
        return createCarSuccess( state, action );
    } else {
        return state;
    }
};

export default reducer;