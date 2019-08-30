import React, {Component} from 'react';
import './User.css';
import Button from "@material-ui/core/Button";
import {FaMedal, FaUserEdit} from "react-icons/fa";
import {connect} from "react-redux";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../axios-baseUrl";
import Avatar from "../../assets/images/image-default.png";
import StarRatings from "react-star-ratings";

class User extends Component {
    state = {
        src: Avatar,
    };

    async componentDidMount() {
        const getUserAvatarResponse = await
            fetch("http://localhost:8080/users/avatar/" + this.props.data.username,{
                headers:
                    {"Authorization": this.props.token}
            })
                .then(response => response.blob());

        if (getUserAvatarResponse.size > 100) {
            this.setState({
                src: URL.createObjectURL(getUserAvatarResponse)
            })
        }
    }

    render() {

        let user = (
            <div className=" Post">
                <div className="Trip additional-details  cardcont  meta-data-container">
                    <div className="image " style={{width: 300}}>
                        <img id="postertest" className='poster' style={{width: 128}}
                             src={this.state.src} alt={''}/>
                        <p className="meta-data">{this.props.data.firstName} {this.props.data.lastName}</p>
                        <span><FaMedal/></span> Rating as driver <div
                        className="header">{
                        <StarRatings
                            rating={this.props.data.ratingAsDriver}
                            starRatedColor="yellow"
                            changeRating={this.changeRating}
                            numberOfStars={5}
                            name='rating'
                            starDimension="30px"
                            starSpacing="6px"
                        />}</div>
                        <span><FaMedal/></span> Rating as passenger <div
                        className="header">{
                        <StarRatings
                            rating={this.props.data.ratingAsPassenger}
                            starRatedColor="yellow"
                            changeRating={this.changeRating}
                            numberOfStars={5}
                            name='rating'
                            starDimension="30px"
                            starSpacing="6px"
                        />}</div>
                    </div>
                    <div className="ed">
                        <Button onClick={() => this.props.showFullUser(this.props.data)}><h3
                            className="header">DETAILS <FaUserEdit/></h3>
                        </Button>
                    </div>

                    <div className="comps" style={{ paddingTop: 40, width: 200}}>
                        Username<br/><p
                        className="row-xs-6 info meta-data">{this.props.data.username}</p>
                        <hr/>
                        User role<p
                        className="row-xs-6 info meta-data">{this.props.data.role}</p>
                    </div>

                    <div className="comps" style={{ paddingTop: 40, width: 350}}>
                        Email<p
                        className="row-xs-6 info meta-data">{this.props.data.email}</p>
                    </div>

                    <div className="comps" style={{ paddingTop: 40, width: 200}}>
                        Phone<p
                        className="row-xs-6 info meta-data">{this.props.data.phone}</p>
                    </div>
                </div>
            </div>
        );
        return (
            <div>
                {user}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        token: state.auth.token,
    }
};

export default connect(mapStateToProps)(withErrorHandler(User, axios));
