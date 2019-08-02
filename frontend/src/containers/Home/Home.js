import React, { Component } from 'react';
import {connect} from 'react-redux';
import './Home.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';


class Home extends Component {


    render () {
        // let homeContent = () =>
        //     (
        //     <div className="todore">
        //         <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
        //         <hr style={{width: '70%'}}/>
        //         <h2 style={{textAlign: "center"}}>
        //             SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
        //         </h2>
        //     </div>
        //     );
        // if(this.props.token){
        //     homeContent = () =>(
        //         <div className="todore">
        //             <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
        //         </div>
        //     )
        //
        // }


        return (
            <div className="todore">
                <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                <hr style={{width: '70%'}}/>
                <h2 style={{textAlign: "center"}}>
                    SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                </h2>
            </div>
          //  {homeContent}

        );
    }
}

const mapStateToProps = state => {
    return {
      //  loading: state.trip.loading,
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(Home);