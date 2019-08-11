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

    showFullTrip = (trip) => {
        let isJoined;
        console.log(trip)
        const currentUserName = this.props.username;
        let tripJoined = 'Join Trip';
        let passengers = trip.passengers.map(passenger =>
            (passenger.username === currentUserName)
        );
        isJoined = passengers.includes(true);
        if (isJoined) {
            tripJoined ='Trip joined'
        }
        let isRequested;
        if(trip.notApprovedPassengers) {
            let notApproved = trip.notApprovedPassengers.map(passenger =>
                (passenger.username === currentUserName)
            );
            isRequested = notApproved.includes(true);
            if (isRequested) {
                tripJoined = 'Request sent'
            }
        }
        if(trip.driver.username === currentUserName){
            tripJoined = ''
        }

        console.log(tripJoined)

        let myTrip ='passenger';
        if(trip.driver.username === this.props.username){
            myTrip = 'driver'
        }
        this.props.onShowFullTrip(trip,tripJoined,'No',myTrip);
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
                    <div className="SearchTrips">
                        <h3>My Trips</h3>
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
        onShowFullTrip: (trip,tripJoined,requestSent,isMyTrip) => dispatch(actions.showFullTrip(trip,tripJoined,requestSent,isMyTrip)),
        onFetchUserImage:(token,userId)=> dispatch(actions.fetchImageUser(token,userId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(MyTrips, axios));