import React, { Component } from 'react';
import './Home.css';


class Home extends Component {


    render () {

        return (
            <div className="todore">
                <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                <hr style={{width: '70%'}}/>
                <h2 style={{textAlign: "center"}}>
                    SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                </h2>
            </div>
        );
    }
}

export default Home;