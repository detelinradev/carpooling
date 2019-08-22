import React, {Component} from 'react';
import {connect} from 'react-redux';

import Button from '../../components/UI/Button/Button';
import Spinner from '../../components/UI/Spinner/Spinner';
import axios from '../../axios-baseUrl';
import Input from '../../components/UI/Input/Input';
import withErrorHandler from '../../hoc/withErrorHandler/withErrorHandler'
import * as actions from '../../store/actions/index';
import {updateObject, checkValidity} from '../../shared/utility';
import 'react-dates/initialize';
import './NewCar.css';
import Modal from "../../components/UI/Modal/Modal";


class NewCar extends Component {
    state = {
        showModal: false,
        createForm: {
            brand: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Brand'
                },
                value: '',
                validation: {
                    required: false,
                    minLength: 2,
                },
                valid: false,
                touched: false
            },
            model: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Model'
                },
                value: '',
                validation: {
                    required: true,
                    minLength: 2,
                },
                valid: false,
                touched: false
            },
            color: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Color'
                },
                value: '',
                validation: {
                    required: true,
                    minLength: 2
                },
                valid: false,
                touched: false
            },
            firstRegistration: {
                elementType: 'input',
                elementConfig: {
                    type: 'number',
                    placeholder: 'First registration'
                },
                value: '',
                validation: {
                    required: true,
                    isNumeric: true,
                    minValue: 1950,
                    maxValue: 2019
                },
                valid: false,
                touched: false
            },
            airConditioned: {
                elementType: 'input',
                elementConfig: {
                    type: 'text',
                    placeholder: 'Air conditioned'
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

    };

    createHandler = (event) => {
        event.preventDefault();

        const formData = {};
        for (let formElementIdentifier in this.state.createForm) {
            formData[formElementIdentifier] = this.state.createForm[formElementIdentifier].value;
        }

        this.props.onCreateCar(formData, this.props.token);
        this.setState({
            createForm: {
                brand: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Brand'
                    },
                    value: '',
                    validation: {
                        required: false,
                        minLength: 2,
                    },
                    valid: false,
                    touched: false
                },
                model: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Model'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 2,
                    },
                    valid: false,
                    touched: false
                },
                color: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Color'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 2
                    },
                    valid: false,
                    touched: false
                },
                firstRegistration: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'number',
                        placeholder: 'First registration'
                    },
                    value: '',
                    validation: {
                        required: true,
                        isNumeric: true,
                        minValue: 1950,
                        maxValue: 2019
                    },
                    valid: false,
                    touched: false
                },
                airConditioned: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'text',
                        placeholder: 'Air conditioned'
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



    toggleModal(){
        this.setState({
            showModal: !this.state.showModal
        })
    }


    editCloseHandler() {
        this.setState({
            showModal: !this.state.showModal
        });
    }
    render() {
        const formElementsArray = [];
        for (let key in this.state.createForm) {
            formElementsArray.push({
                id: key,
                config: this.state.createForm[key]
            });
        }
        let form = (
            <form style={{textAlign: 'left'}} onSubmit={this.createHandler}>
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
                <div style={{textAlign: 'center'}}>
                <Button btnType="Success" disabled={!this.state.formIsValid} onSubmit={() =>this.toggleModal()}>CREATE</Button>
                </div>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        return (
            <div>
                <button className="Car" onClick={() => this.toggleModal()}><h1>+CREATE CAR</h1></button>
                <Modal style={{width: 600}} show={this.state.showModal} modalClosed={() => this.editCloseHandler()}>
                Enter your Car Data
                    {form}
                </Modal>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.trip.loading,
        token: state.auth.token,
        carCreated:state.car.carCreated,
    }
};

const mapDispatchToProps = dispatch => {
    return {
        onCreateCar: (create, token) => dispatch(actions.createCar(create, token))    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(NewCar, axios));