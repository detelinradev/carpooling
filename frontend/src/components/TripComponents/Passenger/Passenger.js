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
        // this.props.onFetchUserImage(this.props.token, this.props.data.modelId,'passenger',this.props.data.modelId);

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.data.modelId)
                .then(response => response.blob());

        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
    }

    giveFeedbackHandler = async () => {
        console.log(this.props.trip.modelId)
        console.log( this.props.data.modelId)
        let feedback = this.state.newFeedback;
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/passengers/" + this.props.data.modelId +"/feedback", {feedback}, {
            headers: {"Authorization": this.props.token}
        });
    };

    ratePassengerHandler = async () => {
        let rate = this.state.newRate;
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/passengers/" + this.props.data.modelId+"/rate", {rate}, {
            headers: {"Authorization": this.props.token}
        });
    };
    feedbackInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newFeedback: event.target.value});
    };

    rateInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newRating: event.target.value});
    };


    render() {
        let formFeedback = null;
        if (this.props.isMyTrip) {
            formFeedback = (
                <div>
                    <p
                        className="header">->GIVE FEEDBACK
                    </p>
                    <input
                        value={this.state.newComment}
                        onChange={(event) => this.feedbackInputChangedHandler(event)}/>
                </div>
            )
        }

        let formRating = null;
        if (this.props.isMyTrip) {
            formRating = (
                <div>
                    <p
                        className="header">*RATE THE PASSENGER
                    </p>
                    <input
                        value={this.state.newComment}
                        onChange={(event) => this.rateInputChangedHandler(event)}/>
                </div>
            )
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
                    <div>
                        <form onSubmit={this.ratePassengerHandler}>
                            {formRating}
                        </form>
                    </div>
                    <div>
                        <form onSubmit={this.giveFeedbackHandler}>
                            {formFeedback}
                        </form>
                    </div>
                </div>

            </div>)
    }

}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
        passengerImage: state.user.passengerImage,
        modelId: state.user.modelId,
        isMyTrip: state.trip.isMyTrip
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId, userType, modelId) => dispatch(actions.fetchImageUser(token, userId, userType, modelId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Passenger, axios));
