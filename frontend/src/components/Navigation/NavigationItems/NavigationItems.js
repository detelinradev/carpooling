import React from 'react';

import classes from './NavigationItems.css';
import NavigationItem from './NavigationItem/NavigationItem';

const navigationItems = ( props ) => (
    <ul className={classes.NavigationItems}>
        <NavigationItem link="/" exact>Home</NavigationItem>
        {props.isAuthenticated ? <NavigationItem link="/myTrips">My Trips</NavigationItem> : null}
        {props.isAuthenticated ? <NavigationItem link="/myProfile">Profile</NavigationItem> : null}
        {props.isAuthenticated ? <NavigationItem link="/createTrip">Create Trip</NavigationItem> : null}
        {!props.isAuthenticated
            ? <NavigationItem link="/auth">Login</NavigationItem>
            : <NavigationItem link="/logout">Logout</NavigationItem>}
    </ul>
);

export default navigationItems;