import * as actionTypes from './actionTypes';
import axios from '../../axios-baseUrl';


export const createTripSuccess = () => {
    return {
        type: actionTypes.CREATE_TRIP_SUCCESS,
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
                dispatch( createTripSuccess() );

            } )
            .catch( error => {
                dispatch( createTripFail( error ) );
            } );
    };
};

export const dismountSearch = () => {
    return {
        type: actionTypes.DISMOUNT_SEARCH
    };
};

export const updateTripSuccess = (  ) => {
    return {
        type: actionTypes.UPDATE_TRIP_SUCCESS,
    };
};

export const tripFinishUpdate = ( tripUpdated ) => {
    return {
        type: actionTypes.TRIP_FINISH_UPDATE,
        tripUpdated:tripUpdated
    };
};

export const changeTripStatus = ( tripStatus ) => {
    return {
        type: actionTypes.CHANGE_TRIP_STATUS,
        tripStatus:tripStatus
    };
};

export const tripCreated = ( tripCreated ) => {
    return {
        type: actionTypes.TRIP_CREATED,
        tripCreated:tripCreated
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
                if(!response.response)
                dispatch( updateTripSuccess() );
            } )
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

export const showFullTrip = (trip) => {
    return {
        type: actionTypes.SHOW_FULL_TRIP,
        trip: trip
    };
};

export const fetchTripsSuccess = ( trips ) => {
    return {
        type: actionTypes.FETCH_TRIPS_SUCCESS,
        trips: trips
    };
};

export const fetchTripSuccess = ( tripFull,passengerStatus ) => {
    return {
        type: actionTypes.FETCH_TRIP_SUCCESS,
        tripFull: tripFull,
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
        axios.get ( "trips" + queryParams, {headers})
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