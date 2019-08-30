import React from 'react';

import carpoolingLogo from '../../assets/images/download (2).png';
import './Logo.css';

const logo = (props) => (
    <div className="Logo" style={{height: props.height}}>
       <img src={carpoolingLogo} alt={'logo'}/>
    </div>
);

export default logo;