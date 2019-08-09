import React, {Component} from 'react';
import './Comment.css';
import * as actions from "../../../store/actions";
import {connect} from "react-redux";
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";

class Comment extends Component {


    componentDidMount() {
        this.props.onFetchUserImage(this.props.token, this.props.author.modelId,'comment');
    }
    render() {
        let image = 'https://cdn.pixabay.com/photo/2018/02/08/22/27/flower-3140492__340.jpg';
        if (this.props.commentImage)
            image = this.props.commentImage;
        return (
            <div className=" Post">
                <div className="Trip additional-details  cardcont  meta-data-container">
                    <p className="image">
                        <p className="meta-data">Comment of {this.props.author.firstName} {this.props.author.lastName}</p></p>
                    <img id="postertest" className='poster' style={{width: 64, height: 64}}
                         src={image} alt={''}/>
                    <p className="row-xs-6 info">Message<p className="meta-data">{this.props.message}</p></p>
                </div>
            </div>)
    }

}
const mapStateToProps = state => {
    return {
        token: state.auth.token,
        commentImage:state.user.commentImage
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUserImage: (token, userId,userType) => dispatch(actions.fetchImageUser(token, userId,userType)),
    };
};

export default connect(mapStateToProps,mapDispatchToProps)(withErrorHandler(Comment, axios));
