import React, { Component } from 'react';
import Avatar from '../../assets/images/image-default.png';
import './Passenger.css';
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import CarAvatar from "../../assets/images/218567006-abstract-car-wallpapers.jpg";

class Passenger extends Component {
    state = {
        src: Avatar
    };

    async componentDidMount() {
        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.passenger.modelId)
                .then(response => response.blob());
        if(getDriverAvatarResponse.size>100){
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
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

export default Passenger;
