import React, {Component} from 'react';
import '../../../containers/Profile/Car.css';
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import CarAvatar from "../../../assets/images/cars-noimage_sedan-lrg.png";

class Car extends Component {
    state = {
        src: CarAvatar,
        data:[]
    };


    async componentDidMount() {
        const getCarAvatarResponse = await
        fetch('http://localhost:8080/images/car/' + this.props.driverName,
            {headers: {"Authorization": this.props.token}})
            .then(response => response.blob());

        if(getCarAvatarResponse.size>500){
            this.setState({
                src: URL.createObjectURL(getCarAvatarResponse)
            })
        }

        const carData = await
            axios.get('/car/' + this.props.driverName,
                {headers: {"Authorization": this.props.token}});

        if(carData){
            this.setState({
                data: carData.data
            })
        }

    }

    render() {
        console.log(this.state.data)

        return (
            <div style={{width: 800, float: 'left', marginRight: 35}} className="Car" >
                <ul>
                    <img style={{maxWidth: 350}}
                         src={this.state.src}
                         alt="car pooling"/>
                </ul>
                <ul>
                    <div>
                        <h2>Brand: <span className="header">{this.state.data.brand}<br/></span></h2>
                        <h2>Model: <span className="header">{this.state.data.model}</span></h2>
                        <h2>Color: <span className="header">{this.state.data.color}</span></h2>
                        <h2>First registration: <span className="header">{this.state.data.firstRegistration}</span></h2>
                    </div>
                </ul>
            </div>)



    };
}
    const mapStateToProps = state => {
    return {
        // trip:state.trip.trip,
        token: state.auth.token,
    }
};

    export default connect(mapStateToProps)(withErrorHandler(Car, axios));