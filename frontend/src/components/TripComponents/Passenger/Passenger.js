import React from 'react';
import './passenger.css';
import StarRatings from "react-star-ratings";

const passenger = (props) => {

    return (
        <div className=" Post">

            <div className="Trip additional-details  cardcont  meta-data-container">
                <h2>Passenger:</h2> <p className="meta-data">{props.data.firstName} {props.data.lastName}</p>
                <p className="image">
                    <img id="postertest" className='poster' style={{width: 128}}
                         src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/></p>

                <p className="row-xs-6 info">Rating<p className="meta-data">{
                    <StarRatings
                        rating={props.data.rating}
                        starRatedColor="blue"
                        //changeRating={this.changeRating}
                        numberOfStars={5}
                        name='rating'
                    />}</p></p>
            </div>
        </div>);

};
export default passenger;
