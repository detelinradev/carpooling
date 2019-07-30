import React from 'react';

//import carpoolingLogo from '../../assets/images/carpooling-logo.png';
import classes from './Logo.css';

const logo = (props) => (
    <div className={classes.Logo} style={{height: props.height}}>
       {/*<img src={carPoolingLogo} alt="CarPoolingApp" />*/}
    </div>
);

export default logo;