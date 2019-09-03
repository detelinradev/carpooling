import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Car.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import CarAvatar from "../../assets/images/cars-noimage_sedan-lrg.png";
import * as actions from "../../store/actions";

class Car extends Component {
    state = {
        src: CarAvatar,
        error: '',
        msg: '',
        file: '',
    };


    async componentDidMount() {


        const getCarAvatarResponse = await
            fetch('http://localhost:8080/users/avatar/car/'+ this.props.username,
                {headers: {"Authorization": this.props.token}})
                .then(response => response.blob());

        if (getCarAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getCarAvatarResponse)
            })
        }
    }


    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    };

    uploadFile = (event) => {
        event.preventDefault();

        if (!this.state.file) {
            this.props.showMessage('Please upload a file');
            return;
        }

        if (this.state.file.size >= 1000000) {
            this.props.showMessage('File size exceeds limit of 1MB');
            return;
        }

        let data = new FormData();
        data.append('upfile', this.state.file);

        fetch('http://localhost:8080/users/avatar/car', {
            method: 'POST',
            headers: {"Authorization": this.props.token},
            body: data
        }).then(response => {
            if(!response.response)
            this.props.showMessage('Successfully uploaded file');
            this.componentDidMount();
        }).catch(err => {
            this.props.showMessage('Request was not completed');
        });
    };

    render() {

        return (
            <div className="Car">
                <ul>
                    <img style={{maxWidth: 350}}
                         src={this.state.src}
                         alt="car pooling"/>
                    <div>
                        <input onChange={this.onFileChange} type="file"/>
                        <br/>
                        <button onClick={this.uploadFile}>Upload</button>
                    </div>
                </ul>
                <ul>
                    <div>
                        <h2>Brand: <span className="header">{this.props.car.brand}<br/></span></h2>
                        <h2>Model: <span className="header">{this.props.car.model}</span></h2>
                        <h2>Color: <span className="header">{this.props.car.color}</span></h2>
                        <h2>First registration: <span className="header">{this.props.car.firstRegistration}</span>
                        </h2>
                        <h2>A/C: <span className="header">{this.props.car.airConditioned}</span></h2>
                    </div>
                </ul>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        carCreated: state.car.carCreated,
        username:state.auth.userId

    }
};


const mapDispatchToProps = dispatch => {
    return {
        onCreateCar: (create, token) => dispatch(actions.createCar(create, token))
    };
};
export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Car, axios));