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


class NewTrip extends Component {
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

        const formData = {};
        for (let formElementIdentifier in this.state.createForm) {
            formData[formElementIdentifier] = this.state.createForm[formElementIdentifier].value;
        }

        this.props.onCreateTrip(formData, this.props.token);

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
                <Button btnType="Success" disabled={!this.state.formIsValid}>CREATE</Button>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        return (
            <div>
            <div className="NewTrip">
                <h4 className="header">Enter your Trip Data</h4>
                {form}
            </div>
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
        onCreateTrip: (create, token) => dispatch(actions.createTrip(create, token))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(NewTrip, axios));