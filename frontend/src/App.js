import React, { Component } from 'react';
import { Route, Switch, withRouter, Redirect } from 'react-router-dom';
import { connect } from 'react-redux';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

import Layout from './hoc/Layout/Layout';
import Logout from './containers/Auth/Logout/Logout';
import * as actions from './store/actions/index';

import Trip from './Trips/Trips'
import NewTrip from "./containers/NewTrip/NewTrip";


const asyncAuth = asyncComponent(() => {
    return import('./containers/Auth/Auth');
});

class App extends Component {
    componentDidMount () {
        this.props.onTryAutoSignup();
    }

    render () {
        let routes = (
            <Switch>
                <Route path="/auth" component={asyncAuth} />
                {/*<Route path="/" exact component={Trip} />*/}
                <Redirect to="/auth" />
            </Switch>
        );

        if ( this.props.isAuthenticated ) {
            routes = (
                <Switch>
                    <Route path="/logout" component={Logout} />
                    <Route path="/createTrip" component={NewTrip} />
                    <Route path="/" exact component={Trip} />
                    <Redirect to="/" />
                </Switch>
            );
        }

        return (
            <div>
                <Layout>
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
