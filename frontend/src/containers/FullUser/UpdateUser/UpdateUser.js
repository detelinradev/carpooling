import React, {Component} from 'react';
import {connect} from 'react-redux';
import Input from '../../../components/UI/Input/Input';
import Button from '../../../components/UI/Button/Button';
import Spinner from '../../../components/UI/Spinner/Spinner';
import './UpdateUser.css';
import * as actions from '../../../store/actions';
import {updateObject, checkValidity} from '../../../shared/utility';
import withErrorHandler from "../../../hoc/withErrorHandler/withErrorHandler";
import axios from "../../../axios-baseUrl";

class UpdateUser extends Component {
    state = {
        controls: {
            role: {
                elementType: 'select',
                elementConfig: {
                    options: [
                        {value: '', displayValue: 'choose option'},
                        {value: 'ADMIN', displayValue: 'ADMIN'},
                        {value: 'USER', displayValue: 'USER'}
                    ]
                },
                value: '',
                validation: {},
                valid: true
            },
            email: {
                elementType: 'input',
                elementConfig: {
                    type: 'email',
                    placeholder: 'Email'
                },
                value: this.props.data.email,
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
                value: this.props.data.phone,
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
        formData['modelId'] = this.props.data.modelId;

        this.props.onUpdateUser(formData, this.props.token);

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

        return (
            <div>
                Update User Data
                {form}
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.auth.loading,
        token: state.auth.token,
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onUpdateUser: (userData, token) => dispatch(actions.updateUser(userData, token)),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(UpdateUser, axios));