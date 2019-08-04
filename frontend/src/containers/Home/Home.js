import React, { Component } from 'react';
import {connect} from 'react-redux';
import './Home.css';
import { FaAndroid } from 'react-icons/fa';




class Home extends Component {


    render() {
        if (this.props.token) {
            return (
                <div className="todore">
                    <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                </div>)
        }

        return (
                <div className="todore">

                    <h1 className="header">THE PERFECT PLACE TO FIND <FaAndroid/><br/> THE FASTEST WAY TO TRAVEL</h1>
                    <hr style={{width: '70%'}}/>
                    <h2 style={{textAlign: "center"}}>
                        SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                    </h2>
                </div>)
    }
}

const mapStateToProps = state => {
    return {
      //  loading: state.trip.loading,
        token: state.auth.token
    }
};

export default connect(mapStateToProps)(Home);