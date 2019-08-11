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
import Dropdown from 'react-dropdown'
import 'react-dropdown/style.css'
import DropdownButton from "react-bootstrap/DropdownButton";

class FullTrip extends Component {
    state = {
        src: Avatar,
        tripStatus:''
    };

    async componentDidMount() {
        //this.props.onFetchUserImage(this.props.token, this.props.trip.driver.modelId, 'driver',this.props.trip.modelId);
        // console.log(this.state.src)
        const getDriverAvatarResponse = await
        fetch("http://localhost:8080/users/avatar/" + this.props.trip.driver.modelId)
            .then(response => response.blob());


        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
        if(this.state.tripStatus !== ''){

        }

    }

    async joinTrip() {
        // console.log(this.props.trip.modelId)
        const currentUserName = this.props.username;
        if (this.props.trip.driver.username !== currentUserName) {
            axios.post('/trips/' + this.props.trip.modelId + '/passengers', null, {
                headers: {"Authorization": this.props.token}
            }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'Yes'));
        }
    }
   // getTripStatus(tripStatus){
   //     this.setState({tripStatus: tripStatus})
   //  }
    async changeTripStatus(tripStatus){
        axios.post('/trips/' + this.props.trip.modelId + '?' +tripStatus, null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'Yes'));
    }

    async cancelTrip(){
        const currentUserName = this.props.username;
        let passengerID =this.props.trip.passengers.map(passenger =>
            (passenger.username === currentUserName)?passenger.modelId:null
        );
        axios.patch('/trips/' + this.props.trip.modelId + '/passengers/' + passengerID + '?status=CANCELED', null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.onFetchTrip(this.props.token, this.props.trip.modelId, 'No'));
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


        let myTrip='';
        if(this.props.isMyTrip !== ''){
            if(this.props.isMyTrip === 'driver'){
                // const optionsDriver = [
                //     'ONGOING', 'DONE', 'CANCELED'
                // ];
                // const defaultOption = optionsDriver[0];

                myTrip=(
                   <div>
                       {/*<DropdownButton id="dropdown-item-button" title="Dropdown button">*/}
                       {/*    <Dropdown.Item as= <Button onClick={() => this.changeTripStatus('ONGOING')}>ONGOING</Dropdown.Item>*/}
                       {/*    <Dropdown.Item as= <Button onClick={() => this.changeTripStatus('ONGOING')}>ONGOING</Dropdown.Item>*/}
                       {/*    <Dropdown.Item as= <Button onClick={() => this.changeTripStatus('ONGOING')}>ONGOING</Dropdown.Item>*/}
                       {/*</DropdownButton>*/}
                   </div>
                )
            }
            if(this.props.isMyTrip === 'passenger'){

                myTrip=(
                <div style={{marginRight: 20, verticalAlign: "middle"}}>
                    <Button onClick={() => this.cancelTrip()}><h3
                        className="header">CANCEL TRIP PARTICIPATION<FaUserEdit/></h3>
                    </Button>
                </div>
                )
            }
        }


        let trip = <Spinner/>;
        if (!this.props.loading) {
            trip = (
                <div style={{marginLeft: 100}}>
                    <div
                        className="proba Trip additional-details hed">{this.props.trip.origin} -> {this.props.trip.destination}</div>
                    <div>{this.props.isMyTrip === 'driver'? 'Driver': null}</div>
                    <div>{this.props.isMyTrip === 'passenger'? 'Passenger': null}</div>
                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.src} alt={''}/>
                            <p className="meta-data">{this.props.trip.driver.firstName} {this.props.trip.driver.lastName}</p>
                            <span><FaMedal/></span> Rating <span
                            className="header">{
                            <StarRatings
                                rating={this.props.trip.ratingAsDriver}
                                starRatedColor="yellow"
                                changeRating={this.changeRating}
                                numberOfStars={5}
                                name='rating'
                                starDimension="30px"
                                starSpacing="6px"
                            />}</span>
                        </p>
                        <div style={{marginRight: 20, verticalAlign: "middle"}}>
                            <Button onClick={() => this.joinTrip()}><h3
                                className="header">{joinTripStatus} <FaUserEdit/></h3>
                            </Button>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Departure Time<p className="row-xs-6 info meta-data">{this.props.trip.departureTime}</p>

                            <hr/>
                            Smoking allowed<p className="row-xs-6 info meta-data">{this.props.trip.smokingAllowed}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Available Seats<p className="row-xs-6 info meta-data">{this.props.trip.availablePlaces}</p>
                            <hr/>
                            Air-conditioned<p className="row-xs-6 info meta-data">{this.props.trip.car.airConditioned}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Price<br/><p className="row-xs-6 info meta-data">{this.props.trip.costPerPassenger} leva</p>
                            <hr/>
                            Luggage allowed<p className="row-xs-6 info meta-data">{this.props.trip.luggageAllowed}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Status<br/><p className="row-xs-6 info meta-data">{this.props.trip.tripStatus}</p>
                            <hr/>
                            Pets allowed<p className="row-xs-6 info meta-data">{this.props.trip.petsAllowed}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Car Brand<br/><p className="row-xs-6 info meta-data">{this.props.trip.car.brand}</p>
                            <hr/>
                            Car Model<p className="row-xs-6 info meta-data">{this.props.trip.car.model}</p>
                        </div>

                        <div className="comps" style={{ paddingTop: 40}}>
                            Message<p className="row-xs-6 info meta-data">{this.props.trip.message}</p>
                        </div>
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
                    {myTrip}
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
        // driverImage: state.user.driverImage,
        username: state.auth.userId,
        tripJoined:state.trip.tripJoined,
        requestSent:state.trip.requestSent,
        // modelId:state.user.modelId,
        isMyTrip: state.trip.isMyTrip

    }
};
const mapDispatchToProps = dispatch => {
    return {
        // onFetchUserImage: (token, userId, userType,modelId) => dispatch(actions.fetchImageUser(token, userId, userType,modelId)),
        onFetchTrip: (token, tripId,requestSent) => dispatch(actions.fetchTrip(token, tripId,requestSent)),
        // onTripJoined:(tripJoinedStatus) => dispatch(actions.changeTripJoinedStatus(tripJoinedStatus))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(FullTrip, axios));