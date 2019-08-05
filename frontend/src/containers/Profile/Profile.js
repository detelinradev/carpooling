import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Car from "./Car";
import Button from "@material-ui/core/Button";
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import Modal from "../../components/UI/Modal/Modal";
import { TiUser, TiGroup} from "react-icons/ti";
import { FaEnvelopeOpen, FaPhone, FaMedal, FaUserEdit} from "react-icons/fa";



class Profile extends Component {
    state = {
        user: {},
        hasCar: false,
        isToggleOn: false,
        edit: false,
        newEmail: "",
        newPassword: "",
        imge: "",
    };


    async componentDidMount() {

        const getAvatarResponse = await
            fetch('http://localhost:8080/users/avatar',
            { headers: {"Authorization": this.props.token}})
                .then(response => response.blob())
                .then(blob => {
                    this.setState({ imge: URL.createObjectURL(blob) })
                });


        const getMeResponse = await
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            });
        // console.log(getAvatarResponse.headers['content-type']);
        // console.log(getAvatarResponse.data);

        this.setState({
            user: getMeResponse.data,
            newEmail: getMeResponse.data.email,
            // imge: URL.createObjectURL(getAvatarResponse)
            // imge: `data:${getAvatarResponse.headers['content-type']}
            //     ;base64,${btoa(
            //     String.fromCharCode(...new Uint8Array(getAvatarResponse.data)))}`
        })};

    //
    // getBase64() {
    //     return axios
    //         .get('http://localhost:8080/users/avatar', {
    //             headers: {"Authorization": this.props.token},
    //             responseType: 'arraybuffer',
    //
    //         })
    //         .then(response => new Buffer(response.data, 'binary').toString('base64'))
    // }


    toggleCarHandler() {
        this.setState({
            isToggleOn: !this.state.isToggleOn
        });
    }

    editHandler() {
        this.setState({
            edit: !this.state.edit
        })
    }
    editCloseHandler() {
        this.setState({
            edit: !this.state.edit
        });
    }

    async editParamsHandler() {
         await
        axios.patch('/users/me/update-email?email='+this.state.newEmail,null,{
            headers: {"Authorization": this.props.token}
        });

        if(this.state.newPassword.size>1) {
            await
                axios.patch('/users/me/update-password?password=' + this.state.newPassword, null, {
                    headers: {"Authorization": this.props.token}
                });
        }

        this.setState({
            edit: !this.state.edit
        });

    }



    changedEmail = (event) => {

        this.setState({ newEmail: event.target.value});
    };

    changedPassword = (event) => {
        if(event.target.value.size>1) {
            this.setState({newPassword: event.target.value});
        }
    };


    render() {

        return (
            <Auxiliary>
                <h1 className="head">
                    USER INFORMATION
                </h1>
                <Modal show={this.state.edit} modalClosed={() => this.editCloseHandler()}>
                    <div className="cont">
                    <label >Email</label>
                    <input className="input" type="text" onChange={event => this.changedEmail(event)} value={this.state.newEmail} />
                    <label >Password</label>
                    <input className="input" type="text" onChange={event => this.changedPassword(event)} value={this.state.newPassword} />

                    <Button className="input save" onClick={() => this.editParamsHandler()}><h2>SAVE</h2></Button>
                    </div>
                </Modal>
                <div >
                    <div className="Profile">
                        <ul style={{marginRight: 50}}>
                            {/*<img src="https://www.w3schools.com/howto/img_avatar.png" alt="car pooling"/>*/}
                            <img src={this.state.imge} alt="car pooling"/>
                            <div className="edit">
                            <Button  onClick={() => this.editHandler()}><h3 className="header">EDIT PROFILE <FaUserEdit/></h3>
                            </Button>
                            </div>
                        </ul>
                        <ul style={{maxWidth: 500}}>
                            <h1><TiUser/> Name:  <span className="header">{this.state.user.firstName} {this.state.user.lastName}</span></h1>
                            <h1><TiGroup/> Username: <span className="header">{this.state.user.username}</span></h1>
                            <h1><FaEnvelopeOpen/> Email: <span className="header">{this.state.user.email}</span></h1>
                            <h1><FaPhone/> Phone:  <span className="header">{this.state.user.phone}</span></h1>
                            <hr/>
                            <li className="feedback"><Button><h3 className="header">Show Feedback</h3></Button></li>
                        </ul>
                        <ul>
                            <li className="rating">
                                <h3><FaMedal/>  Rating as driver<span
                                    className="header"><h1>{this.state.user.ratingAsDriver}</h1></span></h3>
                            </li>
                            <li className="rating">
                                <h3><span><FaMedal/></span>  Rating as passenger<span
                                    className="header"><h1>{this.state.user.ratingAsPassenger}</h1></span></h3>
                            </li>
                        </ul>
                    </div>
                    <button className="Car" onClick={() => this.toggleCarHandler()}>
                        {this.state.isToggleOn ? <Car hasCar={this.state.user.hasCar}/> :
                            <div><h2>SHOW CAR</h2></div>}</button>
                </div>

            </Auxiliary>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(Profile, axios));