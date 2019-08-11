import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Admin.css';
import axios from '../../axios-baseUrl';
import * as actions from '../../store/actions/index';
import Trip from "../../components/TripComponents/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import {checkValidity, updateObject} from "../../shared/utility";
import Input from "../../components/UI/Input/Input";
import Button from "../../components/UI/Button/Button";
import DateTimeFormat from "dateformat";


class Admin extends Component {

    state = {
        createForm: {
            username: {
                elementType: 'input',
                elementConfig: {
                    type: 'username',
                    placeholder: 'Username'
                },
                value: '',
                validation: {
                    required: true,
                },
                valid: false,
                touched: false
            },
            firstName: {
                elementType: 'input',
                elementConfig: {
                    type: 'firstName',
                    placeholder: 'First name'
                },
                value: '',
                validation: {
                    required: true,
                },
                valid: false,
                touched: false
            },
            lastName: {
                elementType: 'input',
                elementConfig: {
                    type: 'lastName',
                    placeholder: 'Last name'
                },
                value: '',
                validation: {
                    required: true,
                },
                valid: false,
                touched: false
            },
            email: {
                elementType: 'input',
                elementConfig: {
                    type: 'email',
                    placeholder: 'Email'
                },
                value: '',
                validation: {
                    required: true,
                    isEmail: true
                },
                valid: false,
                touched: false
            },
            phone: {
                elementType: 'input',
                elementConfig: {
                    type: 'phone',
                    placeholder: 'Phone',
                    minLength: 10,
                    maxLength: 10
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true,
                },
                valid: false,
                touched: false
            }
        },
        formIsValid: false
    };


    createHandler = (event) => {
        event.preventDefault();

        let stringData = "?";
        let questionMark = "";
        for (let formElementIdentifier in this.state.createForm) {
            if (this.state.createForm[formElementIdentifier].value !== '') {
                stringData = stringData + questionMark + formElementIdentifier + '=' + this.state.createForm[formElementIdentifier].value;
                questionMark = '&';
            }
        }
        this.setState({
            createForm: {
                username: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'username',
                        placeholder: 'Username'
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false
                },
                firstName: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'firstName',
                        placeholder: 'First name'
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false
                },
                lastName: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'lastName',
                        placeholder: 'Last name'
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: false,
                    touched: false
                },
                email: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'email',
                        placeholder: 'Email'
                    },
                    value: '',
                    validation: {
                        required: true,
                        isEmail: true
                    },
                    valid: false,
                    touched: false
                },
                phone: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'phone',
                        placeholder: 'Phone'
                    },
                    value: '',
                    validation: {
                        required: true,
                        isNumeric: true,
                        minLength: 10,
                        maxLength: 10
                    },
                    valid: false,
                    touched: false
                }
            },
            formIsValid: false,
        });
        this.props.onFetchTrips(this.props.token, stringData);
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
        let isJoined;
        console.log(trip)
        const currentUserName = this.props.username;
        let tripJoined = 'Join Trip';
        let passengers = trip.passengers.map(passenger =>
            (passenger.username === currentUserName)
        );
        isJoined = passengers.includes(true);
        if (isJoined) {
            tripJoined = 'Trip joined'
        }
        let isRequested;
        if (trip.notApprovedPassengers) {
            let notApproved = trip.notApprovedPassengers.map(passenger =>
                (passenger.username === currentUserName)
            );
            isRequested = notApproved.includes(true);
            if (isRequested) {
                tripJoined = 'Request sent'
            }
        }
        if (trip.driver.username === currentUserName) {
            tripJoined = ''
        }

        console.log(tripJoined)
        this.props.onShowFullTrip(trip, tripJoined, 'No');
        this.props.history.push('/fullTrip');
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
                        name={formElement.id}
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
                >SEARCH</Button>
            </form>
        );
        // if (this.props.loading) {
        //     form = <Spinner/>;
        // }
        let trips = <Spinner/>;
        if (!this.props.loading) {

            trips = this.props.trips.map(trip => (
                <Trip
                    key={trip.id}
                    data={trip}
                    driver={trip.driver}
                    passengers={trip.passengers}
                    comments={trip.comments}
                    car={trip.car}
                    showFullTrip={this.showFullTrip}
                />
            ))
        }
        return (
            <div className="todore">
                <div>
                    <div className="SearchTrips">
                        Search Users
                        {form}
                    </div>
                </div>
                {trips}
            </div>)
    }
}

const mapStateToProps = state => {
    return {
        trips: state.trip.trips,
        loading: state.trip.loading,
        token: state.auth.token,
        username: state.auth.userId
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchTrips: (token, formData) => dispatch(actions.fetchTrips(token, formData)),
        onShowFullTrip: (trip, tripJoined, requestSent) => dispatch(actions.showFullTrip(trip, tripJoined, requestSent)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Admin, axios));