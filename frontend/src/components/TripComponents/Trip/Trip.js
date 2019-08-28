import React, {Component} from 'react';
import './Trip.css';
import Button from "@material-ui/core/Button";
import {FaMedal, FaUserEdit} from "react-icons/fa";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import Avatar from "../../../assets/images/image-default.png";
import StarRatings from "react-star-ratings";

class Trip extends Component {
    state = {
        src: Avatar,
        rating: 0
    };

    async componentDidMount() {

        const headers = {
            "Content-Type":"application/json",
            'Authorization':this.props.token
        };

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.data.driver.username,{headers})
                .then(response => response.blob());

         if (getDriverAvatarResponse.size > 100) {
             this.setState({
                 src: URL.createObjectURL(getDriverAvatarResponse)
             })
         }


        const getMeResponse = await
            axios.get('/users/' + this.props.data.driver.username, {headers});

        this.setState({
            rating: getMeResponse.data.ratingAsDriver

        })
    }

    render() {

        let trip = (
            <div className=" Post">
                <div style={{paddingLeft: 25}} className="proba Trip additional-details hed">
                    <p style={{float: "right"}}>{this.props.data.origin} -> {this.props.data.destination} &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; {this.props.userRole?this.props.userRole:null}</p>
                </div>
                <div className="Trip additional-details  cardcont  meta-data-container">
                    <div className="image ">
                        <img id="postertest" className='poster' style={{width: 128}}
                             src={this.state.src} alt={''}/>
                        <p className="meta-data">{this.props.data.driver.firstName} {this.props.data.driver.lastName}</p>
                        <span><FaMedal/></span> Rating <div
                        className="header">{
                        <StarRatings
                            rating={this.props.data.ratingAsDriver}
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

                    <div className="comps" style={{paddingTop: 40}}>
                        Departure Time<p
                        className="row-xs-6 info meta-data">{this.props.data.departureTime}</p>

                        <hr/>
                        Smoking allowed<p
                        className="row-xs-6 info meta-data">{this.props.data.smokingAllowed}</p>
                    </div>

                    <div className="comps" style={{paddingTop: 40}}>
                        Available Seats<p
                        className="row-xs-6 info meta-data">{this.props.data.availablePlaces}</p>
                        <hr/>
                        Air-conditioned<p
                        className="row-xs-6 info meta-data">{this.props.data.car.airConditioned}</p>
                    </div>

                    <div className="comps" style={{paddingTop: 40}}>
                        Price<br/><p
                        className="row-xs-6 info meta-data">{this.props.data.costPerPassenger} leva</p>
                        <hr/>
                        Luggage allowed<p
                        className="row-xs-6 info meta-data">{this.props.data.luggageAllowed}</p>
                    </div>

                    <div className="comps" style={{paddingTop: 40}}>
                        Status<br/><p className="row-xs-6 info meta-data">{this.props.data.tripStatus}</p>
                        <hr/>
                        Pets allowed<p
                        className="row-xs-6 info meta-data">{this.props.data.petsAllowed}</p>
                    </div>

                </div>
            </div>
        );
        return (
            <div>
                {trip}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
        username: state.auth.userId,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        // onDismountTrip: () => dispatch(actions.dismountTrip()),
    };
};

export default connect(mapStateToProps,mapDispatchToProps)(withErrorHandler(Trip, axios));
