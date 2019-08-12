import React, {Component} from 'react';
import './passenger.css';
import StarRatings from "react-star-ratings";
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import Avatar from '../../../assets/images/image-default.png'


class Passenger extends Component {
    state = {
        src: Avatar,
        newFeedback: '',
        newRate: '',
    };

    async componentDidMount() {

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.data.modelId)
                .then(response => response.blob());

        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
    }

    giveFeedbackHandler = async (event) => {
        event.preventDefault();
        console.log(this.props.trip.modelId)
        console.log(this.props.data.modelId)
        let feedback = this.state.newFeedback;
        // console.log(feedback.value)
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/passengers/" + this.props.data.modelId + "/feedback", {feedback}, {
            headers: {"Authorization": this.props.token}
        });
        this.setState({newFeedback: ''});
    };

    ratePassengerHandler = async (event) => {
        event.preventDefault();
        let rate = this.state.newRate;
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/passengers/" + this.props.data.modelId + "/rate", {rate}, {
            headers: {"Authorization": this.props.token}
        });
        this.setState({newRate: ''});
    };
    feedbackInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newFeedback: event.target.value});
    };

    rateInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newRating: event.target.value});
    };

    async changePassengerStatus(passengerStatus) {
        axios.patch('/trips/' + this.props.trip.modelId + '/passengers/' + this.props.data.modelId + '?status=' + passengerStatus, null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'Yes'));
    }


    render() {
        let currentPassengerStatus = null;
        let currentPassengers = Object.entries(this.props.trip.passengerStatus).map(key =>
            (key[0].includes('username=' + this.props.data.username)) ? currentPassengerStatus = key[1] : null)
        console.log(currentPassengerStatus)
        console.log(this.props.data.username)
        if ((currentPassengerStatus === 'PENDING' && this.props.data.username === this.props.username)
            || (currentPassengerStatus === 'PENDING' && this.props.username === this.props.trip.driver.username)
            || (currentPassengerStatus === 'REJECTED' && this.props.data.username === this.props.username)
            || (currentPassengerStatus === 'REJECTED' && this.props.username === this.props.trip.driver.username)
            || (currentPassengerStatus !== 'PENDING' && currentPassengerStatus !== 'REJECTED')) {

            let formFeedback = null;
            let formRating = null;
            let changePassengerStatus = '';
            if (this.props.isMyTrip) {
                formFeedback = (
                    <div>
                        <form onSubmit={(event) => this.giveFeedbackHandler(event)}>
                            <p
                                className="header">->GIVE FEEDBACK
                            </p>
                            <input
                                value={this.state.newComment}
                                onChange={(event) => this.feedbackInputChangedHandler(event)}/>
                        </form>
                    </div>
                );
                formRating = (
                    <div>
                        <form onSubmit={(event) => this.ratePassengerHandler(event)}>
                            <p
                                className="header">*RATE THE PASSENGER
                            </p>
                            <input
                                value={this.state.newComment}
                                onChange={(event) => this.rateInputChangedHandler(event)}/>
                        </form>
                    </div>
                );
                if (this.props.tripRole === 'driver') {
                    changePassengerStatus = (
                        <div>
                            <div className="dropdown">
                                <button className="dropbtn">Change passenger status</button>
                                <div className="dropdown-content">
                                    <a onClick={() => this.changePassengerStatus('ACCEPTED')}>ACCEPTED</a>
                                    <a onClick={() => this.changePassengerStatus('REJECTED')}>REJECTED</a>
                                    <a onClick={() => this.changePassengerStatus('ABSENT')}>ABSENT</a>
                                </div>
                            </div>
                        </div>
                    )
                }
            }

            return (
                <div style={{float: "left"}} className=" Post">

                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <p className="meta-data">{this.props.data.firstName} {this.props.data.lastName}</p>
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.src} alt={''}/></p>

                        <p className="row-xs-6 info">Rating<p className="meta-data">{
                            <StarRatings
                                rating={this.props.data.rating}
                                starRatedColor="blue"
                                //changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rating'
                                starDimension="30px"
                                starSpacing="6px"
                            />}</p></p>
                        <p className="row-xs-6 info">Passenger status : {currentPassengerStatus}</p>
                        <div>
                            {formRating}
                        </div>
                        <div>
                            {changePassengerStatus}
                        </div>
                        <div>
                            {formFeedback}
                        </div>
                        <p className="meta-data">{this.props.data.passengerStatus}</p>
                    </div>

                </div>)
        } else {
            return null
        }
    }

}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
        // passengerImage: state.user.passengerImage,
        // modelId: state.user.modelId,
        tripRole: state.trip.tripRole,
        trip: state.trip.trip,
        passengerStatus: state.trip.passengerStatus,
        isMyTrip: state.trip.isMyTrip,
        username: state.auth.userId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        // onFetchUserImage: (token, userId, userType, modelId) => dispatch(actions.fetchImageUser(token, userId, userType, modelId)),
        onFetchTrip: (token, tripId, requestSent) => dispatch(actions.fetchTrip(token, tripId, requestSent)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Passenger, axios));
