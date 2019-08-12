import React, {Component} from 'react';
import './Comment.css';
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import Avatar from '../../../assets/images/image-default.png'
import Button from "@material-ui/core/Button";

class Comment extends Component {
    state = {
        src: Avatar
    };

    async componentDidMount() {

        const getDriverAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.author.modelId)
                .then(response => response.blob());

        if (getDriverAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getDriverAvatarResponse)
            })
        }
    }

    render() {
        return (
            <div>
                <img id="postertest" style={{width: 30, height: 30, marginTop: 10}}
                     src={this.state.src} alt={''}/>
                <p>&nbsp;&nbsp;{this.props.author.firstName} {this.props.author.lastName}:&nbsp;&nbsp;</p>
                <p className="metadata">{this.props.message}</p>
            </div>
        )
    }

}

const mapStateToProps = state => {
    return {
        // token: state.auth.token,
        // commentImage: state.user.commentImage,
        // modelId: state.user.modelId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        // onFetchUserImage: (token, userId, userType, modelId) => dispatch(actions.fetchImageUser(token, userId, userType, modelId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Comment, axios));
