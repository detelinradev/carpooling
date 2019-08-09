import React, {Component} from 'react';
import axios from '../../axios-baseUrl';
import Driver from "./Driver";
import {fetchImageCarFail} from "../../store/actions/user";
import Auxiliary from '../../hoc/Auxiliary/Auxiliary';
import './TopRatedDrivers.css';
import Passengers from "./Passengers";

class topRatedDrivers extends Component {
    state = {
        drivers: [],
        passengers: []
    };

    componentDidMount() {
        axios.get('http://localhost:8080/users/top-rated-drivers')
            .then(response => {
                if (response) {
                    this.setState({
                        drivers: response.data
                    })
                }

            })
            .catch(err => {
                console.log(3);
            });

        axios.get('http://localhost:8080/users/top-rated-passengers')
            .then(response => {
                if (response) {
                    this.setState({
                        passengers: response.data
                    })
                }

            })
            .catch(err => {
                console.log(3);
            })

    }

    render() {


        let drivers = this.state.drivers.map(driver => (
            <div className="users">
                <Driver
                    key={driver.modelId}
                    driver={driver}
                />
            </div>
        ));

        let passengers = this.state.passengers.map(passenger => (
            <div className="users">
                <Passengers
                    key={passenger.modelId}
                    passenger={passenger}
                />
            </div>
        ));

        return (
            <Auxiliary>
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
            </Auxiliary>
        )

    }
}

export default topRatedDrivers;