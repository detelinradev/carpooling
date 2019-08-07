import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Home.css';
import axios from '../../axios-baseUrl';
import {FaAndroid} from 'react-icons/fa';
import * as actions from '../../store/actions/index';
import Trip from "../../components/TripComponents/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";


class Home extends Component {

    componentDidMount() {
        if (this.props.token) {
            this.props.onFetchTrips(this.props.token);
        }
    }

    showFullTrip = (trip) => {
        this.props.onShowFullTrip(trip);
        this.props.history.push('/fullTrip');
    };

    render() {
        let trips = <Spinner/>;
        if (this.props.token && this.props.trips) {

            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    data = {trip}
                    driver={trip.driver}
                    passengers={trip.passengers}
                    comments={trip.comments}
                    car ={trip.car}
                    showFullTrip={this.showFullTrip}
                    token={this.props.token}
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
        token: state.auth.token,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token) => dispatch(actions.fetchTrips(token)),
        onShowFullTrip: (trip) => dispatch(actions.showFullTrip(trip)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Home, axios));