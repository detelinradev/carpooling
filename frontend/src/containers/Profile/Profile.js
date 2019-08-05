import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Car from "./Car";
import Button from "@material-ui/core/Button";
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import Modal from "../../components/UI/Modal/Modal";
import Avatar from '../../assets/images/images.png';
import { TiUser, TiGroup} from "react-icons/ti";
import { FaEnvelopeOpen, FaPhone, FaMedal, FaUserEdit} from "react-icons/fa";



class Profile extends Component {
    state = {
        user: {},
        isToggleOn: false,
        edit: false,
        newEmail: "",
        newPassword: "",
        src: Avatar,
        error:'',
        msg:'',
        file:''
    };


    async componentDidMount() {

        const getAvatarResponse = await
            fetch('http://localhost:8080/users/avatar',
            { headers: {"Authorization": this.props.token}})
                .then(response => response.blob());

        if(getAvatarResponse.size>100){
            console.log(getAvatarResponse);
            this.setState({
                src: URL.createObjectURL(getAvatarResponse)
            })}


        const getMeResponse = await
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            });

        this.setState({
            user: getMeResponse.data,
            newEmail: getMeResponse.data.email,

        })};

    // const getAvatarResponseAxios = await
    //     axios('/users/avatar',
    //     { headers: {"Authorization": this.props.token}})
    //         .then(response => {
    //             const blob = new Blob([response.data], {
    //                 responseType: 'image/jpeg',
    //             });
    //             this.setState({
    //                 imge: URL.createObjectURL(blob)
    //             });
    //             console.log(this.state.imge);
    //         });
    //         // .then(blob => {
    //         //     this.setState({ imge: URL.createObjectURL(blob) })
    //         // });

    toggleCarHandler() {
        this.setState({
            isToggleOn: !this.state.isToggleOn
        });
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


    // GetImageHandler = () => {
    //     const getAvatarResponse =
    //         fetch('http://localhost:8080/users/avatar',
    //             { headers: {"Authorization": this.props.token}})
    //             .then(response => response.blob())
    //     ;
    //
    //     this.setState({
    //         src: URL.createObjectURL(getAvatarResponse)
    //     })
    //     ;};



    changedEmail = (event) => {

        this.setState({ newEmail: event.target.value});
    };

    changedPassword = (event) => {
        if(event.target.value.size>1) {
            this.setState({newPassword: event.target.value});
        }
    };


    render() {
        //
        // if(!this.state.src){
        //     this.setState({
        //         src: Avatar
        //     })
        // }

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
                        <ul style={{marginRight: 50,maxWidth: 350}} >
                            <img  src={this.state.src} alt="Not found!"/>
                            <div>
                            <input onChange={this.onFileChange} type="file"/>
                            <br/>
                            <button onClick={this.uploadFile}>Upload</button>
                            </div>
                            <div className="edit">
                            <Button  onClick={() => this.editHandler()}><h3 className="header">EDIT PROFILE <FaUserEdit/></h3>
                            </Button>
                            </div>
                        </ul>
                        <ul style={{maxWidth: 500}}>
                            <h2><TiUser/> Name:  <span className="header">{this.state.user.firstName} {this.state.user.lastName}</span></h2>
                            <h2><TiGroup/> Username: <span className="header">{this.state.user.username}</span></h2>
                            <h2><FaEnvelopeOpen/> Email: <span className="header">{this.state.user.email}</span></h2>
                            <h2><FaPhone/> Phone:  <span className="header">{this.state.user.phone}</span></h2>
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
                    <Car/>

                    {/*<button className="Car" onClick={() => this.toggleCarHandler()}>*/}
                    {/*    {this.state.isToggleOn ? <div/> :*/}
                    {/*        <div><h2>SHOW CAR</h2></div>}</button>*/}
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