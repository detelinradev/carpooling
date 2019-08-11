import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    trips: [],
    loading: false,
    purchased: false,
    trip:null,
    tripJoined:'Join Trip',
    requestSent:'No',
    isMyTrip:''
};

const createInit = ( state, action ) => {
    return updateObject( state, { purchased: false } );
};

const showFullTrip = ( state, action ) => {
    return updateObject( state, {
        trip: action.trip,
        tripJoined:action.tripJoined,
        requestSent: action.requestSent,
        isMyTrip: action.isMyTrip
    } );
};

// const changeJoinTripStatus = ( state, action ) => {
//     return updateObject( state, { tripJoined: action.tripJoined } );
// };

const createTripStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const createTripSuccess = ( state, action ) => {
    // const newTrip = updateObject( action.tripData, { id: action.tripId } );
    return updateObject( state, {
        loading: false,
        purchased: true,
        // trips: state.trips.concat( newTrip )
    } );
};

const createTripFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchTripsStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchTripStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const fetchTripsSuccess = ( state, action ) => {
    return updateObject( state, {
        trips: action.trips,
        loading: false
    } );
};

const fetchTripSuccess = ( state, action ) => {
    return updateObject( state, {
        trip: action.trip,
        requestSent:action.requestSent,
        loading: false
    } );
};

const fetchTripsFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const fetchTripFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.CREATE_INIT: return createInit( state, action );
        case actionTypes.CREATE_TRIP_START: return createTripStart( state, action );
        case actionTypes.CREATE_TRIP_SUCCESS: return createTripSuccess( state, action );
        case actionTypes.CREATE_TRIP_FAIL: return createTripFail( state, action );
        case actionTypes.FETCH_TRIPS_START: return fetchTripsStart( state, action );
        case actionTypes.FETCH_TRIP_START: return fetchTripStart( state, action );
        case actionTypes.FETCH_TRIPS_SUCCESS: return fetchTripsSuccess( state, action );
        case actionTypes.FETCH_TRIP_SUCCESS: return fetchTripSuccess( state, action );
        case actionTypes.FETCH_TRIPS_FAIL: return fetchTripsFail( state, action );
        case actionTypes.FETCH_TRIP_FAIL: return fetchTripFail( state, action );
        case actionTypes.SHOW_FULL_TRIP: return showFullTrip(state, action);
        default: return state;
    }
};

export default reducer;