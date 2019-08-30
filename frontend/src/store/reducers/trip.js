import * as actionTypes from '../actions/actionTypes';
import { updateObject } from '../../shared/utility';

const initialState = {
    trips: [],
    loading: false,
    trip:null,
    tripRole:null,
    passengerStatus:null,
    isMyTrip:null,
    tripCreated:false,
    tripUpdated:'no',
};

const createInit = ( state, action ) => {
    return updateObject( state, { purchased: false } );
};

const showFullTrip = ( state, action ) => {
    return updateObject( state, {
        trip: action.trip,
        tripRole: action.tripRole,
        passengerStatus: action.passengerStatus,
        isMyTrip: action.isMyTrip,
        tripStatus:action.tripStatus
    } );
};

const tripFinishUpdate = (state,action) =>{
    return updateObject( state, {
        tripUpdated: action.tripUpdated,
    } );
};

const tripChangeStatus = (state,action) =>{
    return updateObject( state, {
        tripStatus: action.tripStatus,
    } );
};

const dismountSearch = ( state, action ) => {
    return updateObject( state, {
        trips: [],
    } );
};

const tripCreated = (state,action) =>{
    return updateObject( state, {
        tripCreated: action.tripCreated,
    } );
};

const createTripStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const createTripSuccess = ( state, action ) => {
    return updateObject( state, {
        loading: false,
        tripCreated: true,
    } );
};

const createTripFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const updateTripFail = ( state, action ) => {
    return updateObject( state, {
        loading: false,
        tripUpdated: 'error',
    } );
};

const updateTripStart = ( state, action ) => {
    return updateObject( state, { loading: true } );
};

const updateTripSuccess = ( state, action ) => {
    return updateObject( state, {
        loading: false,
        tripUpdated: 'yes',
    } );
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
        passengerStatus:action.passengerStatus,
        loading: false
    } );
};

const fetchTripsFail = ( state, action ) => {
    return updateObject( state, {
        loading: false ,
        trips:[]
    } );
};

const fetchTripFail = ( state, action ) => {
    return updateObject( state, { loading: false } );
};

const reducer = ( state = initialState, action ) => {
    switch ( action.type ) {
        case actionTypes.CREATE_INIT: return createInit( state, action );
        case actionTypes.CREATE_TRIP_START: return createTripStart( state, action );
        case actionTypes.CREATE_TRIP_SUCCESS: return createTripSuccess( state, action );
        case actionTypes.TRIP_CREATED: return tripCreated( state, action );
        case actionTypes.CREATE_TRIP_FAIL: return createTripFail( state, action );
        case actionTypes.UPDATE_TRIP_START: return updateTripStart( state, action );
        case actionTypes.UPDATE_TRIP_SUCCESS: return updateTripSuccess( state, action );
        case actionTypes.UPDATE_TRIP_FAIL: return updateTripFail( state, action );
        case actionTypes.TRIP_FINISH_UPDATE: return tripFinishUpdate( state, action );
        case actionTypes.FETCH_TRIPS_START: return fetchTripsStart( state, action );
        case actionTypes.FETCH_TRIP_START: return fetchTripStart( state, action );
        case actionTypes.FETCH_TRIPS_SUCCESS: return fetchTripsSuccess( state, action );
        case actionTypes.FETCH_TRIP_SUCCESS: return fetchTripSuccess( state, action );
        case actionTypes.FETCH_TRIPS_FAIL: return fetchTripsFail( state, action );
        case actionTypes.FETCH_TRIP_FAIL: return fetchTripFail( state, action );
        case actionTypes.SHOW_FULL_TRIP: return showFullTrip(state, action);
        case actionTypes.DISMOUNT_SEARCH: return dismountSearch(state, action);
        case actionTypes.CHANGE_TRIP_STATUS: return tripChangeStatus(state, action);
        default: return state;
    }
};

export default reducer;