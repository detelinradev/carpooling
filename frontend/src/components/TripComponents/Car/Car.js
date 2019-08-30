import React, {Component} from 'react';
import '../../../containers/Profile/Car.css';
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import CarAvatar from "../../../assets/images/cars-noimage_sedan-lrg.png";

class Car extends Component {
    state = {
        src: CarAvatar
    };


    async componentDidMount() {
        const getCarAvatarResponse = await
        fetch('http://localhost:8080/users/avatar/car/' + this.props.trip.driver.username,
            {headers: {"Authorization": this.props.token}})
            .then(response => response.blob());

        if(getCarAvatarResponse.size>100){
            this.setState({
                src: URL.createObjectURL(getCarAvatarResponse)
            })
        }
    }

    render() {


        return (
            <div style={{width: 800, float: 'left', marginRight: 35}} className="Car" >
                <ul>
                    <img style={{maxWidth: 350}}
                         src={this.state.src}
                         alt="car pooling"/>
                </ul>
                <ul>
                    <div>
                        <h2>Brand: <span className="header">{this.props.data.brand}<br/></span></h2>
                        <h2>Model: <span className="header">{this.props.data.model}</span></h2>
                        <h2>Color: <span className="header">{this.props.data.color}</span></h2>
                        <h2>First registration: <span className="header">{this.props.data.firstRegistration}</span></h2>
                    </div>
                </ul>
            </div>)



    };
}
    const mapStateToProps = state => {
    return {
        trip:state.trip.trip,
        token: state.auth.token,
    }
};

    export default connect(mapStateToProps)(withErrorHandler(Car, axios));