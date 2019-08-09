import React, {Component} from 'react';
import './passenger.css';
import StarRatings from "react-star-ratings";
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";

class Passenger extends Component {


    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.data.modelId,'passenger');
    }
    render() {
        let image = 'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg';
        if(this.props.passengerImage)
            image = this.props.passengerImage;

        return (
            <div className=" Post">

                <div className="Trip additional-details  cardcont  meta-data-container">
                    <h2>Passenger:</h2> <p className="meta-data">{this.props.data.firstName} {this.props.data.lastName}</p>
                    <p className="image">
                        <img id="postertest" className='poster' style={{width: 128}}
                             src={image} alt={''}/></p>

                    <p className="row-xs-6 info">Rating<p className="meta-data">{
                        <StarRatings
                            rating={this.props.data.rating}
                            starRatedColor="blue"
                            //changeRating={this.changeRating}
                            numberOfStars={5}
                            name='rating'
                        />}</p></p>
                </div>
            </div>)
    }

}
const mapStateToProps = state => {
    return {
        token: state.auth.token,
        passengerImage:state.user.passengerImage
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId,userType) => dispatch(actions.fetchImageUser(token, userId,userType)),
    };
};

export default connect(mapStateToProps,mapDispatchToProps)(withErrorHandler(Passenger, axios));
