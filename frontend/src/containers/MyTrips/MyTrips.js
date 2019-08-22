import React, {Component} from "react";
import * as actions from "../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../axios-baseUrl";
import Spinner from "../../components/UI/Spinner/Spinner";
import Trip from "../../components/TripComponents/Trip/Trip";


class MyTrips extends Component {

    componentDidMount() {
        this.props.onFetchTrips(this.props.token,'/myTrips');
    }
    componentWillUnmount() {
        this.props.onDismountMyTrips()
    }

    showFullTrip = (trip) => {
        const currentUserName = this.props.username;
        const isDriver = trip.driver.username === currentUserName;
        let isPassenger;
        let passengerStatus =null;
        // if(!isDriver) {

            let passengers = trip.passengers.map(passenger =>
                (passenger.username === currentUserName)
            );
            isPassenger = passengers.includes(true);
            // if(isPassenger){
                let some =Object.entries(trip.passengerStatus).map(key=>
                    (key[0].includes('username='+currentUserName))? passengerStatus = key[1]:null
                );
            // }
        // }

        let isMyTrip = true;
        let tripRole = null;
        let tripStatus = trip.tripStatus;
        if(isPassenger) tripRole = 'passenger';
        if(isDriver) tripRole = 'driver';
        this.props.onShowFullTrip(trip, tripRole,passengerStatus,isMyTrip,tripStatus);
        this.props.history.push('/fullTrip');
    };
    render() {
        let trips = <Spinner/>;
        if (!this.props.loading) {

            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    data = {trip}
                    userRole={trip.driver.username === this.props.username?'driver':'passenger'}
                    driver={trip.driver}
                    passengers={trip.passengers}
                    comments={trip.comments}
                    car ={trip.car}
                    showFullTrip={this.showFullTrip}
                />
            ))
        }

        return(
            <div className="todore">
                <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                <div>
                    <div style={{color: "white"}} className="SearchTrips">
                        <h1>My Trips</h1>
                    </div>
                    {trips}
                </div>

            </div>
        )

    }
}
const mapStateToProps = state => {
    return {
        trips: state.trip.trips,
        loading: state.trip.loading,
        token: state.auth.token,
        username:state.auth.userId,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token,formData) => dispatch(actions.fetchTrips(token, formData)),
        onShowFullTrip: (trip,tripRole,passengerStatus,isMyTrip,tripStatus) => dispatch(actions.showFullTrip(trip,tripRole,passengerStatus,isMyTrip,tripStatus)),
        onDismountMyTrips: () => dispatch(actions.dismountSearch()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(MyTrips, axios));