import React, {Component} from 'react';
import {connect} from 'react-redux';
import './SearchTrips.css';
import axios from '../../axios-baseUrl';
import * as actions from '../../store/actions/index';
import Trip from "../../components/TripComponents/Trip/Trip";
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import {checkValidity, updateObject} from "../../shared/utility";
import Input from "../../components/UI/Input/Input";
import Button from "../../components/UI/Button/Button";
import DateTimeFormat from "dateformat";


class SearchTrips extends Component {

    state = {
        startDate: new Date(),
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
            // costPerPassenger: {
            //     elementType: 'input',
            //     elementConfig: {
            //         type: 'text',
            //         placeholder: 'Cost per passenger'
            //     },
            //     value: '',
            //     validation: {
            //         required: true,
            //         isNumeric: true
            //     },
            //     valid: false,
            //     touched: false
            // },
            earliestDepartureTime: {
                elementType: 'date',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Departure time'
                },
                value: '',
                validation: {},
                valid: true,
                touched: false
            },
            latestDepartureTime: {
                elementType: 'date',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Departure time'
                },
                value: '',
                validation: {},
                valid: true,
                touched: false
            },
            smokingAllowed: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'yes', displayValue: 'yes'},
                        {value: 'no', displayValue: 'no'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            },
            petsAllowed: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'yes', displayValue: 'yes'},
                        {value: 'no', displayValue: 'no'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            },
            luggageAllowed: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'yes', displayValue: 'yes'},
                        {value: 'no', displayValue: 'no'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            },
            airConditioned: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'yes', displayValue: 'yes'},
                        {value: 'no', displayValue: 'no'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            }
        },
        formIsValid: false,
        changedDate: new Date()

    };

    inputDateChangedHandler = (date, name) => {
        console.log(date)
        console.log(name)
        this.setState({
            startDate: date
        });

        const updatedFormElement = updateObject(this.state.createForm[name], {
            value: date,
            //  valid: checkValidity(date, this.state.createForm['departureTime'].validation),
            touched: true
        });
        const updatedCreateForm = updateObject(this.state.createForm, {
            [name]: updatedFormElement
        });

        let formIsValid = true;
        for (let inputIdentifier in updatedCreateForm) {
            formIsValid = updatedCreateForm[inputIdentifier].valid && formIsValid;
        }
        this.setState({createForm: updatedCreateForm, formIsValid: formIsValid});
    };


    createHandler = (event) => {
        event.preventDefault();
        if( this.state.createForm.earliestDepartureTime.value !== '') {
            this.state.createForm.earliestDepartureTime.value = DateTimeFormat(this.state.createForm.earliestDepartureTime.value, "yyyy-mm-dd HH:MM");
        }
        if( this.state.createForm.latestDepartureTime.value !== '') {
            this.state.createForm.latestDepartureTime.value = DateTimeFormat(this.state.createForm.latestDepartureTime.value, "yyyy-mm-dd HH:MM");
        }

        let stringData = "?";
        let questionMark = "";
        for (let formElementIdentifier in this.state.createForm) {
            if (this.state.createForm[formElementIdentifier].value !== '') {
                stringData = stringData + questionMark + formElementIdentifier + '=' + this.state.createForm[formElementIdentifier].value;
                questionMark = '&';
            }
        }
        this.setState({
            startDate: new Date(),
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
                earliestDepartureTime: {
                    elementType: 'date',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Departure time'
                    },
                    value: '',
                    validation: {},
                    valid: true,
                    touched: false
                },
                latestDepartureTime: {
                    elementType: 'date',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Departure time'
                    },
                    value: '',
                    validation: {},
                    valid: true,
                    touched: false
                },
                smokingAllowed: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: 'yes', displayValue: 'yes'},
                            {value: 'no', displayValue: 'no'}
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true
                },
                petsAllowed: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: 'yes', displayValue: 'yes'},
                            {value: 'no', displayValue: 'no'}
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true
                },
                luggageAllowed: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: 'yes', displayValue: 'yes'},
                            {value: 'no', displayValue: 'no'}
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true
                },
                airConditioned: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: 'yes', displayValue: 'yes'},
                            {value: 'no', displayValue: 'no'}
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true
                }
            },
            formIsValid: false,
            changedDate: new Date()
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
                        startDate={this.state.startDate}
                        dateChange={(date) => this.inputDateChangedHandler(date, formElement.id)}
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
                        Search Trips
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

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(SearchTrips, axios));