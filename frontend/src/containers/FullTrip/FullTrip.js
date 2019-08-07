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

class FullTrip extends Component {

    state={
        image:""
    };


    async componentDidMount() {

        const fetchUserImage = await
            fetch('http://localhost:8080/users/avatar/' + this.props.trip.driver.modelId,
                {headers: {"Authorization": this.props.token}})
                .then(response => response.blob());

        if (fetchUserImage.size > 100) {
            this.setState({
                image: URL.createObjectURL(fetchUserImage)
            })
        }
    }

    render() {
        let comments = this.props.trip.comments.map(comment => (
            <Comment
                key={comment.modelId}
                message={comment.message}
                author={comment.author}
            />
        ));

        let passengers = this.props.trip.passengers.map(passenger => (
            <Passenger
                key={passenger.id}
                data={passenger}
            />
        ));

        let car = (
            <Car
                data={this.props.trip.car}
            />
        );

        let trip = <Spinner/>;
        if (!this.props.loading) {
            trip = (
                <div className=" Post">
                    <div
                        className="proba Trip additional-details hed">{this.props.trip.origin} -> {this.props.trip.destination}</div>
                    <div className="Trip additional-details  cardcont  meta-data-container">
                        <p className="image">
                            <img id="postertest" className='poster' style={{width: 128}}
                                 src={this.state.image} alt={''}/>
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
                            className="meta-data">{this.props.trip.car.smokingAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Pets allowed<p
                            className="meta-data">{this.props.trip.car.petsAllowed}</p></p>
                        <p className="row-xs-6 info">Luggage allowed<p
                            className="meta-data">{this.props.trip.car.luggageAllowed}</p>
                        </p>
                        <p className="row-xs-6 info">Air-conditioned<p
                            className="meta-data">{this.props.trip.car.airConditioned}</p>
                        </p>
                        {comments}
                        {passengers}

                    </div>
                    {car}
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
        // userImage: state.trip.userImage
    }
};
// const mapDispatchToProps = dispatch => {
    // return {
    //     onFetchUserImage: (token, userId) => dispatch(actions.fetchImageUser(token, userId)),
    // };
// };

export default connect(mapStateToProps)(withErrorHandler(FullTrip, axios));