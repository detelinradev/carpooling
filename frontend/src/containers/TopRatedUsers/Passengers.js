import React, { Component } from 'react';
import Avatar from '../../assets/images/image-default.png';
import './Passengers.css';
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../axios-baseUrl";

class Passengers extends Component {
    state = {
        src: Avatar
    };

    async componentDidMount() {

        const headers = {
            "Content-Type":"application/json",
            'Authorization':this.props.token
        };

        const getPassengerAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.passenger.username,{headers})
                .then(response => response.blob());

        if(getPassengerAvatarResponse.size>100){
            this.setState({
                src: URL.createObjectURL(getPassengerAvatarResponse)
            })
        }

    }

    render() {
        return (
            <div className="Passenger">
                <img  src={this.state.src} alt="Not found!"/>
                <span className="header"> {this.props.passenger.username}</span>
                <div className="divech">
                    Passenger rating  <span className="header-two"> {this.props.passenger.ratingAsPassenger}</span>
                </div>
            </div>
        )
    }

}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(Passengers, axios));
