import React from 'react';

//import carpoolingLogo from '../../assets/images/carpooling-logo.png';
import './Logo.css';

const logo = (props) => (
    <div className="Logo" style={{height: props.height}}>
       <img src="http://drinkndrive.me/wp-content/uploads/2018/07/logo-1-300x138.png" alt="CarPoolingApp" />
    </div>
);

export default logo;