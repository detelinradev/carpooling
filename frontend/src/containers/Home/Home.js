import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Home.css';
import axios from '../../axios-baseUrl';
import {FaAndroid} from 'react-icons/fa';
import * as actions from '../../store/actions/index';
import Trip from "../../components/Trips/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";


class Home extends Component {
    componentDidMount() {
        if (this.props.token) {
            this.props.onFetchTrips(this.props.token);
        }
    }


    render() {
        let trips = <Spinner/>;
        if (this.props.token && !this.props.loading) {
            console.log(this.props.trips);

            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    driver={trip.driver}
                    message={trip.message}
                    departureTime={trip.departureTime}
                    origin={trip.origin}
                    destination={trip.destination}
                    availablePlaces={trip.availablePlaces}
                    passengers={trip.passengers}
                    tripStatus={trip.tripStatus}
                    comments={trip.comments}
                    smoking={trip.car.smokingAllowed}
                    pets={trip.car.petsAllowed}
                    luggage={trip.car.luggageAllowed}
                    airConditioned={trip.car.airConditioned}
                />
            ))
        }
        if (this.props.token) {
            return (
                <div className="todore">
                    <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                    {trips}
                </div>)
        }

        return (
            <div className="todore">

                <h1 className="header">THE PERFECT PLACE TO FIND <FaAndroid/><br/> THE FASTEST WAY TO TRAVEL</h1>
                <hr style={{width: '70%'}}/>
                <h2 style={{textAlign: "center"}}>
                    SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                </h2>
            </div>)
    }
}

const mapStateToProps = state => {
    return {
        trips: state.trip.trips,
        loading: state.trip.loading,
        token: state.auth.token
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token) => dispatch(actions.fetchTrips(token))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Home, axios));