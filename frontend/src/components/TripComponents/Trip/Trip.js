import React from 'react';
import './Trip.css';
import Comment from "../Comment/Comment";

const trip = (props) => {


    let comments = props.comments.map(comment => (
        <Comment
            key={comment.id}
            message={comment.message}
            author={comment.author}
        />
    ));


    return (
        <div className=" Post">
            <div className="proba Trip additional-details hed">{props.data.origin} -> {props.data.destination}</div>
            <div className="Trip additional-details  cardcont  meta-data-container">
                <p className="image">
                    <img id="postertest" className='poster' style={{width: 128}}
                         src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>
                    <p className="meta-data">{props.driver.firstName} {props.driver.lastName}</p></p>
                <p className="row-xs-6 info"> Departure Time<p className="meta-data">{props.data.departureTime}</p></p>
                <p className="row-xs-6 info">Available Seats<p className="meta-data">{props.data.availablePlaces}</p>
                </p>
                <p className="row-xs-6 info">Price<p className="meta-data">{props.data.costPerPassenger} leva</p></p>
                <p className="row-xs-6 info">Car<p className="meta-data">{props.car.brand} {props.car.model}</p></p>
                <p className="row-xs-6 info">Status<p className="meta-data">{props.data.tripStatus}</p></p>
                <p className="row-xs-3 info">Message<p className="meta-data">{props.data.message}</p></p>
                <p className="row-xs-6 info">Smoking allowed<p className="meta-data">{props.car.smokingAllowed}</p></p>
                <p className="row-xs-6 info">Pets allowed<p className="meta-data">{props.car.petsAllowed}</p></p>
                <p className="row-xs-6 info">Luggage allowed<p className="meta-data">{props.car.luggageAllowed}</p></p>
                <p className="row-xs-6 info">Air-conditioned<p className="meta-data">{props.car.airConditioned}</p></p>

            </div>
            {comments}
        </div>);

};
export default trip;
