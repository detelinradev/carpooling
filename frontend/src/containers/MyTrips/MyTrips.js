import React, {Component} from "react";
import * as actions from "../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../axios-baseUrl";
import Spinner from "../../components/UI/Spinner/Spinner";
import Trip from "../../components/TripComponents/Trip/Trip";


class MyTrips extends Component {

    componentDidMount() {
        this.props.onFetchTrips(this.props.token, '/myTrips');
    }

    componentWillUnmount() {
        this.props.onDismountMyTrips()
    }

    showFullTrip = (trip) => {
        const currentUserName = this.props.username;
        let userStatus = null;

        let tripRole = Object.entries(trip.userStatus).map(key =>
            (key[0].includes('username=' + currentUserName)) ? userStatus = key[1] : null
        );

        let isMyTrip = true;
        let tripStatus = trip.tripStatus;
        this.props.onShowFullTrip(trip, tripRole, userStatus, isMyTrip, tripStatus);
        this.props.history.push('/fullTrip');
    };

    render() {
        let trips = <Spinner/>;
        if (!this.props.loading) {


            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    data={trip}
                    userRole={trip.driver.username === this.props.username?'driver':'passenger'}
                    showFullTrip={this.showFullTrip}
                />
            ))
        }

        return (
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
        username: state.auth.userId,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token, formData) => dispatch(actions.fetchTrips(token, formData)),
        onShowFullTrip: (trip, tripRole, userStatus, isMyTrip, tripStatus) => dispatch(actions.showFullTrip(trip, tripRole, userStatus, isMyTrip, tripStatus)),
        onDismountMyTrips: () => dispatch(actions.dismountSearch()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(MyTrips, axios));