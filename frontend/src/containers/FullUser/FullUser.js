import React, {Component} from 'react';
import {connect} from 'react-redux';
import './FullUser.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Button from "@material-ui/core/Button";
import Modal from "../../components/UI/Modal/Modal";
import Avatar from '../../assets/images/image-default.png';
import {TiUser, TiGroup} from "react-icons/ti";
import {FaEnvelopeOpen, FaPhone, FaMedal, FaUserEdit} from "react-icons/fa";
import StarRatings from 'react-star-ratings';
import * as actions from "../../store/actions";
import UpdateUser from "./UpdateUser/UpdateUser";
import CarAvatar from "../../assets/images/cars-noimage_sedan-lrg.png";


class FullUser extends Component {
    state = {
        src: Avatar,
        srcCar: CarAvatar,
        showModal: false,
        car: ''
    };


    async componentDidMount() {

        const getAvatarResponse = await
            fetch('http://localhost:8080/users/avatar/' + this.props.user.modelId,
                {headers: {"Authorization": this.props.token}})
                .then(response => response.blob());
        if (getAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getAvatarResponse)
            })
        }

        const getCarAvatarResponse = await
            fetch('http://localhost:8080/users/avatar/car/' + this.props.user.modelId,
                {headers: {"Authorization": this.props.token}})
                .then(response => response.blob());

        if (getCarAvatarResponse.size > 100) {
            this.setState({
                srcCar: URL.createObjectURL(getCarAvatarResponse)
            })
        }

        const getCarResponse = await axios.get('/car/' + this.props.user.modelId, {
            headers:
                {"Authorization": this.props.token}
        });

        if (getCarResponse) {
            this.setState({
                car: getCarResponse.data
            })
        }
    };

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.userUpdate) {
            this.props.onFetchUser(this.props.token,this.props.user.username);
            this.props.onUserFinishUpdate(false);
            this.toggleModal()

        }
    }

    async deleteUser() {
        axios.patch('/users/' + this.props.user.modelId + '/delete', null, {
            headers: {"Authorization": this.props.token}
        }).then(res => this.props.history.push('/admin'));

    }

    toggleModal() {
        this.setState({
            showModal: !this.state.showModal
        })
    }


    render() {
        let updateUser = (

            <Modal show={this.state.showModal} modalClosed={() => this.toggleModal()}>
                <UpdateUser
                    showModal={this.state.showModal}
                    data={this.props.user}
                />
            </Modal>

        );
        let car = (
            <div className="Car">
                <ul>
                    <img style={{maxWidth: 350}}
                         src={this.state.srcCar}
                         alt="car pooling"/>
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
        );


        return (
            <div>
                <h1 className="head">
                    USER INFORMATION
                </h1>
                <div>
                    <div className="Profile">
                        <ul style={{marginRight: 50, maxWidth: 350}}>
                            <img src={this.state.src} alt="Not found!"/>
                            <div className="edit">
                                <Button onClick={() => this.deleteUser()}><h3 className="header">DELETE
                                    USER <FaUserEdit/></h3>
                                </Button>
                            </div>
                        </ul>
                        <ul style={{maxWidth: 350}}>
                            <h2><TiUser/> Name: <span
                                className="header">{this.props.user.firstName} {this.props.user.lastName}</span></h2>
                            <h2><TiGroup/> Username: <span className="header">{this.props.user.username}</span></h2>
                            <h2><FaEnvelopeOpen/> Email: <span className="header">{this.props.user.email}</span></h2>
                            <h2><FaPhone/> Phone: <span className="header">{this.props.user.phone}</span></h2>
                            <h2><TiUser/> User role: <span
                                className="header">{this.props.user.role}</span></h2>
                            <hr/>
                            <div className="edit">
                                <Button onClick={() => this.toggleModal()}><h3 className="header">EDIT
                                    PROFILE <FaUserEdit/></h3>
                                </Button>
                            </div>
                        </ul>
                        <ul style={{maxWidth: 400}}>
                            <li className="rating">
                                <h3><FaMedal/> Rating as driver<span
                                    className="header"><h3>{
                                    <StarRatings
                                        rating={this.props.user.ratingAsDriver}
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
                                        rating={this.props.user.ratingAsPassenger}
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
                    {updateUser}
                </div>
            </div>

        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        user: state.user.user,
        userUpdate: state.user.userUpdated,

    }
};

const mapDispatchToProps = dispatch => {
    return {
        onFetchUser:(token,userId) => dispatch(actions.fetchUser(token,userId)),
        onUserFinishUpdate: (userUpdated) => dispatch(actions.userFinishUpdate(userUpdated))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(FullUser, axios));