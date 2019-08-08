import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Home.css';
import axios from '../../axios-baseUrl';
import {FaAndroid} from 'react-icons/fa';
import * as actions from '../../store/actions/index';
import Trip from "../../components/TripComponents/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import {checkValidity, updateObject} from "../../shared/utility";
import Input from "../../components/UI/Input/Input";
import Button from "../../components/UI/Button/Button";


class Home extends Component {

    state = {
        //focused: false,
        createForm: {
            origin: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Origin'
                },
                value: '',
                validation: {
                    required: true
                },
                valid: false,
                touched: false
            },
            destination: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Destination'
                },
                value: '',
                validation: {
                    required: true
                },
                valid: false,
                touched: false
            },
            departureTime: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Departure time'
                },
                value: '',
                validation: {
                    required: true,
                    // minLength: 5,
                    // maxLength: 5,
                    //isNumeric: true
                },
                valid: false,
                touched: false
            },
            availablePlaces: {
                elementType: 'input',
                elementConfig: {
                    type: 'number',
                    placeholder: 'Available places'
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: false,
                touched: false
            },
            tripDuration: {
                elementType: 'input',
                elementConfig: {
                    type: 'number',
                    placeholder: 'Trip duration'
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: false,
                touched: false
            },
            costPerPassenger: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Cost per passenger'
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: false,
                touched :false
            },
            message: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Message'
                },
                value: '',
                validation: {
                    required: true
                },
                valid: false,
                touched: false
            },
            // date: {
            //     elementType: 'select',
            //     elementConfig: {
            //         type: 'text',
            //         placeholder: 'message'
            //     },
            //     value: null,
            //     validation: {
            //         required: true
            //     },
            //     valid: false,
            //     touched: false
            // }
        },
        formIsValid: false,

    };

    createHandler = (event) => {
        event.preventDefault();

        let stringData ="?";
        let questionMark="";
        for (let formElementIdentifier in this.state.createForm) {
            if(this.state.createForm[formElementIdentifier].value !=='')
            stringData = stringData + questionMark + formElementIdentifier + '=' + this.state.createForm[formElementIdentifier].value;
            questionMark = '&';
        }
        this.props.onFetchTrips(this.props.token,stringData);

    };

    inputChangedHandler = (event, inputIdentifier) => {

        const updatedFormElement = updateObject(this.state.createForm[inputIdentifier], {
            value: event.target.value,
            valid: checkValidity(event.target.value, this.state.createForm[inputIdentifier].validation),
            touched: true
        });
        const updatedCreateForm = updateObject(this.state.createForm, {
            [inputIdentifier]: updatedFormElement
        });

        let formIsValid = true;
        for (let inputIdentifier in updatedCreateForm) {
            formIsValid = updatedCreateForm[inputIdentifier].valid && formIsValid;
        }
        this.setState({createForm: updatedCreateForm, formIsValid: formIsValid});
    };

    showFullTrip = (trip) => {
        this.props.onShowFullTrip(trip);
        this.props.history.push('/fullTrip');
    };

    fetchUsersImages = (userId) => {
        console.log(1)
        this.props.onFetchUserImage(userId);
        // this.props.history.push('/fullTrip');
    };


    render() {
        const formElementsArray = [];
        for (let key in this.state.createForm) {
            formElementsArray.push({
                id: key,
                config: this.state.createForm[key]
            });
        }
        let form = (
            <form onSubmit={this.createHandler}>

                {formElementsArray.map(formElement => (
                    <Input
                        key={formElement.id}
                        elementType={formElement.config.elementType}
                        elementConfig={formElement.config.elementConfig}
                        value={formElement.config.value}
                        invalid={!formElement.config.valid}
                        shouldValidate={formElement.config.validation}
                        touched={formElement.config.touched}
                        changed={(event) => this.inputChangedHandler(event, formElement.id)}/>
                ))}
                <Button btnType="Success"
                       // disabled={!this.state.formIsValid}
                >CREATE</Button>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        let trips = <Spinner/>;
        if (this.props.token && this.props.trips) {

            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    data = {trip}
                    driver={trip.driver}
                    passengers={trip.passengers}
                    comments={trip.comments}
                    car ={trip.car}
                    showFullTrip={this.showFullTrip}
                    fetchUserImage={this.fetchUsersImages}
                    token={this.props.token}
                    userImage={this.props.userImage}
                />
            ))
        }
        if (this.props.token) {
            return (
                <div className="todore">
                    <h1 className="header">THE PERFECT PLACE TO FIND <br/> THE FASTEST WAY TO TRAVEL</h1>
                    <div>
                        <div className="NewTrip">
                            <h4>Search Trips</h4>
                            {form}
                        </div>
                    </div>
                    {trips}
                </div>)
        }

        return (
            <div className="todore">

                <h1 className="header">THE PERFECT PLACE TO FIND <FaAndroid/><br/> THE FASTEST WAY TO TRAVEL</h1>
                <hr style={{width: '70%'}}/>
                <h2 style={{textAlign: "center"}}>
                    SEARCH AMONG ALL KIND OF VEHICLES AND DESTINATIONS!
                </h2>
            </div>)
    }
}

const mapStateToProps = state => {
    return {
        trips: state.trip.trips,
        loading: state.trip.loading,
        token: state.auth.token,
        userImage: state.user.userImage
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token,formData) => dispatch(actions.fetchTrips(token, formData)),
        onShowFullTrip: (trip) => dispatch(actions.showFullTrip(trip)),
        onFetchUserImage:(userId)=> dispatch(actions.fetchImageUser(this.props.token,userId))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Home, axios));