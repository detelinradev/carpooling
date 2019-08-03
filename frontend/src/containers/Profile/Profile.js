import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Car from "./Car";


class Profile extends Component {
    state = {
        user: {},
        hasCar: false,
        isToggleOn: false,
    };


    async componentDidMount() {
        const getMeResponse = await
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            });
        this.setState({
            user: getMeResponse.data,
        });


    }



    toggleCarHandler(){
        this.setState({
            isToggleOn: !this.state.isToggleOn
        });
    }


    render() {
        return (
            <div>
                <div className="Profile">
                    <img src="https://www.w3schools.com/howto/img_avatar.png" alt="car pooling"/>
                    <ul>
                        <li><h1>Name: <span
                            className="header">{this.state.user.firstName} {this.state.user.lastName}</span></h1></li>
                        <li><h1>Username: <span className="header">{this.state.user.username}</span></h1></li>
                        <li><h2>Email: <span className="header">{this.state.user.email}</span></h2></li>
                        <li><h2>Phone: <span className="header">{this.state.user.phone}</span></h2></li>
                    </ul>
                    <hr/>
                    <ul style={{paddingRight: 140}}>
                        <li>
                            <h2>
                                Rating as driver: <span className="header">{this.state.user.ratingAsDriver}</span>
                            </h2>
                        </li>
                        <li>
                            <h2>
                                Rating as passenger: <span className="header">{this.state.user.ratingAsPassenger}</span>
                            </h2>
                        </li>
                    </ul>
                </div>
                <button className="Profile" onClick={() => this.toggleCarHandler()}>
                    {this.state.isToggleOn ? <Car hasCar={this.state.user.hasCar} /> : <div>Toggle car</div>}</button>


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