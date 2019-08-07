import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const createCarSuccess = ( id, carData ) => {
    return {
        type: actionTypes.CREATE_CAR_SUCCESS,
        tripId: id,
        tripData: carData
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
                dispatch( createCarSuccess( response.data.name, CarData ) );

            } )
            .then(()=>{
                this.props.history.replace( '/MyProfile' );
            })
            .catch( error => {
                dispatch( createCarFail( error ) );
            } );
    };
};

export const createInit = () => {
    return {
        type: actionTypes.CREATE_INIT
    };
};
