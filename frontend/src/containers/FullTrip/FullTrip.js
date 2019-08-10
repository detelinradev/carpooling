import React, {Component} from 'react';
import {connect} from 'react-redux';
import './FullTrip.css';
import axios from '../../axios-baseUrl';
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import Comment from '../../components/TripComponents/Comment/Comment'
import Passenger from '../../components/TripComponents/Passenger/Passenger'
import StarRatings from "react-star-ratings";
import {FaMedal} from "react-icons/fa";
import Car from "../../components/TripComponents/Car/Car";
import * as actions from "../../store/actions";
import Button from "@material-ui/core/Button";
import {FaUserEdit} from "react-icons/fa";
import Avatar from "../../assets/images/image-default.png";

class FullTrip extends Component {
    state = {
        src: Avatar,
    };

    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.trip.driver.modelId, 'driver',this.props.trip.modelId);
        console.log(this.state.src)
        if(this.props.driverImage){
            this.setState({
                src: this.props.driverImage
            })
        }
        console.log(this.state.src)
    }

    async joinTrip() {
        const currentUserName = this.props.username;
        if (this.props.trip.driver.username !== currentUserName) {
            axios.post('/trips/' + this.props.trip.modelId + '/passengers', null, {
                headers: {"Authorization": this.props.token}
            }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'Yes'))
                .then(res => this.componentDidMount());
        }
    }

    render() {
        let comments;
        if (this.props.trip.comments) {
            comments = this.props.trip.comments.map(comment => (
                <Comment
                    key={comment.modelId}
                    data={comment}
                    message={comment.message}
                    author={comment.author}
                />
            ));
        }

        let passengers;
        if (this.props.trip.passengers) {
            passengers = this.props.trip.passengers.map(passenger => (
                <Passenger
                    key={passenger.id}
                    data={passenger}
                />
            ));
        }

        let car = (
            <Car
                data={this.props.trip.car}
            />
        );

        let joinTripStatus='Trip joined';
        if(this.props.tripJoined === 'Join Trip'){
            joinTripStatus = 'Join trip';
            if(this.props.requestSent === 'Yes'){
                joinTripStatus = 'Request sent'
            }
        }
        if(this.props.tripJoined === 'Request sent'){
            joinTripStatus = 'Request Sent'
        }
        if(this.props.tripJoined === ''){
            joinTripStatus = ''
        }


        let trip = <Spinner/>;
        if (!this.props.loading) {
            trip = (
                <div style={{marginLeft: 100}}>
                    <div
                        className="proba Trip additional-details hed">{this.props.trip.origin} -> {this.props.trip.destination}</div>
                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.src} alt={''}/>
                            <p className="meta-data">{this.props.trip.driver.firstName} {this.props.trip.driver.lastName}</p>
                        </p>
                        <h3><span><FaMedal/></span> Rating <span
                            className="header"><h1>{
                            <StarRatings
                                rating={this.props.trip.ratingAsDriver}
                                starRatedColor="blue"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rating'
                            />}</h1></span></h3>
                        <div style={{marginRight: 20, verticalAlign: "middle"}}>
                            <Button onClick={() => this.joinTrip()}><h3
                                className="header">{joinTripStatus} <FaUserEdit/></h3>
                            </Button>
                        </div>
                        <p className="row-xs-6 info"> Departure Time<p
                            className="meta-data">{this.props.trip.departureTime}</p>
                        </p>
                        <p className="row-xs-6 info">Available Seats<p
                            className="meta-data">{this.props.trip.availablePlaces}</p>
                        </p>
                        <p className="row-xs-6 info">Price<p
                            className="meta-data">{this.props.trip.costPerPassenger} leva</p>
                        </p>
                        <p className="row-xs-6 info">Car<p
                            className="meta-data">{this.props.trip.car.brand} {this.props.trip.car.model}</p></p>
                        <p className="row-xs-6 info">Status<p className="meta-data">{this.props.trip.tripStatus}</p></p>
                        <p className="row-xs-3 info">Message<p className="meta-data">{this.props.trip.message}</p></p>
                        <p className="row-xs-6 info">Smoking allowed<p
                            className="meta-data">{this.props.trip.smokingAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Pets allowed<p
                            className="meta-data">{this.props.trip.petsAllowed}</p></p>
                        <p className="row-xs-6 info">Luggage allowed<p
                            className="meta-data">{this.props.trip.luggageAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Air-conditioned<p
                            className="meta-data">{this.props.trip.car.airConditioned}</p>
                        </p>


                    </div>
                    {car}
                    <div className="Comment">
                        <div>
                            <Button ><h3
                                className="header">+ADD COMMENT<FaUserEdit/></h3>
                            </Button>
                        </div>
                        <br/>
                        {comments}
                    </div>
                    <div style={{float: "left"}}>
                        <h1>
                            PASSENGERS
                        </h1>
                        {passengers}
                    </div>
                </div>
            )
        }

        return (
            <div className="todore">
                <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                {trip}
            </div>
        )

    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        trip: state.trip.trip,
        driverImage: state.user.driverImage,
        username: state.auth.userId,
        tripJoined:state.trip.tripJoined,
        requestSent:state.trip.requestSent,
        modelId:state.user.modelId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId, userType,modelId) => dispatch(actions.fetchImageUser(token, userId, userType,modelId)),
        onFetchTrip: (token, tripId,requestSent) => dispatch(actions.fetchTrip(token, tripId,requestSent)),
        // onTripJoined:(tripJoinedStatus) => dispatch(actions.changeTripJoinedStatus(tripJoinedStatus))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(FullTrip, axios));