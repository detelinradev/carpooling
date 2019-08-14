import React, {Component} from 'react';
import {connect} from 'react-redux';
import {Redirect} from 'react-router-dom';

import Input from '../../../components/UI/Input/Input';
import Button from '../../../components/UI/Button/Button';
import Spinner from '../../../components/UI/Spinner/Spinner';
import './UpdateUser.css';
import * as actions from '../../../store/actions';
import {updateObject, checkValidity} from '../../../shared/utility';
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";
import DateTimeFormat from "dateformat";

class UpdateUser extends Component {
    state = {
        controls: {
            username: {
                elementType: 'input',
                elementConfig: {
                    type: 'username',
                    placeholder: 'Username'
                },
                value: this.props.user.username,
                validation: {
                    required: true,
                },
                valid: true,
                touched: false
            },
            firstName: {
                elementType: 'input',
                elementConfig: {
                    type: 'firstName',
                    placeholder: 'First name'
                },
                value: this.props.user.firstName,
                validation: {
                    required: true,
                },
                valid: true,
                touched: false
            },
            lastName: {
                elementType: 'input',
                elementConfig: {
                    type: 'lastName',
                    placeholder: 'Last name'
                },
                value: this.props.user.lastName,
                validation: {
                    required: true,
                },
                valid: true,
                touched: false
            },
            role: {
                elementType: 'input',
                elementConfig: {
                    type: 'role',
                    placeholder: 'User role'
                },
                value: this.props.user.role,
                validation: {
                    required: true,
                },
                valid: true,
                touched: false
            },
            email: {
                elementType: 'input',
                elementConfig: {
                    type: 'email',
                    placeholder: 'Email'
                },
                value: this.props.user.email,
                validation: {
                    required: true,
                    isEmail: true
                },
                valid: true,
                touched: false
            },
            phone: {
                elementType: 'input',
                elementConfig: {
                    type: 'phone',
                    placeholder: 'Phone'
                },
                value: this.props.user.phone,
                validation: {
                    required: true,
                    isNumeric: true,
                },
                valid: true,
                touched: false
            }
        },
        formIsValid: true
    };

    createHandler = async (event) => {
        event.preventDefault();
        const formData = {};
        for (let formElementIdentifier in this.state.controls) {
            formData[formElementIdentifier] = this.state.controls[formElementIdentifier].value;
        }
        formData['modelId'] = this.props.user.modelId;
        formData['ratingAsDriver'] = 0;
        formData['ratingAsPassenger'] = 0;

        this.props.onUpdateUser(formData, this.props.token);
        this.setState({
            controls: {
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
                password: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'password',
                        placeholder: 'Password'
                    },
                    value: '',
                    validation: {
                        required: true,
                        minLength: 6
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
                role: {
                    elementType: 'input',
                    elementConfig: {
                        type: 'role',
                        placeholder: 'User role'
                    },
                    value: '',
                    validation: {
                        required: true,
                    },
                    valid: true,
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
                    },
                    valid: false,
                    touched: false
                }
            },
            formIsValid: false
        })

    };


    inputChangedHandler = (event, inputIdentifier) => {

        const updatedFormElement = updateObject(this.state.controls[inputIdentifier], {
            value: event.target.value,
            valid: checkValidity(event.target.value, this.state.controls[inputIdentifier].validation),
            touched: true
        });
        const updatedCreateForm = updateObject(this.state.controls, {
            [inputIdentifier]: updatedFormElement
        });

        let formIsValid = true;
        for (let inputIdentifier in updatedCreateForm) {
            formIsValid = updatedCreateForm[inputIdentifier].valid && formIsValid;
        }
        this.setState({controls: updatedCreateForm, formIsValid: formIsValid});
    };


    render() {
        const formElementsArray = [];
        for (let key in this.state.controls) {
            formElementsArray.push({
                id: key,
                config: this.state.controls[key]
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
                <Button btnType="Success" disabled={!this.state.formIsValid}>UPDATE</Button>
            </form>
        );
        if (this.props.loading) {
            form = <Spinner/>;
        }
        let errorMessage = null;

        if (this.props.error) {
            errorMessage = (
                <p>{this.props.error.message}</p>
            );
        }

        return (
            <div>
                Update User Data
                {form}
                {errorMessage}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.auth.loading,
        error: state.auth.error,
        user: state.user.user,
        token: state.auth.token,
        userUpdate: state.user.userUpdated
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onUpdateUser: (userData, token) => dispatch(actions.updateUser(userData, token)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(UpdateUser, axios));