import React, {Component} from 'react';
import {connect} from 'react-redux';

import Button from '../../components/UI/Button/Button';
import Spinner from '../../components/UI/Spinner/Spinner';
import './NewTrip.css';
import axios from '../../axios-baseUrl';
import Input from '../../components/UI/Input/Input';
import withErrorHandler from '../../hoc/withErrorHandler/withErrorHandler'
import * as actions from '../../store/actions/index';
import {updateObject, checkValidity} from '../../shared/utility';
import 'react-dates/initialize';
import DateTimeFormat from "dateformat";


class NewTrip extends Component {
    state = {
        startDate: new Date(),
        changedDate:new Date(),
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
                elementType: 'date',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Departure time'
                },
                value: '',
                validation: {
                    required: false,
                },
                valid: true,
                touched: false
            },
            availablePlaces: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: '1', displayValue: '1'},
                        {value: '2', displayValue: '2'},
                        {value: '3', displayValue: '3'},
                        {value: '4', displayValue: '4'},
                    ]
                },
                value: '',
                validation: {},
                valid: true,
            },
            costPerPassenger: {
                elementType: 'input',
                elementConfig: {
                    type: 'number',
                    placeholder: 'Cost per passenger'
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: false,
                touched: false
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
            smokingAllowed: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'true', displayValue: 'yes'},
                        {value: 'false', displayValue: 'no'}
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
                        {value: 'true', displayValue: 'yes'},
                        {value: 'false', displayValue: 'no'}
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
                        {value: 'true', displayValue: 'yes'},
                        {value: 'false', displayValue: 'no'}
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
                        {value: 'true', displayValue: 'yes'},
                        {value: 'false', displayValue: 'no'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            }
        },
        formIsValid: false,
        startLocation: 0,
        endLocation: 0,
        travelDistance: 0,
        tripDuration: 0,

    };
   componentDidUpdate(prevProps, prevState, snapshot) {
       if(this.props.tripCreated) {
           this.props.history.push('/myTrips');
           this.props.onTripCreated(false);
       }
   }

    async getCoordinates() {
        await axios.get("http://dev.virtualearth.net/REST/v1/Locations/"+this.state.createForm.origin.value+"?key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
            .then(response => {

                this.setState({
                    startLocation: response.data.resourceSets[0].resources[0].point.coordinates
                });
            });

        await axios.get("http://dev.virtualearth.net/REST/v1/Locations/"+this.state.createForm.destination.value+"?key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
            .then(response => {
                this.setState({
                    endLocation: response.data.resourceSets[0].resources[0].point.coordinates
                });
            });

        await axios.get("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins="+this.state.startLocation+"&destinations="+this.state.endLocation+"&travelMode=driving&key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
            .then(response => {
                this.setState({
                    travelDistance: response.data.resourceSets[0].resources[0].results[0].travelDistance,
                    tripDuration: response.data.resourceSets[0].resources[0].results[0].travelDuration,
                });
            });

    }

    createHandler = async (event) => {
        event.preventDefault();
        const formData = {};
        for (let formElementIdentifier in this.state.createForm) {
            formData[formElementIdentifier] = this.state.createForm[formElementIdentifier].value;
        }
        await this.getCoordinates();
        formData["tripDuration"] = this.state.tripDuration;
       formData["departureTime"]= DateTimeFormat(this.state.createForm.departureTime.value, "yyyy-mm-dd HH:MM");

       console.log(formData)
        this.props.onCreateTrip(formData, this.props.token);
        this.setState({
            startDate: new Date(),
            changedDate:new Date(),
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
                    elementType: 'date',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Departure time'
                    },
                    value: '',
                    validation: {
                        required: false,
                    },
                    valid: true,
                    touched: false
                },
                availablePlaces: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: '1', displayValue: '1'},
                            {value: '2', displayValue: '2'},
                            {value: '3', displayValue: '3'},
                            {value: '4', displayValue: '4'},
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true,
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
                    touched: false
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
                smokingAllowed: {
                    elementType: 'select',
                    elementConfig: {
                        options: [
                            {value: '', displayValue: 'choose option'},
                            {value: 'true', displayValue: 'yes'},
                            {value: 'false', displayValue: 'no'}
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
                            {value: 'true', displayValue: 'yes'},
                            {value: 'false', displayValue: 'no'}
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
                            {value: 'true', displayValue: 'yes'},
                            {value: 'false', displayValue: 'no'}
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
                            {value: 'true', displayValue: 'yes'},
                            {value: 'false', displayValue: 'no'}
                        ]
                    },
                    value: '',
                    validation: {},
                    valid: true
                }
            },
            formIsValid: false,
            startLocation: 0,
            endLocation: 0,
            travelDistance: 0,
            tripDuration: 0,

        })
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

    inputDateChangedHandler = (date,name) => {
        this.setState({
            startDate : date
        });

        const updatedFormElement = updateObject(this.state.createForm[name], {
            value: date,
            touched: true
        });
        const updatedCreateForm = updateObject(this.state.createForm, {
            [name]: updatedFormElement
        });

        this.setState({createForm: updatedCreateForm});
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
                        dateChange={(date) => this.inputDateChangedHandler(date,formElement.id)}
                        changed={(event) => this.inputChangedHandler(event, formElement.id)}/>
                ))}
                <Button btnType="Success" disabled={!this.state.formIsValid}>CREATE</Button>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        return (
            <div className="NewTrip todore">
                Enter your Trip Data
                {form}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        tripCreated:state.trip.tripCreated
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onCreateTrip: (create, token) => dispatch(actions.createTrip(create, token)),
        onTripCreated:(tripCreated) => dispatch(actions.tripCreated(tripCreated))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(NewTrip, axios));