import React, { Component } from 'react';
import Avatar from '../../assets/images/images.png';
import './Driver.css';
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import CarAvatar from "../../assets/images/218567006-abstract-car-wallpapers.jpg";

class Driver extends Component {
    state = {
        src: CarAvatar
    };

    async componentDidMount() {
        const getDriverAvatarResponse = await
        fetch("http://localhost:8080/users/avatar/" + this.props.driver.modelId)
            .then(response => response.blob());

        if(getDriverAvatarResponse){
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }

    }

    render() {
        return (
                    <div className="Driver">
                        <ul style={{marginRight: 50, maxWidth: 350}}>
                            <img src={this.state.src} alt="Not found!"/>
                        </ul>
                        <ul style={{maxWidth: 350}}>
                            <h2> Name: <span className="header">{this.props.driver.firstName}</span></h2>
                        </ul>
                    </div>
        )
    }

}

export default Driver;
