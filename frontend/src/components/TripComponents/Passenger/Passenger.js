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
        rating: 0,
    };

    async componentDidMount() {

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.userName, {
                headers:
                    {"Authorization": this.props.token}
            })
                .then(response => response.blob());

        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
        const getMeResponse = await
            axios.get('/users/' + this.props.userName, {
                headers:
                    {"Authorization": this.props.token}
            });

        if (getMeResponse) {
            this.setState({
                rating: getMeResponse.data.ratingAsPassenger

            })
        }
    }

    feedbackInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newFeedback: event.target.value});
    };

    giveFeedbackHandler = async (event) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/users/feedback/" + this.props.trip.modelId + "/user/" + this.props.modelId, this.state.newFeedback, {
            headers: {"Authorization": this.props.token, "Content-Type": "application/json"}
        }).then(res => {
            this.props.onFetchTrip(this.props.token, this.props.trip.modelId);
            if (!res.response)
                this.props.showMessage('Feedback successfully added');
        }).catch(err => {
            this.props.showMessage('Request was not completed');
        });
        this.setState({newFeedback: ''});
    };

    ratePassengerHandler = async (event) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/users/rate/" + this.props.trip.modelId + "/user/" + this.props.modelId, this.state.newRate, {
            headers: {"Authorization": this.props.token, "Content-Type": "application/json"}
        }).then(res => {
            this.props.onFetchTrip(this.props.token, this.props.trip.modelId);
            if (!res.response)
                this.props.showMessage('Passenger successfully rated');
        }).catch(err => {
            this.props.showMessage('Request was not completed');
        });
        this.setState({newRate: ''});
    };

    rateInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newRate: event.target.value});
    };

    async changePassengerStatus(passengerStatus) {
        axios.patch('/trips/' + this.props.trip.modelId + '/passengers/' + this.props.modelId + '?status=' + passengerStatus, null, {
            headers: {"Authorization": this.props.token}
        }).then(res => {
            this.props.onFetchTrip(this.props.token, this.props.trip.modelId);
            if (!res.response)
                this.props.showMessage('Passenger status successfully changed to ' + passengerStatus);
        }).catch(err => {
            this.props.showMessage('Request was not completed');
        });

    }

    render() {
        let currentPassengerStatus = null;
        let currentPassengers = Object.entries(this.props.trip.passengerStatus).map(key =>
            (key[0].includes('username=' + this.props.userName)) ? currentPassengerStatus = key[1] : null);

        if (
            (currentPassengerStatus === 'PENDING' && this.props.userName === this.props.username)
            || (currentPassengerStatus === 'PENDING' && this.props.username === this.props.trip.driver.username)
            || (currentPassengerStatus === 'REJECTED' && this.props.userName === this.props.username)
            || (currentPassengerStatus !== 'PENDING' && currentPassengerStatus !== 'REJECTED')
        ) {

            let formFeedback = null;
            let formRating = null;
            let changePassengerStatus = '';
            if (this.props.isMyTrip) {
                let currentPassengerStatus = null;
                let some = Object.entries(this.props.trip.passengerStatus).map(key =>
                    (key[0].includes('username=' + this.props.userName)) ? currentPassengerStatus = key[1] : null);

                if (this.props.tripRole === 'driver'
                    && this.props.trip.tripStatus === "DONE"
                    && currentPassengerStatus !== "PENDING"
                ) {
                    formFeedback = (
                        <div>
                            <form onSubmit={(event) => this.giveFeedbackHandler(event)}>
                                <p
                                    className="header">LEAVE FEEDBACK
                                </p>
                                <input
                                    value={this.state.newFeedback}
                                    onChange={(event) => this.feedbackInputChangedHandler(event)}/>
                            </form>
                        </div>
                    );
                    formRating = (
                        <div>
                            <form onSubmit={(event) => this.ratePassengerHandler(event)}>
                                <p
                                    className="header">RATE PASSENGER
                                </p>
                                <input
                                    value={this.state.newRate}
                                    onChange={(event) => this.rateInputChangedHandler(event)}/>
                            </form>
                        </div>
                    );
                }

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
                        <p className="meta-data">{this.props.firstName} {this.props.lastName}</p>
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.src} alt={''}/></p>

                        <p style={{width: 240}} className="row-xs-6 info">Rating<p className="meta-data">{
                            <StarRatings
                                rating={this.state.rating}
                                starRatedColor="yellow"
                                numberOfStars={5}
                                name='rating'
                                starDimension="20px"
                                starSpacing="4px"
                            />}</p></p>
                        <p style={{marginTop: 20}} className="row-xs-6 info">Passenger status
                            : {currentPassengerStatus}</p>
                        <div style={{marginTop: 20}}>
                            {changePassengerStatus}
                        </div>
                        <a>
                            {formRating}
                        </a>
                        <a>
                            {formFeedback}
                        </a>
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
        tripRole: state.trip.tripRole,
        trip: state.trip.trip,
        passengerStatus: state.trip.passengerStatus,
        isMyTrip: state.trip.isMyTrip,
        username: state.auth.userId,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrip: (token, tripId) => dispatch(actions.fetchTrip(token, tripId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Passenger, axios));
