import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const createCarSuccess = (  ) => {
return {
    type: actionTypes.CREATE_CAR_SUCCESS,
        };
};

export const createCarFail = ( error ) => {
    return {
        type: actionTypes.CREATE_CAR_FAIL,
        error: error
    };
};

export const createCarStart = () => {
    return {
        type: actionTypes.CREATE_CAR_START
    };
};

export const carFinishCreate = ( ) => {
    return {
        type: actionTypes.CREATE_CAR_FINISH
    };
};



export const createCar = (CarData, token ) => {


    return dispatch => {
        dispatch( createCarStart() );
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.post ( '/car' ,CarData,{headers}
        )
            .then( response => {
                if(!response.response)
                dispatch( createCarSuccess( ) );

            } )
            .catch( error => {
                dispatch( createCarFail( error ) );
            } );
    };
};
