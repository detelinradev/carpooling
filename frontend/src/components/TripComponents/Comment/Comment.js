import React, {Component} from 'react';
import './Comment.css';
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import Avatar from '../../../assets/images/image-default.png'

class Comment extends Component {


    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.author.modelId, 'comment',this.props.data.modelId);
    }

    render() {
        let image = Avatar;
        if (this.props.commentImage)
            image = this.props.commentImage;
        return (
            <div >
                <img id="postertest"  style={{width: 30, height: 30, marginTop: 10}}
                     src={image} alt={''}/>
                <p>&nbsp;&nbsp;{this.props.author.firstName} {this.props.author.lastName}:&nbsp;&nbsp;</p>
                <p className="metadata">{this.props.message}</p>
            </div>
        )
    }

}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
        commentImage: state.user.commentImage,
        modelId:state.user.modelId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId, userType,modelId) => dispatch(actions.fetchImageUser(token, userId, userType,modelId)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Comment, axios));
