import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Car.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Modal from "../../components/UI/Modal/Modal";
import CarAvatar from "../../assets/images/cars-noimage_sedan-lrg.png";
import NewCar from "./NewCar";
import * as actions from "../../store/actions";

class Car extends Component {

    state = {
        car: {},
        showModal: false,
        src: CarAvatar,
        error:'',
        msg:'',
        file:'',
    };

    async componentDidMount() {
            const getCarResponse = await axios.get('/carMe', {
                headers:
                    {"Authorization": this.props.token}
            });

        const getCarAvatarResponse = await
            fetch('http://localhost:8080/users/avatarMe/car',
                { headers: {"Authorization": this.props.token}})
                .then(response => response.blob());

            if(getCarAvatarResponse.size>100){
                this.setState({
                    src: URL.createObjectURL(getCarAvatarResponse)
                })
            }

        if(getCarResponse) {
            this.setState({
                car: getCarResponse.data
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
        this.setState({error: '', msg: ''});
        if(!this.state.file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }
        if(this.state.file.size >= 2000000) {
            this.setState({error: 'File size exceeds limit of 2MB.'})
            return;
        }
        let data = new FormData();
        data.append('upfile', this.state.file);
        console.log(data.getAll("upfile"));
        fetch('http://localhost:8080/users/avatar/car', {
            method: 'POST',
            headers: {"Authorization": this.props.token},
            body: data
        }).then(response => {
            this.setState({error: '', msg: 'Successfully uploaded file'});
            this.componentDidMount();
        }).catch(err => {
            this.setState({error: err});
        });
    };

    toggleModal(){
        this.setState({
            showModal: !this.state.showModal
        })
    }


    editCloseHandler() {
        this.setState({
            showModal: !this.state.showModal
        });
    }


    render() {

        const car =
            this.state.car ? (
                    <div className="Car" >
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
                            <h2>Brand: <span className="header">{this.state.car.brand}<br/></span></h2>
                            <h2>Model: <span className="header">{this.state.car.model}</span></h2>
                            <h2>Color: <span className="header">{this.state.car.color}</span></h2>
                            <h2>First registration: <span className="header">{this.state.car.firstRegistration}</span></h2>
                            <h2>A/C: <span className="header">{this.state.car.airConditioned}</span></h2>
                        </div>
                        </ul>
                    </div>
                ) :
                (
                    <button className="Car" onClick={() => this.toggleModal()}><h1>+CREATE CAR</h1></button>
                );

        return (
            <div>
                <Modal style={{width: 600}} show={this.state.showModal} modalClosed={() => this.editCloseHandler()}>
                    <NewCar showModal={this.state.showModal}/>
                </Modal>
                {car}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        carCreated:state.car.carCreated,

    }
};


const mapDispatchToProps = dispatch => {
    return {
        onCreateCar: (create, token) => dispatch(actions.createCar(create, token))    };
};
export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Car, axios));