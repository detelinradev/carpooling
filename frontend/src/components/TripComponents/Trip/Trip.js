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
import Avatar from "../../../assets/images/image-default.png";

class Trip extends Component {
    state = {
        src: Avatar,
    };
    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.data.driver.modelId, 'driver', this.props.data.modelid);
        console.log(this.state.src);

        if (this.props.data.modelId === this.props.modelId) {
            if (this.props.driverImage) {
                this.setState({
                    src: this.props.driverImage
                })
            }
            console.log(this.state.src)
        }
    }

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
                                 src={this.state.src} alt={''}/>
                            <p className="meta-data">{this.props.data.driver.firstName} {this.props.data.driver.lastName}</p>
                        </p>
                        <div className="ed">
                            <Button onClick={() => this.props.showFullTrip(this.props.data)}><h3
                                className="header">DETAILS <FaUserEdit/></h3>
                            </Button>
                        </div>
                        <div className="comps" style={{margin: 15}}>
                        <p className="row-xs-6 info"> Departure Time<p
                            className="meta-data">{this.props.data.departureTime}</p>
                        </p>
                            <hr/>
                        <p className="row-xs-6 info">Smoking allowed<p
                            className="meta-data">{this.props.data.smokingAllowed}</p>
                        </p>
                        </div>

                        <div  className="comps" style={{margin: 15}}>
                        <p className="row-xs-6 info">Available Seats<p
                            className="meta-data">{this.props.data.availablePlaces}</p>
                        </p>
                            <hr/>
                        <p className="row-xs-6 info">Air-conditioned<p
                            className="meta-data">{this.props.data.car.airConditioned}</p>
                        </p>
                        </div>

                        <div  className="comps" style={{margin: 15}}>
                        <p className="row-xs-6 info">Price<p
                            className="meta-data">{this.props.data.costPerPassenger} leva</p>
                        </p>
                            <hr/>
                        <p className="row-xs-6 info">Luggage allowed<p
                            className="meta-data">{this.props.data.luggageAllowed}</p>
                        </p>
                        </div>

                        <div className="comps"  style={{margin: 15}}>
                        <p className="row-xs-6 info">Status<p className="meta-data">{this.props.data.tripStatus}</p></p>
                            <hr/>
                        <p className="row-xs-6 info">Pets allowed<p
                            className="meta-data">{this.props.data.petsAllowed}</p></p>
                        </div>

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
        driverImage: state.user.driverImage,
        modelId:state.user.modelId
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId, userType,modelId) => dispatch(actions.fetchImageUser(token, userId, userType,modelId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Trip, axios));
