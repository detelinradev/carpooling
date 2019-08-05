import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';

export const createTripSuccess = ( id, tripData ) => {
    return {
        type: actionTypes.CREATE_TRIP_SUCCESS,
        tripId: id,
        tripData: tripData
    };
};

export const createTripFail = ( error ) => {
    return {
        type: actionTypes.CREATE_TRIP_FAIL,
        error: error
    };
};

export const createTripStart = () => {
    return {
        type: actionTypes.CREATE_TRIP_START
    };
};



export const createTrip = (TripData, token ) => {


    return dispatch => {
        dispatch( createTripStart() );
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.post ( '/trips' ,TripData,{headers}
         )
            .then( response => {
                dispatch( createTripSuccess( response.data.name, TripData ) );

            } )
            .then(()=>{
                this.props.history.replace( '/' );
            })
            .catch( error => {
                dispatch( createTripFail( error ) );
            } );
    };
};

export const createInit = () => {
    return {
        type: actionTypes.CREATE_INIT
    };
};

export const fetchTripsSuccess = ( trips ) => {
    return {
        type: actionTypes.FETCH_TRIPS_SUCCESS,
        trips: trips
    };
};

export const fetchTripsFail = ( error ) => {
    return {
        type: actionTypes.FETCH_TRIPS_FAIL,
        error: error
    };
};

export const fetchTripsStart = () => {
    return {
        type: actionTypes.FETCH_TRIPS_START
    };
};

export const fetchTrips = (token) => {
    return dispatch => {
        dispatch(fetchTripsStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        const queryParams = '?driver=username1';
            // '?auth=' + token + '&orderBy="userId"&equalTo="' + userId + '"';
        axios.get( '/trips' + queryParams, {headers})
            .then( res => {
                const fetchedTrips = [];
                for ( let key in res.data ) {
                    fetchedTrips.push( {
                        ...res.data[key],
                        id: key
                    } );
                }
                dispatch(fetchTripsSuccess(fetchedTrips));
            } )
            .catch( err => {
                dispatch(fetchTripsFail(err));
            } );
    };
};