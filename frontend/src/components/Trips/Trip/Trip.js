import React from 'react';
import './Trip.css';

const trip = ( props ) => {
    const tripData = [];

    for (let field in props.tripData) {
        tripData.push(
            {
                id: tripField,
                value: props.tripData[tripField]
            }
        );
    }
    return(
        <div className="Trip">
            {tripData}
        </div>
    )
};


export default trip;

{/*    <div className=" Post">*/}
{/*    <div  className="proba Trip additional-details hed">{this.props.origin} -> {this.props.destination}</div>*/}

{/*    <div className="Trip additional-details  cardcont  meta-data-container">*/}
{/*        <p className="image">*/}
{/*            <img id="postertest" className='poster' style={{width: 128}}*/}
{/*                 src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>*/}

{/*            <p className="meta-data">{this.props.creator}</p>*/}
{/*        </p>*/}

{/*        <p className="row-xs-6 info">Date<p className="meta-data">27.03</p></p>*/}
{/*        <p className="row-xs-6 info"> Departure Time<p className="meta-data">{this.props.departureTime}</p></p>*/}
{/*        <p className="row-xs-6 info">Available Seats<p className="meta-data">{this.props.seats}</p></p>*/}
{/*        <p className="row-xs-6 info">Price<p className="meta-data">{this.props.price} leva</p></p>*/}
{/*        <p className="row-xs-6 info">Car<p className="meta-data" >{this.state.car.brand} {this.state.car.model}</p></p>*/}
{/*    </div>*/}
{/*</div>)*/}
