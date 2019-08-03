import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Profile.css';
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from '../../axios-baseUrl';
import Car from "./Car";
import Button from "@material-ui/core/Button";
import Auxiliary from "../../hoc/Auxiliary/Auxiliary";
import Modal from "../../components/UI/Modal/Modal";


class Profile extends Component {
    state = {
        user: {},
        hasCar: false,
        isToggleOn: false,
        edit: false,
        testemail: "eeec@gmail.com"
    };


    async componentDidMount() {
        const getMeResponse = await
            axios.get('/users/me', {
                headers:
                    {"Authorization": this.props.token}
            });
        this.setState({
            user: getMeResponse.data,
        });


    }


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
        })
    }

    async editFields() {
         await
        axios.patch('/users/me/update-email?email='+this.state.testemail,null,{
            headers: {"Authorization": this.props.token}
        });

    }


    changed = (event) => {
        // this.state.email = event.target.value;

        this.setState({ [this.state.testemail]: event.target.value });
        this.editFields();
    };


    render() {

        return (
            <Auxiliary>
                <h1 className="head">
                    USER INFORMATION
                </h1>
                <Modal show={this.state.edit} modalClosed={() => this.editCloseHandler()}>
                    <input type="text" onChange={event => this.changed(event)} value={this.state.email} />
                    <Button className="head" onClick={() => this.editFields()}><h2>SAVE</h2></Button>
                </Modal>
                <div>
                    <div className="Profile">
                        <ul>
                            <img src="https://www.w3schools.com/howto/img_avatar.png" alt="car pooling"/>
                            <Button className="edit" onClick={() => this.editHandler()}><h3 className="header">EDIT PROFILE</h3>
                            </Button>
                        </ul>
                        <ul>
                            <li><h1>Name:<span className="header">{this.state.user.firstName} {this.state.user.lastName}</span></h1></li>
                            <li><h1>Username: <span className="header">{this.state.user.username}</span></h1></li>
                            <li><h2>Email: <span className="header">{this.state.user.email}</span></h2></li>
                            <li><h2>Phone: <span className="header">{this.state.user.phone}</span></h2></li>
                            <hr/>
                            <li className="feedback"><Button><h3 className="header">Show Feedback</h3></Button></li>
                        </ul>
                        <ul>
                            <li className="rating">
                                <h3>Rating as driver<span
                                    className="header"><h1>{this.state.user.ratingAsDriver}</h1></span></h3>
                            </li>
                            <li className="rating">
                                <h3>Rating as passenger<span
                                    className="header"><h1>{this.state.user.ratingAsPassenger}</h1></span></h3>
                            </li>
                        </ul>
                    </div>
                    <button className="Profile" onClick={() => this.toggleCarHandler()}>
                        {this.state.isToggleOn ? <Car hasCar={this.state.user.hasCar}/> :
                            <div>Toggle car</div>}</button>
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