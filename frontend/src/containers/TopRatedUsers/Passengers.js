import React, { Component } from 'react';
import Avatar from '../../assets/images/image-default.png';
import './Passengers.css';

class Passengers extends Component {
    state = {
        src: Avatar
    };

    async componentDidMount() {
        const getPassengerAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.passenger.modelId)
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

export default Passengers;
