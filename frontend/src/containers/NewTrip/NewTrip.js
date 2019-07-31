import React, {Component} from 'react';
import {connect} from 'react-redux';

import Button from '../../components/UI/Button/Button';
import Spinner from '../../components/UI/Spinner/Spinner';
import classes from './NewTrip.css';
import axios from '../../axios-baseUrl';
import Input from '../../components/UI/Input/Input';
import withErrorHandler from '../../hoc/withErrorHandler/withErrorHandler'
import * as actions from '../../store/actions/index';
import {updateObject, checkValidity} from '../../shared/utility';

class NewTrip extends Component {
    state = {
        createForm: {
            origin: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'origin'
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
                    placeholder: 'destination'
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
                    placeholder: 'DepartureTime'
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
                    placeholder: 'Available Places'
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
                    type: 'text',
                    placeholder: 'Trip Duration'
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
                    placeholder: 'Cost Per Passenger'
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
                    placeholder: 'message'
                },
                value: '',
                validation: {
                    required: true
                },
                valid: false,
                touched: false
            }
        },
        formIsValid: false
    };

    createHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.createForm) {
            formData[formElementIdentifier] = this.state.createForm[formElementIdentifier].value;
        }
        const create = {
            //ingredients: this.props.ings,
           // price: this.props.price,
            tripData: formData,
           // userId: this.props.userId
        };

        this.props.onCreateTrip(create, this.props.token);

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
            <div className={classes.NewTrip}>
                <h4>Enter your Trip Data</h4>
                {form}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
       // ings: state.burgerBuilder.ingredients,
       // price: state.burgerBuilder.totalPrice,
        loading: state.trip.loading,
        token: state.auth.token,
       // userId: state.auth.userId
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onCreateTrip: (create, token) => dispatch(actions.createTrip(create, token))
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(NewTrip, axios));