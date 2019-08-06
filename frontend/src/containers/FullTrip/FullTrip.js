import React, {Component} from 'react';
import {connect} from 'react-redux';
import './FullTrip.css';
import axios from '../../axios-baseUrl';
import {FaAndroid} from 'react-icons/fa';
import * as actions from '../../store/actions/index';
import Trip from "../../components/TripComponents/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import Button from "@material-ui/core/Button";

class FullTrip extends Component{

    render() {
        return(
            <div className=" Post" >
                <div className="proba Trip additional-details hed">{this.props.trip.origin} -> {this.props.trip.destination}</div>
                <div className="Trip additional-details  cardcont  meta-data-container">
                    <p className="image">
                        <img id="postertest" className='poster' style={{width: 128}}
                             src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>
                        <p className="meta-data">{this.props.trip.driver.firstName} {this.props.trip.driver.lastName}</p></p>
                    <p className="row-xs-6 info"> Departure Time<p className="meta-data">{this.props.trip.departureTime}</p>
                    </p>
                    <p className="row-xs-6 info">Available Seats<p
                        className="meta-data">{this.props.trip.availablePlaces}</p>
                    </p>
                    <p className="row-xs-6 info">Price<p className="meta-data">{this.props.trip.costPerPassenger} leva</p>
                    </p>
                    <p className="row-xs-6 info">Car<p className="meta-data">{this.props.trip.car.brand} {this.props.trip.car.model}</p></p>
                    <p className="row-xs-6 info">Status<p className="meta-data">{this.props.trip.tripStatus}</p></p>
                    <p className="row-xs-3 info">Message<p className="meta-data">{this.props.trip.message}</p></p>
                    <p className="row-xs-6 info">Smoking allowed<p className="meta-data">{this.props.trip.car.smokingAllowed}</p>
                    </p>
                    <p className="row-xs-6 info">Pets allowed<p className="meta-data">{this.props.trip.car.petsAllowed}</p></p>
                    <p className="row-xs-6 info">Luggage allowed<p className="meta-data">{this.props.trip.car.luggageAllowed}</p>
                    </p>
                    <p className="row-xs-6 info">Air-conditioned<p className="meta-data">{this.props.trip.car.airConditioned}</p>
                    </p>
                </div>

            </div>
        )

}
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        trip:state.trip.trip,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrip: (token,tripId) => dispatch(actions.fetchTrip(token,tripId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(FullTrip, axios));