import React, {Component} from 'react';
import '../../../containers/Profile/Car.css';
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import CarAvatar from "../../../assets/images/218567006-abstract-car-wallpapers.jpg";

class Car extends Component {


    componentDidMount() {
        this.props.onFetchCarImage(this.props.token, this.props.trip.driver.modelId);
        console.log(this.props.trip.driver.modelId)
    }

    render() {
        let image = CarAvatar;
        if (this.props.carImage) {
            console.log(this.props.carImage)
            image = this.props.carImage;
        }

        return (
            <div className="Car">
                <ul>
                    <img style={{maxWidth: 350}}
                         src={image}
                         alt="car pooling"/>
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
        carImage:state.user.carImage
    }
};
    const mapDispatchToProps = dispatch => {
    return {
        onFetchCarImage: (token, userId) => dispatch(actions.fetchImageCar(token, userId)),
    };
};

    export default connect(mapStateToProps,mapDispatchToProps)(withErrorHandler(Car, axios));