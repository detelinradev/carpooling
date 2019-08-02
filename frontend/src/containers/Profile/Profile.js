import React, {Component} from 'react';
//import axios from 'axios';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';

class Profile extends Component {

    state = {
        id: 0,
        username: "",
        firstName: "",
        lastName: "",
        role: "",
        email: "",
        phone: 0,
        avatarUri: "",
        ratingAsDriver: 0,
        ratingAsPassenger: 0,
        brand: "",
        model: "",
        color: "",
        firstRegistration: 0,
        airConditioned: false,
        smokingAllowed: false,
        luggageAllowed: false,
        petsAllowed: false
    };

    async componentDidMount() {

        const [getMeResponse, getCarResponse] = await Promise.all([
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            }),
        axios.get('/car', {
            headers:
                {"Authorization": this.props.token}
            })]);

        const getRatingAsDriverResponse = await axios.get('/api/ratings/search/findAverageRatingByUserAsDriver', {
            headers:
                {"Authorization": this.props.token},
            params:
                {userID: getMeResponse.data.id}
        });

        const getRatingAsPassengerResponse = await axios.get('/api/ratings/search/findAverageRatingByUserAsPassenger', {
            headers:
                {"Authorization": this.props.token},
            params:
                {userID: getMeResponse.data.id}
        });

        this.setState({
            id: getMeResponse.data.id,
            username: getMeResponse.data.username,
            firstName: getMeResponse.data.firstName,
            lastName: getMeResponse.data.lastName,
            role: getMeResponse.data.role,
            email: getMeResponse.data.email,
            phone: getMeResponse.data.phone,
            avatarUri: getMeResponse.data.avatarUri,

            ratingAsDriver: getRatingAsDriverResponse.data,

            ratingAsPassenger: getRatingAsPassengerResponse.data,

            brand: getCarResponse.data.brand,
            model: getCarResponse.data.model,
            firstRegistration: getCarResponse.data.firstRegistration,
            airConditioned: getCarResponse.data.airConditioned,
            smokingAllowed: getCarResponse.data.smokingAllowed,
            luggageAllowed: getCarResponse.data.luggageAllowed,
            petsAllowed: getCarResponse.data.petsAllowed
        });

    }

    render() {

        return (
            <div>
            <div  className="Profile">
                <img src="https://www.w3schools.com/howto/img_avatar.png" alt="car pooling"/>
                <ul>
                    <li><h1>Name: <span className="header">{this.state.firstName} {this.state.lastName}</span></h1></li>
                    <li><h1>Username: <span className="header">{this.state.username}</span></h1></li>
                </ul>
                <hr/>
                <ul style={{paddingRight: 140}}>
                    <li><h2>Role: <span className="header">{this.state.role}</span></h2></li>
                    <li><h2>Rating as driver: <span className="header">{this.state.ratingAsDriver}</span></h2></li>
                    <li><h2>Rating as passenger: <span className="header">{this.state.ratingAsPassenger}</span></h2>
                    </li>
                </ul>
            </div>
                <div  className="Profile" style={{paddingRight: 700}}>
                    <img src="https://res.cloudinary.com/teepublic/image/private/s--L9GalMpn--/t_Preview/b_rgb:000000,c_limit,f_jpg,h_630,q_90,w_630/v1556900569/production/designs/3555619_1.jpg" alt="car pooling"/>
                    <div>
                        <h1>Brand: <span className="header">{this.state.brand}<br/></span></h1>
                        <h1>Model: <span className="header">{this.state.model}</span></h1>
                        <h1>First registration: <span className="header">{this.state.firstRegistration}</span></h1>
                        <h1>A/C: <span className="header">{this.state.airConditioned}</span></h1>
                    </div>
                </div>

            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(Profile, axios));