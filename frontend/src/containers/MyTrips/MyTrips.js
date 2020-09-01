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
        let passengerStatus = null;

        let isMyTrip = true;
        // let tripRole = trip.driver.username === currentUserName?'driver':'passenger';
        this.props.onShowFullTrip(trip);
        this.props.history.push('/fullTrip');
    };

    render() {
        let trips = <Spinner/>;
        if (!this.props.loading) {



            trips = this.props.trips.map(trip => (

                <Trip
                    key={trip.trip.id}
                    data={trip}
                    trip={trip.trip}
                    driver={trip.user}
                    userRole={trip.user.username === this.props.username?'driver':'passenger'}
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
        onShowFullTrip: (trip) => dispatch(actions.showFullTrip(trip)),
        onDismountMyTrips: () => dispatch(actions.dismountSearch()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(MyTrips, axios));