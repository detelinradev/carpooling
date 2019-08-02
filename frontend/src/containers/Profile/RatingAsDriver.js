import React from 'react';
import axios from "../../axios-baseUrl";
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";


const RatingAsDriver = (props) => {

    axios.get('/api/ratings/search/findAverageRatingByUserAsDriver', {
        headers:
            {"Authorization" : props.token},
        params:
            {userID: props.id}
    })
        .then(response => {
            console.log(response);
            this.setState({
                ratingAsDriver: response.data
            });
        })
        .catch(error => {
            console.log(error);
        });

    return (
        <h2>Rating as driver: <span className="header">{props.ratingAsDriver}</span></h2>
    );
};

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(RatingAsDriver, axios));