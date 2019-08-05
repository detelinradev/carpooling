// import React, { Component } from 'react';
import React from 'react';
import './Trip.css';

const trip = (props) => {
// class Trip extends Component {
    // state = {
    //     trip: {
    //         id:"",
    //         driver: [],
    //         message: "",
    //         departureTime: "",
    //         origin: "",
    //         destination:"",
    //         availablePlaces:"",
    //         passengers: [],
    //         tripStatus: "",
    //         comments:[],
    //         car:{
    //             smokingAllowed: "",
    //             petsAllowed: "",
    //             luggageAllowed: "",
    //             airConditioned: ""
    //         }
    //
    //     },
    // };

    // render() {

    return (
        <div className="Trip">
            <div>
                <h6>Driver: {props.driver.username}<br/></h6>
                <h6>Origin: {props.origin}</h6>
                <h6>Destination: {props.destination}</h6>
                <h6>Departure time: {props.departureTime}</h6>
                <h6>Available places: {props.availablePlaces}</h6>
                <h6>Trip status: {props.tripStatus}</h6>
                <h6>Message: {props.message}</h6>
                <h6>Smoking: {props.car.smokingAllowed}</h6>
                {/*<h6>Pets: {props.car.petsAllowed}</h6>*/}
                {/*<h6>Luggage: {props.car.luggageAllowed}</h6>*/}
                {/*<h6>Air-condition: {props.car.airConditioned}</h6>*/}

                {/*<h6>Driver: {this.state.trip.driver.username}<br/></h6>*/}
                {/*<h6>Origin: {props.origin}</h6>*/}
                {/*<h6>Destination: {this.state.trip.destination}</h6>*/}
                {/*<h6>Departure time: {this.state.trip.departureTime}</h6>*/}
                {/*<h6>Available places: {this.state.trip.availablePlaces}</h6>*/}
                {/*<h6>Trip status: {this.state.trip.tripStatus}</h6>*/}
                {/*<h6>Message: {this.state.trip.message}</h6>*/}
                {/*<h6>Smoking: {this.state.trip.car.smokingAllowed}</h6>*/}
                {/*<h6>Pets: {this.state.trip.car.petsAllowed}</h6>*/}
                {/*<h6>Luggage: {this.state.trip.car.luggageAllowed}</h6>*/}
                {/*<h6>Air-condition: {this.state.trip.car.airConditioned}</h6>*/}

            </div>
        </div>
    )
};

export default trip;

// export default Trip;

{/*    <div className=" Post">*/
}
{/*    <div  className="proba Trip additional-details hed">{this.props.origin} -> {this.props.destination}</div>*/
}

{/*    <div className="Trip additional-details  cardcont  meta-data-container">*/
}
{/*        <p className="image">*/
}
{/*            <img id="postertest" className='poster' style={{width: 128}}*/
}
{/*                 src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>*/
}

{/*            <p className="meta-data">{this.props.creator}</p>*/
}
{/*        </p>*/
}

{/*        <p className="row-xs-6 info">Date<p className="meta-data">27.03</p></p>*/
}
{/*        <p className="row-xs-6 info"> Departure Time<p className="meta-data">{this.props.departureTime}</p></p>*/
}
{/*        <p className="row-xs-6 info">Available Seats<p className="meta-data">{this.props.seats}</p></p>*/
}
{/*        <p className="row-xs-6 info">Price<p className="meta-data">{this.props.price} leva</p></p>*/
}
{/*        <p className="row-xs-6 info">Car<p className="meta-data" >{this.state.car.brand} {this.state.car.model}</p></p>*/
}
{/*    </div>*/
}
{/*</div>)*/
}
