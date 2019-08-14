import React, {Component} from 'react';
import {connect} from 'react-redux';
import './Admin.css';
import axios from '../../axios-baseUrl';
import * as actions from '../../store/actions/index';
import Spinner from "../../components/UI/Spinner/Spinner";
import withErrorHandler from "../../hoc/withErrorHandler/withErrorHandler";
import {checkValidity, updateObject} from "../../shared/utility";
import Input from "../../components/UI/Input/Input";
import Button from "../../components/UI/Button/Button";
import User from "../../components/UserComponents/User";


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

    componentWillUnmount() {
       this.props.onDismountAdmin()
    }

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
        this.props.onFetchUsers(this.props.token, stringData);
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

    showFullUser = (user) => {
        this.props.onShowFullUser(user);
        this.props.history.push('/fullUser');
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
                <Button btnType="Success">SEARCH</Button>
            </form>
        );

        let users = <Spinner/>;
        if (!this.props.loading) {
            users = this.props.users.map(user => (
                <User
                    key={user.id}
                    data={user}
                    showFullUser={this.showFullUser}
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
                {users}
            </div>)
    }
}

const mapStateToProps = state => {
    return {
        users: state.user.users,
        loading: state.trip.loading,
        token: state.auth.token,
    }
};
const mapDispatchToProps = dispatch => {
    return {
        onFetchUsers: (token, formData) => dispatch(actions.fetchUsers(token, formData)),
        onShowFullUser: (user) => dispatch(actions.showFullUser(user)),
        onDismountAdmin: () => dispatch(actions.dismountAdmin()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(withErrorHandler(Admin, axios));