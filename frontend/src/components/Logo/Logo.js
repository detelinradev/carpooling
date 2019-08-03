import React from 'react';

//import carpoolingLogo from '../../assets/images/carpooling-logo.png';
import './Logo.css';

const logo = (props) => (
    <div className="Logo" style={{height: props.height}}>
       <img src="https://mondrian.mashable.com/2018%252F09%252F13%252Ff8%252Fb8e27b3b340b4147b86c0d1c2cd7d1b5.67978.jpg%252F1200x630.jpg?signature=WzXjA-ZbPEBrTtMJ8waIt132wkk=" alt="CarPoolingApp" />
    </div>
);

export default logo;