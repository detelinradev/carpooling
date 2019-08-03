import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Car.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';

class Car extends Component {

    state = {
        car: {}
    };

    async componentDidMount() {
            const getCarResponse = await axios.get('/car', {
                headers:
                    {"Authorization": this.props.token}
            });

            if(getCarResponse) {
                this.setState({
                    car: getCarResponse.data
                })
            }
    }


    render() {

        const carr =
            this.props.hasCar ? (

                    <div className="Car" style={{paddingRight: 700}}>
                        <img
                            src="https://res.cloudinary.com/teepublic/image/private/s--L9GalMpn--/t_Preview/b_rgb:000000,c_limit,f_jpg,h_630,q_90,w_630/v1556900569/production/designs/3555619_1.jpg"
                            alt="car pooling"/>
                        <div>
                            <h1>Brand: <span className="header">{this.state.car.brand}<br/></span></h1>
                            <h1>Model: <span className="header">{this.state.car.model}</span></h1>
                            <h1>First registration: <span className="header">{this.state.car.firstRegistration}</span></h1>
                            <h1>A/C: <span className="header">{this.state.car.airConditioned}</span></h1>
                        </div>
                    </div>
                ) :
                (null);

        return (
            <div>
                {carr}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(Car, axios));