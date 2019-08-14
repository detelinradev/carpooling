import React, {Component} from 'react';
import {connect} from 'react-redux';

import Button from '../../../components/UI/Button/Button';
import Spinner from '../../../components/UI/Spinner/Spinner';
import './UpdateTrip.css';
import axios from '../../../axios-baseUrl';
import Input from '../../../components/UI/Input/Input';
import withErrorHandler from '../../../hoc/withErrorHandler/withErrorHandler'
import * as actions from '../../../store/actions/index';
import {updateObject, checkValidity} from '../../../shared/utility';
import 'react-dates/initialize';
import DateTimeFormat from "dateformat";


class UpdateTrip extends Component {
    state = {
        startDate: new Date(),
        changedDate: new Date(),
        createForm: {
            origin: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Origin'
                },
                value: this.props.data.origin,
                validation: {
                    required: true
                },
                valid: true,
                touched: false
            },
            destination: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Destination'
                },
                value: this.props.data.destination,
                validation: {
                    required: true
                },
                valid: true,
                touched: false
            },
            departureTime: {
                elementType: 'date',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Departure time'
                },
                value: this.props.data.departureTime,
                validation: {
                    required: false,
                },
                valid: true,
                touched: false
            },
            availablePlaces: {
                elementType: 'input',
                elementConfig: {
                    type: 'number',
                    placeholder: 'Available places'
                },
                value: this.props.data.availablePlaces,
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: true,
                touched: false
            },
            costPerPassenger: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Cost per passenger'
                },
                value: this.props.data.costPerPassenger,
                validation: {
                    required: true,
                    isNumeric: true
                },
                valid: true,
                touched: false
            },
            message: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Message'
                },
                value: this.props.data.message,
                validation: {
                    required: true
                },
                valid: true,
                touched: false
            },
            smokingAllowed: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Smoking Allowed'
                },
                value: this.props.data.smokingAllowed,
                validation: {
                    required: true,
                    minLength: 2,
                    maxLength: 3,
                },
                valid: true,
                touched: false
            },
            petsAllowed: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Pets Allowed'
                },
                value: this.props.data.petsAllowed,
                validation: {
                    required: true,
                    minLength: 2,
                    maxLength: 3,
                },
                valid: true,
                touched: false
            },
            luggageAllowed: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Luggage Allowed'
                },
                value: this.props.data.luggageAllowed,
                validation: {
                    required: true,
                    minLength: 2,
                    maxLength: 3,
                },
                valid: true,
                touched: false
            }
        },
        formIsValid: true,
        startLocation: 0,
        endLocation: 0,
        travelDistance: 0,
        tripDuration: 0,
        modelId: 0

    };

    async getCoordinates() {
        await axios.get("http://dev.virtualearth.net/REST/v1/Locations/" + this.state.createForm.origin.value + "?key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
            .then(response => {
                console.log(this.state.createForm.origin.value);

                this.setState({
                    startLocation: response.data.resourceSets[0].resources[0].point.coordinates
                });
            });

        await axios.get("http://dev.virtualearth.net/REST/v1/Locations/" + this.state.createForm.destination.value + "?key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
            .then(response => {
                this.setState({
                    endLocation: response.data.resourceSets[0].resources[0].point.coordinates
                });
            });

        await axios.get("https://dev.virtualearth.net/REST/v1/Routes/DistanceMatrix?origins=" + this.state.startLocation + "&destinations=" + this.state.endLocation + "&travelMode=driving&key=AicLZ6MUrcgX7d1YzI03aJetdI5O9YyESuynCP_jJyhoFFRcxIrUBaTa8UsdqqG4")
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
        formData['modelId'] = this.props.data.modelId;
        formData["tripDuration"] = this.state.tripDuration;
        formData["departureTime"] = DateTimeFormat(this.state.createForm.departureTime.value, "yyyy-mm-dd HH:MM");

        this.props.onUpdateTrip(formData, this.props.token);
        this.setState({
            startDate: new Date(),
            changedDate: new Date(),
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
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Smoking Allowed'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 2,
                        maxLength: 3,
                    },
                    valid: false,
                    touched: false
                },
                petsAllowed: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Pets Allowed'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 2,
                        maxLength: 3,
                    },
                    valid: false,
                    touched: false
                },
                luggageAllowed: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Luggage Allowed'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 2,
                        maxLength: 3,
                    },
                    valid: false,
                    touched: false
                }
            },
            formIsValid: false,
            startLocation: 0,
            endLocation: 0,
            travelDistance: 0,
            tripDuration: 0,
            modelId: 0

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

    inputDateChangedHandler = (date, name) => {
        this.setState({
            startDate: date
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
                        dateChange={(date) => this.inputDateChangedHandler(date, formElement.id)}
                        changed={(event) => this.inputChangedHandler(event, formElement.id)}/>
                ))}
                <Button btnType="Success" disabled={!this.state.formIsValid}>UPDATE</Button>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        return (
            <div >
                Update your Trip Data
                {form}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onUpdateTrip: (create, token) => dispatch(actions.updateTrip(create, token)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(UpdateTrip, axios));