import React, { Component } from 'react';
import { Route, Switch, withRouter, Redirect,  } from 'react-router-dom';
import { connect } from 'react-redux';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

import Layout from './hoc/Layout/Layout';
import Logout from './containers/Auth/Logout/Logout';
import * as actions from './store/actions/index';
import './App.css';
import Home from './containers/Home/Home';
import Profile from './containers/Profile/Profile';

import Trips from './components/Trips/Trips'
import NewTrip from "./containers/NewTrip/NewTrip";


const asyncAuth = asyncComponent(() => {
    return import('./containers/Auth/Auth');
});
const asyncCreateTrip = asyncComponent(() => {
    return import('./containers/NewTrip/NewTrip');
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

        if ( this.props.isAuthenticated ) {
            routes = (
                <Switch>
                    <Route path="/logout" component={Logout} />
                    <Route path="/createTrip" component={asyncCreateTrip} />
                    <Route path="/myProfile" component={Profile} />
                    <Route path="/auth" component={asyncAuth} />
                    <Route path="/" exact component={Home} />

                    <Redirect to="/" />
                </Switch>
            );
        }

        return (
            <div>
                <Layout>
                    {/*<Login/>*/}
                    {routes}
                </Layout>
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
