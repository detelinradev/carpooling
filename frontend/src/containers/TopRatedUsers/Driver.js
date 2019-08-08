import React from 'react';
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";

const driver = (props) => {

    return (

        <Auxiliary>
            <div style={{color: "white"}}>
                {props.driver.firstName} {props.driver.ratingAsDriver}
                {/*<div className="proba Trip additional-details hed">{props.data.origin} -> {props.data.destination}</div>*/}
                {/*<div className="Trip additional-details  cardcont  meta-data-container">*/}
                {/*    <p className="image">*/}
                {/*        <img id="postertest" className='poster' style={{width: 128}}*/}
                {/*             src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>*/}
                {/*        <p className="meta-data">{props.data.driver.firstName} {props.data.driver.lastName}</p></p>*/}
                {/*    <div className="edit">*/}
                {/*        <Button onClick={() => props.showFullTrip(props.data)}><h3*/}
                {/*            className="header">DETAILS <FaUserEdit/></h3>*/}
                {/*        </Button>*/}
                {/*    </div>*/}
                {/*    <p className="row-xs-6 info"> Departure Time<p className="meta-data">{props.data.departureTime}</p>*/}
                {/*    </p>*/}
                {/*    <p className="row-xs-6 info">Available Seats<p*/}
                {/*        className="meta-data">{props.data.availablePlaces}</p>*/}
                {/*    </p>*/}
                {/*    <p className="row-xs-6 info">Price<p className="meta-data">{props.data.costPerPassenger} leva</p>*/}
                {/*    </p>*/}
                {/*    <p className="row-xs-6 info">Car<p className="meta-data">{props.data.car.brand} {props.data.car.model}</p></p>*/}
                {/*    <p className="row-xs-6 info">Status<p className="meta-data">{props.data.tripStatus}</p></p>*/}
                {/*    <p className="row-xs-3 info">Message<p className="meta-data">{props.data.message}</p></p>*/}
                {/*    <p className="row-xs-6 info">Smoking allowed<p className="meta-data">{props.car.smokingAllowed}</p>*/}
                {/*    </p>*/}
                {/*    <p className="row-xs-6 info">Pets allowed<p className="meta-data">{props.car.petsAllowed}</p></p>*/}
                {/*    <p className="row-xs-6 info">Luggage allowed<p className="meta-data">{props.car.luggageAllowed}</p>*/}
                {/*    </p>*/}
                {/*    <p className="row-xs-6 info">Air-conditioned<p className="meta-data">{props.car.airConditioned}</p>*/}
                {/*    </p>*/}


            </div>
        </Auxiliary>
);

};
export default driver;
