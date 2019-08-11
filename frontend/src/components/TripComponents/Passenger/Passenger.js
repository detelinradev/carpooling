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
        src: Avatar
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

    render() {

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
                </div>
            </div>)
    }

}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
        passengerImage: state.user.passengerImage,
        modelId: state.user.modelId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId, userType, modelId) => dispatch(actions.fetchImageUser(token, userId, userType, modelId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Passenger, axios));
