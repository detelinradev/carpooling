import React, {Component} from 'react';
import axios from '../../axios-baseUrl';
import Driver from "./Driver";

class topRatedDrivers extends Component {
    state = {
        users: []
    };

    componentDidMount() {
        axios.get('http://localhost:8080/users/top-rated-drivers')
            .then(response => {
                this.setState({
                    users: response.data
                });
            });
    }

    render() {



        let drivers = this.state.users.map(driver => (
            <Driver
                key={driver.modelId}
                driver={driver}
            />
        ));

        return (
            <div >
                <ul>
                    <div style={{color: "white"}}>
                        TOP 10 DRIVERS
                        {drivers}
                    </div>
                </ul>
            </div>)

    }
}
export default topRatedDrivers;