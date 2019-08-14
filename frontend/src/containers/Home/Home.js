import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Home.css';
import axios from '../../axios-baseUrl';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";


class Home extends Component {


    render() {
        if (this.props.token) {
            return (
                <Auxiliary>
                    <h1 className="header">
                        WELCOME
                    </h1>
                <div className="hometext">
                    <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                    <hr style={{width: '70%'}}/>
                    <h2 style={{textAlign: "center"}}>
                        SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                    </h2>
                    <h3>
                        This app can help you find the most suitable type of transport for you at the right time
                        <br/>
                        You can create and search a trip as easy as clicking several buttons
                    </h3>
                </div>
                </Auxiliary>)
        }

        return (
            <div className="hometext">

                <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                <hr style={{width: '70%'}}/>
                <h2 style={{textAlign: "center"}}>
                    SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                </h2>
            </div>)
    }
}

export default connect()(withErrorHandler(Home, axios));