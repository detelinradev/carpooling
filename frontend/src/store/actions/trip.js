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
                this.props.history.push( '/' );
            })
            .catch( error => {
                dispatch( createTripFail( error ) );
            } );
    };
};


export const updateTripSuccess = ( trip ) => {
    return {
        type: actionTypes.UPDATE_TRIP_SUCCESS,
        // tripId: id,
        trip: trip
    };
};

export const updateTripFail = ( error ) => {
    return {
        type: actionTypes.UPDATE_TRIP_FAIL,
        error: error
    };
};

export const updateTripStart = () => {
    return {
        type: actionTypes.UPDATE_TRIP_START
    };
};

export const updateTrip = (TripData, token ) => {


    return dispatch => {
        dispatch( updateTripStart() );
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.put ( '/trips' ,TripData,{headers}
        )
            .then( response => {
                dispatch( updateTripSuccess( TripData ) );

            } )
            .then(()=>{
                this.props.history.push( '/FullTrip' );
            })
            .catch( error => {
                dispatch( updateTripFail( error ) );
            } );
    };
};

export const createInit = () => {
    return {
        type: actionTypes.CREATE_INIT
    };
};

export const showFullTrip = ( trip,tripRole,passengerStatus,isMyTrip) => {
    return {
        type: actionTypes.SHOW_FULL_TRIP,
        trip:trip,
        tripRole:tripRole,
        passengerStatus:passengerStatus,
        isMyTrip:isMyTrip
    };
};

export const fetchTripsSuccess = ( trips ) => {
    return {
        type: actionTypes.FETCH_TRIPS_SUCCESS,
        trips: trips
    };
};

export const fetchTripSuccess = ( trip,passengerStatus ) => {
    return {
        type: actionTypes.FETCH_TRIP_SUCCESS,
        trip: trip,
        passengerStatus: passengerStatus
    };
};

export const fetchTripsFail = ( error ) => {
    return {
        type: actionTypes.FETCH_TRIPS_FAIL,
        error: error
    };
};

export const fetchTripFail = ( error ) => {
    return {
        type: actionTypes.FETCH_TRIP_FAIL,
        error: error
    };
};

export const fetchTripsStart = () => {
    return {
        type: actionTypes.FETCH_TRIPS_START
    };
};

export const fetchTripStart = () => {
    return {
        type: actionTypes.FETCH_TRIP_START
    };
};

export const fetchTrips = (token,queryParams) => {
    return dispatch => {

        dispatch(fetchTripsStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( 'http://localhost:8080/trips' + queryParams, {headers})
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

export const fetchTrip = (token,tripId,passengerStatus) => {
    return dispatch => {
        dispatch(fetchTripStart());
        const headers = {
            "Content-Type":"application/json",
            'Authorization':token
        };
        axios.get( '/trips/' +tripId , {headers})
            .then( res => {
                dispatch(fetchTripSuccess(res.data,passengerStatus))
            })
            .catch( err => {
                dispatch(fetchTripFail(err));
            } );
    };
};