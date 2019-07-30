import React, { Component } from 'react';
import axios from 'axios';
import Trip from "./Trip/Trip";
import './Trips.css';

class Trips extends Component {
    constructor(props) {
        super(props);
        this.state = {
            trips: []
        };
    }

    componentDidMount() {
        axios.get('/trips')
            .then(response => {
                this.setState({
                    trips: response.data._embedded.trips,
                });
            })
            .catch(error => {
                console.log(error);
            });
    }

    render() {
        let trips = <p style={{textAlign: 'center'}}>Something went wrong!</p>;
        if (!this.state.error) {
            trips = this.state.trips.map(trip => {
                return (
                    // <Link to={'/trips/' + trip.id} key={trip.id}>
                    <Trip
                        key={trip.created}
                        creator={trip.creator}
                        destination={trip.destination}
                        origin={trip.origin}
                        departureTime={trip.departureTime}
                        price={trip.costPerPassenger}
                        seats={trip.availablePlaces}
                    />
                    // </Link>
                );
            });

            return (
                <div >
                    <h1 className="header">TRIPS</h1>
                    <section className="Trips">
                        {trips}
                    </section>
                </div>
            );
        }
    }
}

export default Trips;