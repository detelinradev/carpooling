import React, {Component} from 'react';
import './Trip.css';
import Button from "@material-ui/core/Button";
import {FaMedal, FaUserEdit} from "react-icons/fa";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import * as actions from "../../../store/actions";
import Avatar from "../../../assets/images/image-default.png";
import StarRatings from "react-star-ratings";

class Trip extends Component {
    state = {
        src: Avatar,
        rating:0
    };

    async componentDidMount() {
            const getDriverAvatarResponse = await
                fetch("http://localhost:8080/users/avatar/" + this.props.data.driver.modelId)
                    .then(response => response.blob());

            if (getDriverAvatarResponse.size > 100) {
                this.setState({
                    src: URL.createObjectURL(getDriverAvatarResponse)
                })
            }

        const getMeResponse = await
            axios.get('/users/' +this.props.data.driver.username, {
                headers:
                    {"Authorization": this.props.token}
            });

        this.setState({
            rating: getMeResponse.data.ratingAsDriver

        })
    }

    render() {

          let trip = (
                <div className=" Post">
                    <div
                        className="proba Trip additional-details hed">{this.props.data.origin} -> {this.props.data.destination}</div>
                    <div>{this.props.userRole?this.props.userRole:null}</div>
                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <div className="image ">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.src} alt={''}/>
                            <p className="meta-data">{this.props.data.driver.firstName} {this.props.data.driver.lastName}</p>
                            <span><FaMedal/></span> Rating <div
                            className="header">{
                            <StarRatings
                                rating={this.state.rating}
                                starRatedColor="yellow"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rating'
                                starDimension="30px"
                                starSpacing="6px"
                            />}</div>
                        </div>
                        <div className="ed">
                            <Button onClick={() => this.props.showFullTrip(this.props.data)}><h3
                                className="header">DETAILS <FaUserEdit/></h3>
                            </Button>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Departure Time<p
                            className="row-xs-6 info meta-data">{this.props.data.departureTime}</p>

                            <hr/>
                            Smoking allowed<p
                            className="row-xs-6 info meta-data">{this.props.data.smokingAllowed}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Available Seats<p
                            className="row-xs-6 info meta-data">{this.props.data.availablePlaces}</p>
                            <hr/>
                            Air-conditioned<p
                            className="row-xs-6 info meta-data">{this.props.data.car.airConditioned}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Price<br/><p
                            className="row-xs-6 info meta-data">{this.props.data.costPerPassenger} leva</p>
                            <hr/>
                            Luggage allowed<p
                            className="row-xs-6 info meta-data">{this.props.data.luggageAllowed}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Status<br/><p className="row-xs-6 info meta-data">{this.props.data.tripStatus}</p>
                            <hr/>
                            Pets allowed<p
                            className="row-xs-6 info meta-data">{this.props.data.petsAllowed}</p>
                        </div>

                    </div>
                </div>
            )
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
       // driverImage: state.user.driverImage,
       //  modelId: state.user.modelId
    }
};

const mapDispatchToProps = dispatch => {
    return {
       // onFetchUserImage: (token, userId, userType, modelId) => dispatch(actions.fetchImageUser(token, userId, userType, modelId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Trip, axios));
