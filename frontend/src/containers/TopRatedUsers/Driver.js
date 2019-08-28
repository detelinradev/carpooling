import React, {Component} from 'react';
import Avatar from '../../assets/images/image-default.png';
import './Driver.css';
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../axios-baseUrl";

class Driver extends Component {
    state = {
        src: Avatar,
    };

    async componentDidMount() {

        const headers = {
            "Content-Type":"application/json",
            'Authorization':this.props.token
        };

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.driver.username,{headers})
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

const mapStateToProps = state => {
    return {
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(Driver, axios));
