import React, {Component} from 'react';
import {Route, Switch, withRouter, Redirect,} from 'react-router-dom';
import {connect} from 'react-redux';
import asyncComponent from './hoc/asyncComponent/asyncComponent';

import Layout from './hoc/Layout/Layout';
import Logout from './containers/Auth/Logout/Logout';
import * as actions from './store/actions/index';
import './App.css';
import Home from './containers/Home/Home';
import TopRatedUsers from "./containers/TopRatedUsers/TopRatedUsers";


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
const asyncMyTrips = asyncComponent(() => {
    return import('./containers/MyTrips/MyTrips');
});
const asyncAdmin = asyncComponent(() => {
    return import('./containers/Admin/Admin');
});
const asyncFullUser = asyncComponent(() => {
    return import('./containers/FullUser/FullUser');
});

class App extends Component {
    componentDidMount() {
        this.props.onTryAutoSignup();
    }

    render() {
        let routes = (
            <Switch>
                <Route path="/auth" component={asyncAuth}/>
                <Route path="/" exact component={Home}/>
                <Redirect to="/"/>
            </Switch>
        );
        let topRated;

        if (this.props.isAuthenticated) {
            if (this.props.isAdmin) {
                routes = (
                    <Switch>
                        <Route path="/logout" component={Logout}/>
                        <Route path="/admin" component={asyncAdmin}/>
                        <Route path="/fullUser" component={asyncFullUser}/>
                        <Route path="/fullTrip" component={asyncFullTrip}/>
                        <Route path="/createTrip" component={asyncCreateTrip}/>
                        <Route path="/searchTrips" component={asyncSearchTrips}/>
                        <Route path="/myTrips" component={asyncMyTrips}/>
                        <Route path="/myProfile" component={asyncProfile}/>
                        <Route path="/auth" component={asyncAuth}/>
                        <Route path="/" exact component={Home}/>
                        <Redirect to="/"/>
                    </Switch>)
            } else

                routes = (
                    <Switch>
                        <Route path="/logout" component={Logout}/>
                        <Route path="/fullUser" component={asyncFullUser}/>
                        <Route path="/fullTrip" component={asyncFullTrip}/>
                        <Route path="/createTrip" component={asyncCreateTrip}/>
                        <Route path="/searchTrips" component={asyncSearchTrips}/>
                        <Route path="/myTrips" component={asyncMyTrips}/>
                        <Route path="/myProfile" component={asyncProfile}/>
                        <Route path="/auth" component={asyncAuth}/>
                        <Route path="/" exact component={Home}/>
                        <Redirect to="/"/>
                    </Switch>
                );
            topRated = (
                <TopRatedUsers/>
            )
        }

        return (
            <div>
                <Layout>
                    {routes}
                    {topRated}
                </Layout>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        isAuthenticated: state.auth.token !== null,
        isAdmin: state.auth.userRole === 'ADMIN'
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onTryAutoSignup: () => dispatch(actions.authCheckState())
    };
};

export default withRouter(connect(mapStateToProps, mapDispatchToProps)(App));
