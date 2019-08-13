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
import Modal from "../../components/UI/Modal/Modal";
import UpdateTrip from "./UpdateTrip/UpdateTrip";

class FullTrip extends Component {
    state = {
        src: Avatar,
        newComment: '',
        rating: 0,
        newRate: null,
        newFeedback: null,
        showModal: false,
    };

    async componentDidMount() {
        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.trip.driver.modelId)
                .then(response => response.blob());


        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
        this.getDriverRate()

    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.tripUpdate) {
            this.props.history.push('/MyTrips');
            this.props.onTripFinishUpdate(false)
        }
    }

    async getDriverRate() {
        const getMeResponse = await
            axios.get('/users/' + this.props.trip.driver.username, {
                headers:
                    {"Authorization": this.props.token}
            });

        this.setState({
            rating: getMeResponse.data.ratingAsDriver

        })
    }

    async joinTrip() {
        const currentUserName = this.props.username;
        if (this.props.trip.driver.username !== currentUserName) {
            axios.post('/trips/' + this.props.trip.modelId + '/passengers', null, {
                headers: {"Authorization": this.props.token}
            }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'PENDING'));
        }
    }

    async deleteTrip() {
        axios.patch('/trips/' + this.props.trip.modelId + '/delete', null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.history.push('/myTrips'));

    }

    async changeTripStatus(tripStatus) {
        axios.patch('/trips/' + this.props.trip.modelId + '?status=' + tripStatus, null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId));
    }

    async cancelTrip() {
        const currentUserName = this.props.username;
        let passengerId = null;
        let passengers = this.props.trip.passengers.map(passenger =>
            (passenger.username === currentUserName) ? passengerId = passenger.modelId : null
        );
        axios.patch('/trips/' + this.props.trip.modelId + '/passengers/' + passengerId + '?status=CANCELED', null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'CANCELED'));
        this.props.history.push('/myTrips')
    }

    inputChangedHandler = (event) => {
        this.setState({newComment: event.target.value});
    };


    async commentHandler(event) {
        event.preventDefault();
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/comments?comment=" + this.state.newComment, null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId));
        this.setState({newComment: ''});
    };

    rateDriverHandler = async (event) => {
        event.preventDefault();
        let rate = this.state.newRate;
        console.log(rate.toString())
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/driver/rate", this.state.newRate, {
            headers: {"Authorization": this.props.token, "Content-Type": "application/json"}
        }).then(res => this.getDriverRate());
        this.setState({newRate: ''});
    };

    rateInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newRate: event.target.value});
    };

    feedbackInputChangedHandler = (event) => {
        event.preventDefault();
        this.setState({newFeedback: event.target.value});
    };

    giveFeedbackHandler = async (event) => {
        event.preventDefault();
        await axios.post("http://localhost:8080/trips/" + this.props.trip.modelId + "/driver/feedback", this.state.newFeedback, {
            headers: {"Authorization": this.props.token, "Content-Type": "application/json"}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId));
        this.setState({newFeedback: ''});
    };

    toggleModal() {
        this.setState({
            showModal: !this.state.showModal
        })
    }


    editCloseHandler() {
        this.setState({
            showModal: !this.state.showModal
        });
    }


    render() {
        let comments;
        if (this.props.trip.comments) {
            comments = this.props.trip.comments.map(comment => (
                <Comment
                    key={comment.modelId}
                    isMyTrip={this.props.isMyTrip}
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
                    isMyTrip={this.props.isMyTrip}
                    data={passenger}
                    // trip={this.props.trip}
                />
            ));
        }

        let car = (
            <Car
                data={this.props.trip.car}
            />
        );
        let joinTripStatus = null;
        if (this.props.tripRole !== 'driver') {
            joinTripStatus = 'Join trip';
            if (this.props.passengerStatus) {
                if (this.props.passengerStatus === 'PENDING') {
                    joinTripStatus = 'Request sent';
                }
                if (this.props.passengerStatus === 'ACCEPTED') {
                    joinTripStatus = 'Trip joined'
                }
                if (this.props.passengerStatus === 'REJECTED') {
                    joinTripStatus = 'Driver denied'
                }
                if (this.props.passengerStatus === 'ABSENT') {
                    joinTripStatus = 'You missed it'
                }
                if (this.props.passengerStatus === 'CANCELED') {
                    joinTripStatus = 'You canceled it'
                }
            }
        }
        let joinTrip = null;
        if (this.props.trip.tripStatus === 'AVAILABLE' && this.props.tripRole !== 'driver')
            joinTrip = (
                <div style={{marginRight: 20, verticalAlign: "middle"}}>
                    <Button onClick={() => this.joinTrip()}><h3
                        className="header">{joinTripStatus} </h3>
                    </Button>
                </div>
            );


        let buttonUpdateTrip = null;
        let updateTrip = null;
        let formFeedback = null;
        let formChangeTripStatus = null;
        let formComment = null;
        let formRating = null;
        let formDeleteTrip = null;
        if (this.props.isMyTrip) {

            formComment = (
                <div>
                    <form onSubmit={(event) => this.commentHandler(event)}>
                        {
                            <div>
                                <p className="header">+ADD COMMENT</p>
                                <input
                                    value={this.state.newComment}
                                    onChange={(event) => this.inputChangedHandler(event)}/>
                            </div>
                        }
                    </form>
                </div>
            );

            if (this.props.tripRole === 'passenger' && this.props.trip.tripStatus === 'DONE' && this.props.passengerStatus !== "PENDING") {
                formRating = (
                    <div>
                        <form onSubmit={(event) => this.rateDriverHandler(event)}>
                            <p
                                className="header">RATE DRIVER
                            </p>
                            <input
                                value={this.state.newRate}
                                onChange={(event) => this.rateInputChangedHandler(event)}/>
                        </form>
                    </div>
                );
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
            }
            if (this.props.tripRole === 'passenger' && this.props.passengerStatus !== 'CANCELED') {

                formChangeTripStatus = (
                    <div style={{marginRight: 20, verticalAlign: "middle"}}>
                        <Button onClick={() => this.cancelTrip()}><h3
                            className="header">LEAVE TRIP</h3>
                        </Button>
                    </div>
                );
            }

            if (this.props.tripRole === 'driver') {

                formDeleteTrip = (
                    <div style={{marginRight: 20, verticalAlign: "middle"}}>
                        <Button onClick={() => this.deleteTrip()}><h3
                            className="header">DELETE TRIP</h3>
                        </Button>
                    </div>
                );

                buttonUpdateTrip = (
                    <div style={{marginRight: 20, verticalAlign: "middle"}}>
                        <Button onClick={() => this.toggleModal()}><h3
                            className="header">UPDATE TRIP</h3>
                        </Button>
                    </div>
                );
                updateTrip = (

                    <Modal style={{width: 600}} show={this.state.showModal} modalClosed={() => this.editCloseHandler()}>
                        <UpdateTrip
                            showModal={this.state.showModal}
                            data={this.props.trip}
                        />
                    </Modal>

                );

                formChangeTripStatus = (
                    <div>
                        <div className="dropdown">
                            <button className="dropbtn">Change trip status</button>
                            <div className="dropdown-content">
                                <a onClick={() => this.changeTripStatus('ONGOING')}>ONGOING</a>
                                <a onClick={() => this.changeTripStatus('DONE')}>DONE</a>
                                <a onClick={() => this.changeTripStatus('CANCELED')}>CANCELED</a>
                            </div>
                        </div>
                    </div>
                )
            }
        }


        let trip = <Spinner/>;
        console.log(this.props.trip)
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
                            <span><FaMedal/></span> Rating <span
                            className="header">{
                            <StarRatings
                                rating={this.state.rating}
                                starRatedColor="yellow"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rating'
                                starDimension="30px"
                                starSpacing="6px"
                            />}</span>
                        </p>
                        <div>
                        {formRating}
                        {formFeedback}
                        {formDeleteTrip}
                        {joinTrip}
                        {buttonUpdateTrip}
                        {formChangeTripStatus}
                        </div>

                        <div className="comps" style={{paddingTop: 30}}>
                            Departure Time<p style={{fontSize: 18}}
                                             className="row-xs-6 info meta-data">{this.props.trip.departureTime}</p>

                            <hr/>
                            Smoking allowed<p className="row-xs-6 info meta-data">{this.props.trip.smokingAllowed}</p>
                        </div>

                        <div className="comps" style={{paddingTop: 25}}>
                            Available Seats<p className="row-xs-6 info meta-data">{this.props.trip.availablePlaces}</p>
                            <hr/>
                            Air-conditioned<p
                            className="row-xs-6 info meta-data">{this.props.trip.car.airConditioned}</p>
                        </div>

                        <div className="comps" style={{paddingTop: 40}}>
                            Price<br/><p className="row-xs-6 info meta-data">{this.props.trip.costPerPassenger} leva</p>
                            <hr/>
                            Luggage allowed<p className="row-xs-6 info meta-data">{this.props.trip.luggageAllowed}</p>
                        </div>

                        <div className="comps" style={{paddingTop: 40}}>
                            Status<br/><p className="row-xs-6 info meta-data">{this.props.trip.tripStatus}</p>
                            <hr/>
                            Pets allowed<p className="row-xs-6 info meta-data">{this.props.trip.petsAllowed}</p>
                        </div>

                        <div className="comps" style={{paddingTop: 40}}>
                            Car Brand<br/><p className="row-xs-6 info meta-data">{this.props.trip.car.brand}</p>
                            <hr/>
                            Car Model<p className="row-xs-6 info meta-data">{this.props.trip.car.model}</p>
                        </div>

                        <div className="comps" style={{paddingTop: 40}}>
                            Message<p className="row-xs-6 info meta-data">{this.props.trip.message}</p>
                        </div>
                    </div>
                    {car}
                    <div className="Comments">
                        Comments
                        {formComment}
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
            <div>
                <div className="todore">
                    <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                    {trip}
                </div>
                <div >
                    {updateTrip}
                </div>
            </div>

        )

    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        trip: state.trip.trip,
        username: state.auth.userId,
        tripRole: state.trip.tripRole,
        passengerStatus: state.trip.passengerStatus,
        isMyTrip: state.trip.isMyTrip,
        tripUpdate: state.trip.tripUpdated

    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrip: (token, tripId, passengerStatus) => dispatch(actions.fetchTrip(token, tripId, passengerStatus)),
        onTripFinishUpdate: (tripUpdated) => dispatch(actions.tripFinishUpdate(tripUpdated))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(FullTrip, axios));