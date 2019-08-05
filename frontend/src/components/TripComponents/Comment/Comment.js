import React from 'react';
import './Comment.css';

const comment = (props) => {

    return (
        <div className=" Post">
            <div className="Trip additional-details  cardcont  meta-data-container">
                <p className="image">
                    <img id="postertest" className='poster' style={{width: 128}}
                         src={'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg'} alt={''}/>
                    <p className="meta-data">{props.author.firstName} {props.author.lastName}</p></p>
                <p className="row-xs-6 info">Message<p className="meta-data">{props.message}</p></p>
            </div>
        </div>);

};
export default comment;
