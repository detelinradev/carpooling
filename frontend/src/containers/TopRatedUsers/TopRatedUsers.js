import React, {Component} from 'react';
import axios from '../../axios-baseUrl';
import Driver from "./Driver";
import './TopRatedUsers.css';
import Passengers from "./Passengers";
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";

class TopRatedUsers extends Component {
    state = {
        drivers: [],
        passengers: []
    };

    componentDidMount() {
        const headers = {
            "Content-Type":"application/json",
            'Authorization':this.props.token
        };
        axios.get('http://localhost:8080/users/top-rated-drivers?',{headers})
            .then(response => {
                if (response) {
                    this.setState({
                        drivers: response.data
                    })
                }

            });
        console.log(this.state.drivers)

        axios.get('http://localhost:8080/users/top-rated-passengers?',{headers})
            .then(response => {
                if (response) {
                    this.setState({
                        passengers: response.data
                    })
                }

            });
    }



    render() {


        let drivers = this.state.drivers.map(driver => (
            <div key={driver.modelId} className="users">
                <Driver
                    key={driver.modelId}
                    driver={driver}
                />
            </div>
        ));

        let passengers = this.state.passengers.map(passenger => (
            <div key={passenger.modelId} className="users">
                <Passengers
                    key={passenger.modelId}
                    passenger={passenger}
                />
            </div>
        ));

        return (
                <div style={{marginBottom: 50}} className="users">
                    <div>
                        <h1> TOP RATED DRIVERS</h1>
                    </div>
                    <div>
                        {drivers}
                    </div>
                    <div>
                        <h1> TOP RATED PASSENGERS</h1>
                    </div>
                    <div>
                    {passengers}
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

export default connect(mapStateToProps)(withErrorHandler(TopRatedUsers, axios));