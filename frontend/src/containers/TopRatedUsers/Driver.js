import React, {Component} from 'react';
import Avatar from '../../assets/images/image-default.png';
import './Driver.css';
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import CarAvatar from "../../assets/images/218567006-abstract-car-wallpapers.jpg";
import StarRatings from "react-star-ratings";

class Driver extends Component {
    state = {
        src: Avatar
    };

    async componentDidMount() {
        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.driver.modelId)
                .then(response => response.blob());

        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }

    }

    render() {
        return (
            <div className="Driver">
                <img  src={this.state.src} alt="Not found!"/>
                <span className="header"> {this.props.driver.username}</span>

                <div className="divech">
                    Driver rating  <span className="header-two"> {this.props.driver.ratingAsDriver}</span>
                </div>
            </div>
        )
    }

}

export default Driver;
