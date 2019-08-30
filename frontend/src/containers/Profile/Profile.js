import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Car from "./Car";
import Button from "@material-ui/core/Button";
import Modal from "../../components/UI/Modal/Modal";
import Avatar from '../../assets/images/image-default.png';
import {TiUser, TiGroup} from "react-icons/ti";
import {FaEnvelopeOpen, FaPhone, FaMedal, FaUserEdit} from "react-icons/fa";
import StarRatings from 'react-star-ratings';
import * as actions from "../../store/actions";
import NewCar from "./NewCar";


class Profile extends Component {
    state = {
        user: {},
        car: {},
        isToggleOn: false,
        edit: false,
        editPass: false,
        newEmail: "",
        newPassword: "",
        src: Avatar,
        error: '',
        msg: '',
        file: '',
        message: null,
        showModal: false
    };


    async componentDidMount() {

        const getAvatarResponse = await
            fetch('http://localhost:8080/users/avatarMe',
                {headers: {"Authorization": this.props.token}})
                .then(response => response.blob());
        if (getAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getAvatarResponse)
            })
        }

        const getMeResponse = await
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            });

        if (getMeResponse) {
            this.setState({
                user: getMeResponse.data,
                newEmail: getMeResponse.data.email,
            })
        }
        this.getCar()


    };

    async componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.carCreated === 'yes') {
            this.setState({message: 'Car successfully created'});
            this.getCar();
            this.props.onCarFinishCreate();
            this.toggleModal();
        }
        if (this.props.carCreated === 'error') {
            this.props.onCarFinishCreate();
            this.toggleModal();
        }
    }

    async getCar() {
        const getCarResponse = await axios.get('/carMe', {
            headers:
                {"Authorization": this.props.token}
        });

        if (getCarResponse) {
            this.setState({
                car: getCarResponse.data
            })
        }
    }


    editHandler() {
        this.setState({
            edit: !this.state.edit
        });

    }

    editCloseHandler() {
        this.setState({
            edit: !this.state.edit
        });
    }

    editPassHandler() {
        this.setState({
            editPass: !this.state.editPass
        });

    }

    editPassCloseHandler() {
        this.setState({
            editPass: !this.state.editPass
        });
    }

    async editParamsHandler() {
        await
            axios.patch('/users/me/update-email?email=' + this.state.newEmail, null, {
                headers: {"Authorization": this.props.token}
            }).then(res => {
                if (!res.response)
                    this.setState({message: 'Email successfully changed'});
            }).catch(err => {
                this.setState({message: 'Request was not completed'});
            });

        this.setState({
            edit: !this.state.edit
        });
        this.componentDidMount();

    }

    async editPassParamsHandler() {

        await
            axios.patch('/users/me/update-password?password=' + this.state.newPassword, null, {
                headers: {"Authorization": this.props.token}
            }).then(res => {
                if (!res.response)
                    this.setState({message: 'Password successfully changed'});
            }).catch(err => {
                this.setState({message: 'Request was not completed'});
            });

        this.setState({
            editPass: !this.state.editPass
        });
        this.componentDidMount();

    }


    onFileChange = (event) => {
        this.setState({
            file: event.target.files[0]
        });
    };

    uploadFile = (event) => {
        event.preventDefault();
        this.setState({error: '', msg: ''});

        if (!this.state.file) {
            this.setState({error: 'Please upload a file.'})
            return;
        }

        if (this.state.file.size >= 1000000) {
            this.setState({error: 'File size exceeds limit of 1MB.'})
            return;
        }

        let data = new FormData();
        data.append('upfile', this.state.file);

        fetch('http://localhost:8080/users/avatar', {
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


    changedEmail = (event) => {

        this.setState({newEmail: event.target.value});
    };

    changedPassword = (event) => {
        this.setState({newPassword: event.target.value});

    };

    errorConfirmedHandler = () => {
        this.setState({
            message: null
        });
    };

    toggleModal() {
        this.setState({
            showModal: !this.state.showModal
        })
    }


    render() {

        let responseMessage = (
            <Modal
                show={this.state.message}
                modalClosed={this.errorConfirmedHandler}>
                {this.state.message ? this.state.message : null}
            </Modal>
        );
        let buttonCreateCar = null;
        let car = null;
        if (!this.state.car) {
            buttonCreateCar = (

                <div className="Car">
                    <Button onClick={() => this.toggleModal()}><h1>+CREATE CAR</h1></Button>
                </div>

            );
        }

        if (this.state.car) {
            car = (
                <div>
                    <Car
                        car={this.state.car}
                    />
                </div>
            )
        }


        return (
            <div>
                <h1 className="head">
                    USER INFORMATION
                </h1>
                <Modal show={this.state.edit} modalClosed={() => this.editCloseHandler()}>
                    <div className="cont">
                        <label>Email</label>
                        <input className="input" type="text" onChange={event => this.changedEmail(event)}
                               value={this.state.newEmail}/>
                        <Button className="input save" onClick={() => this.editParamsHandler()}><h2>SAVE</h2></Button>
                    </div>
                </Modal>


                <Modal show={this.state.editPass} modalClosed={() => this.editPassCloseHandler()}>
                    <label>Password</label>
                    <input className="input" type="text" onChange={event => this.changedPassword(event)}
                           value={this.state.newPassword}/>
                    <Button className="input save" onClick={() => this.editPassParamsHandler()}><h2>SAVE</h2></Button>
                </Modal>

                <Modal show={this.state.showModal} modalClosed={() => this.toggleModal()}>
                    <NewCar
                        showModal={this.state.showModal}
                    />
                </Modal>

                <div>
                    <div className="Profile">
                        <ul style={{marginRight: 50, maxWidth: 350}}>
                            <img src={this.state.src} alt="Not found!" style={{width: 350}}/>
                            <div>
                                <input onChange={this.onFileChange} type="file"/>
                                <br/>
                                <button onClick={this.uploadFile}>Upload</button>
                            </div>
                            <div className="edit">
                                <Button onClick={() => this.editPassHandler()}><h3 className="header">CHANGE
                                    PASSWORD <FaUserEdit/></h3>
                                </Button>
                            </div>


                        </ul>
                        <ul style={{maxWidth: 350}}>
                            <h2><TiUser/> Name: <span
                                className="header">{this.state.user.firstName} {this.state.user.lastName}</span></h2>
                            <h2><TiGroup/> Username: <span className="header">{this.state.user.username}</span></h2>
                            <h2><FaEnvelopeOpen/> Email: <span className="header">{this.state.user.email}</span></h2>
                            <h2><FaPhone/> Phone: <span className="header">{this.state.user.phone}</span></h2>
                            <hr/>
                            <div className="edit">
                                <Button onClick={() => this.editHandler()}><h3 className="header">EDIT
                                    PROFILE <FaUserEdit/></h3>
                                </Button>
                            </div>
                        </ul>
                        <ul style={{maxWidth: 400}}>
                            <li className="rating">
                                <h3><FaMedal/> Rating as driver<span
                                    className="header"><h3>{
                                    <StarRatings
                                        rating={this.state.user.ratingAsDriver === null ? 0.0 : this.state.user.ratingAsDriver}
                                        starRatedColor="yellow"
                                        changeRating={this.changeRating}
                                        numberOfStars={5}
                                        name='rating'
                                        starDimension="30px"
                                        starSpacing="6px"
                                    />}</h3></span></h3>
                            </li>
                            <li className="rating">
                                <h3><span><FaMedal/></span> Rating as passenger<span
                                    className="header"><h1>{
                                    <StarRatings
                                        rating={this.state.user.ratingAsPassenger === null ? 0.0 : this.state.user.ratingAsPassenger}
                                        starRatedColor="yellow"
                                        changeRating={this.changeRating}
                                        numberOfStars={5}
                                        name='rating'
                                        starDimension="30px"
                                        starSpacing="6px"
                                    />}</h1></span></h3>
                            </li>
                        </ul>
                    </div>
                    {car}
                    {buttonCreateCar}
                    {responseMessage}
                </div>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        carCreated: state.car.carCreated,

    }
};

const mapDispatchToProps = dispatch => {
    return {
        onCreateCar: (create, token) => dispatch(actions.createCar(create, token)),
        onCarFinishCreate: () => dispatch(actions.carFinishCreate())
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Profile, axios));