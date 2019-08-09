import React, { Component } from 'react';
import { Route, Switch, withRouter, Redirect,  } from 'react-router-dom';
import { connect } from 'react-redux';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

import Layout from './hoc/Layout/Layout';
import Logout from './containers/Auth/Logout/Logout';
import * as actions from './store/actions/index';
import './App.css';
import Home from './containers/Home/Home';
import TopRatedDrivers from "./containers/TopRatedUsers/TopRatedDrivers";




const asyncAuth = asyncComponent(() => {
    return import('./containers/Auth/Auth');
});
const asyncCreateTrip = asyncComponent(() => {
    return import('./containers/NewTrip/NewTrip');
});
const asyncFullTrip = asyncComponent(() => {
    return import('./containers/FullTrip/FullTrip');
});
const asyncProfile = asyncComponent(() => {
    return import('./containers/Profile/Profile');
});
const asyncSearchTrips = asyncComponent(() => {
    return import('./containers/SearchTrips/SearchTrips');
});

class App extends Component {
    componentDidMount () {
        this.props.onTryAutoSignup();
    }

    render () {
        let routes = (
            <Switch>
                <Route path="/auth" component={asyncAuth} />
                <Route path="/" exact component={Home} />
                <Redirect to="/" />
            </Switch>
        );
        let topRated;

        if ( this.props.isAuthenticated ) {
            routes = (
                <Switch>
                    <Route path="/logout" component={Logout} />
                    <Route path="/fullTrip" component={asyncFullTrip} />
                    <Route path="/createTrip" component={asyncCreateTrip} />
                    <Route path="/searchTrips" component={asyncSearchTrips} />
                    <Route path="/myProfile" component={asyncProfile} />
                    <Route path="/auth" component={asyncAuth} />
                    <Route path="/" exact component={Home} />
                    <Route path="/" component={Home} />

                    <Redirect to="/" />
                </Switch>
            );
            topRated = (
                <TopRatedDrivers/>
            )
        }

        return (
            <div>
                <Layout>
                    {routes}
                    {/*<footer style={{textAlign: 'center', marginTop: 100}} className="test-footer m-5 color=elegant-color">*/}
                    {/*    This is footer*/}
                    {/*</footer>*/}
                    {topRated}
                </Layout>
                    <footer className="footer">
                        footer
                    </footer>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        isAuthenticated: state.auth.token !== null
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onTryAutoSignup: () => dispatch( actions.authCheckState() )
    };
};

export default withRouter( connect( mapStateToProps, mapDispatchToProps )( App ) );
