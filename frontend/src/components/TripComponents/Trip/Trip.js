import React, {Component} from 'react';
import './Trip.css';
import Button from "@material-ui/core/Button";
import Auxiliary from "../../../hoc/Auxiliary/Auxiliary";
import {FaUserEdit} from "react-icons/fa";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import * as actions from "../../../store/actions";
import Spinner from "../../UI/Spinner/Spinner";

class Trip extends Component {
    // const getAvatarResponse = async () =>{
    //   await fetch('http://localhost:8080/users/avatar/' + props.data.driver.modelId,
    //         {headers: {"Authorization": props.token}})
    //         .then(response => response.blob())
    //         .then(blob =>  URL.createObjectURL(blob))
    // };
    //
    // const fetchImage = async () => {
    //      const token = sessionStorage.getItem("jwt");
    //      await fetch(`http://localhost:8080/image/downloadImage`,
    //          { headers: {Authorization: token}}
    //      )
    //      // .then(validateResponse)
    //          .then(response => response.blob())
    //          .then(blob => {
    //              this.setState({ src: URL.createObjectURL(blob) })
    //          })
    //
    //  };
    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.data.driver.modelId,'driver');
    }

    // let fetchUserImage = {()=> this.props.fetchUserImage}
    render() {
        // let trip = this.props.error ? <p>Trips can't be loaded!</p> : <Spinner />;
        let trip = null;
        if (this.props.driverImage) {
            trip = (
                <div className=" Post">
                    <div
                        className="proba Trip additional-details hed">{this.props.data.origin} -> {this.props.data.destination}</div>
                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.props.driverImage} alt={''}/>
                            <p className="meta-data">{this.props.data.driver.firstName} {this.props.data.driver.lastName}</p>
                        </p>
                        <div className="edit">
                            <Button onClick={() => this.props.showFullTrip(this.props.data)}><h3
                                className="header">DETAILS <FaUserEdit/></h3>
                            </Button>
                        </div>
                        <p className="row-xs-6 info"> Departure Time<p
                            className="meta-data">{this.props.data.departureTime}</p>
                        </p>
                        <p className="row-xs-6 info">Available Seats<p
                            className="meta-data">{this.props.data.availablePlaces}</p>
                        </p>
                        <p className="row-xs-6 info">Price<p
                            className="meta-data">{this.props.data.costPerPassenger} leva</p>
                        </p>
                        <p className="row-xs-6 info">Car<p
                            className="meta-data">{this.props.data.car.brand} {this.props.data.car.model}</p></p>
                        <p className="row-xs-6 info">Status<p className="meta-data">{this.props.data.tripStatus}</p></p>
                        <p className="row-xs-3 info">Message<p className="meta-data">{this.props.data.message}</p></p>
                        <p className="row-xs-6 info">Smoking allowed<p
                            className="meta-data">{this.props.car.smokingAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Pets allowed<p
                            className="meta-data">{this.props.car.petsAllowed}</p></p>
                        <p className="row-xs-6 info">Luggage allowed<p
                            className="meta-data">{this.props.car.luggageAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Air-conditioned<p
                            className="meta-data">{this.props.car.airConditioned}</p>
                        </p>

                    </div>

                </div>

            )
        }
        return (
    <div>
    {trip}
    </div>

        );
    }

}

const mapStateToProps = state => {
    return {
        // trips: state.trip.trips,
        // loading: state.trip.loading,
        token: state.auth.token,
        driverImage: state.user.driverImage
    }
};

const mapDispatchToProps = dispatch => {
    return {
        // onFetchTrips: (token,formData) => dispatch(actions.fetchTrips(token, formData)),
        // onShowFullTrip: (trip) => dispatch(actions.showFullTrip(trip)),
        onFetchUserImage: (token, userId,userType) => dispatch(actions.fetchImageUser(token, userId,userType))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Trip, axios));
