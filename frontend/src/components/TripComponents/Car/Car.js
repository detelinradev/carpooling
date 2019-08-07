import React from 'react';
import '../../../containers/Profile/Car.css';

const car = (props) => {

    return (
        <div className="Car" >
            <ul>
                <div>
                    <h2>Brand: <span className="header">{props.data.brand}<br/></span></h2>
                    <h2>Model: <span className="header">{props.data.model}</span></h2>
                    <h2>Color: <span className="header">{props.data.color}</span></h2>
                    <h2>First registration: <span className="header">{props.data.firstRegistration}</span></h2>
                </div>
            </ul>
        </div>)


};
export default car;