import React, { Component } from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router-dom';

import Input from '../../components/UI/Input/Input';
import Button from '../../components/UI/Button/Button';
import Spinner from '../../components/UI/Spinner/Spinner';
import './Auth.css';
import * as actions from '../../store/actions';
import { updateObject, checkValidity } from '../../shared/utility';

class Auth extends Component {
    state = {
        controlsLogin: {
            username: {
                elementType: 'input',
                elementConfig: {
                    type: 'username',
                    placeholder: 'Username'
                },
                value: '',
                validation: {
                    required: true,
                    // isEmail: true
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
            }
        },
        controlsSignUp: {
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
            email: {
                elementType: 'input',
                elementConfig: {
                    type: 'email',
                    placeholder: 'Email'
                },
                value: '',
                validation: {
                    required: true,
                    // isEmail: true
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

                    // isEmail: true
                },
                valid: false,
                touched: false
            }
        },
        isSignup: false
    };

    componentDidMount () {
        if (
            this.props.authRedirectPath !== '/' ) {
            this.props.onSetAuthRedirectPath();
        }
    }

    inputChangedHandlerLogin = ( event, controlName ) => {
        const updatedControls = updateObject( this.state.controlsLogin, {
            [controlName]: updateObject( this.state.controlsLogin[controlName], {
                value: event.target.value,
                valid: checkValidity( event.target.value, this.state.controlsLogin[controlName].validation ),
                touched: true
            } )
        } );
        this.setState( { controlsLogin: updatedControls } );
    };

    inputChangedHandlerSignUp = ( event, controlName ) => {
        const updatedControls = updateObject( this.state.controlsSignUp, {
            [controlName]: updateObject( this.state.controlsSignUp[controlName], {
                value: event.target.value,
                valid: checkValidity( event.target.value, this.state.controlsSignUp[controlName].validation ),
                touched: true
            } )
        } );
        this.setState( { controlsSignUp: updatedControls } );
    };

    submitHandler = ( event ) => {
        event.preventDefault();
        if(this.state.isSignup) {
            this.props.onAuth(this.state.controlsSignUp.username.value,
                this.state.controlsSignUp.password.value,
                this.state.isSignup,
                this.state.controlsSignUp.firstName.value,
                this.state.controlsSignUp.lastName.value,
                this.state.controlsSignUp.email.value,
                this.state.controlsSignUp.phone.value);
        }else {
            this.props.onAuth(this.state.controls.email.value, this.state.controls.password.value, this.state.isSignup);
        }
    };
    // submitHandlerSignUp = ( event ) => {
    //     event.preventDefault();
    //     this.props.onAuth( this.state.controlsSignUp.username.value, this.state.controlsSignUp.password.value, this.state.isSignup,this.state.controlsSignUp.firstName );
    // };

    switchAuthModeHandler = () => {
        this.setState( prevState => {
            return { isSignup: !prevState.isSignup };
        } );
    };

    render () {
        const formElementsArrayLogin = [];
        for ( let key in this.state.controlsLogin ) {
            formElementsArrayLogin.push( {
                id: key,
                config: this.state.controlsLogin[key]
            } );
        }

        const formElementsArraySignUp = [];
        for ( let key in this.state.controlsSignUp ) {
            formElementsArraySignUp.push( {
                id: key,
                config: this.state.controlsSignUp[key]
            } );
        }

        let formLogin = formElementsArrayLogin.map( formElement => (
            <Input
                key={formElement.id}
                elementType={formElement.config.elementType}
                elementConfig={formElement.config.elementConfig}
                value={formElement.config.value}
                invalid={!formElement.config.valid}
                shouldValidate={formElement.config.validation}
                touched={formElement.config.touched}
                changed={( event ) => this.inputChangedHandlerLogin( event, formElement.id )} />
        ) );

        let formSignUp = formElementsArraySignUp.map( formElement => (
            <Input
                key={formElement.id}
                elementType={formElement.config.elementType}
                elementConfig={formElement.config.elementConfig}
                value={formElement.config.value}
                invalid={!formElement.config.valid}
                shouldValidate={formElement.config.validation}
                touched={formElement.config.touched}
                changed={( event ) => this.inputChangedHandlerSignUp( event, formElement.id )} />
        ) );

        if ( this.props.loading ) {
            formLogin = <Spinner />;
            formSignUp=<Spinner/>;
        }

        let errorMessage = null;

        if ( this.props.error ) {
            errorMessage = (
                <p>{this.props.error.message}</p>
            );
        }

        let authRedirect = null;
        if ( this.props.isAuthenticated ) {
            authRedirect = <Redirect to={this.props.authRedirectPath} />
        }
        let form =formLogin;
        if(this.state.isSignup){
            form = formSignUp
        }

        return (
            <div className="Auth">
                {authRedirect}
                {errorMessage}
                <form onSubmit={this.submitHandler}>
                    {form}
                    <Button btnType="Success">SUBMIT</Button>
                </form>
                <Button
                    clicked={this.switchAuthModeHandler}
                    btnType="Danger">SWITCH TO {this.state.isSignup ? 'SIGNIN' : 'SIGNUP'}</Button>
            </div>
        );
    }
}

const mapStateToProps = state => {
    return {
        loading: state.auth.loading,
        error: state.auth.error,
        isAuthenticated: state.auth.token !== null,
        authRedirectPath: state.auth.authRedirectPath
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onAuth: ( username, password, isSignup,firstName, lastName, email, phone ) => dispatch( actions.auth( username, password, isSignup,firstName, lastName, email, phone ) ),
        onSetAuthRedirectPath: () => dispatch( actions.setAuthRedirectPath( '/' ) )
    };
};

export default connect( mapStateToProps, mapDispatchToProps )( Auth );