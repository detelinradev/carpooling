import React from 'react';

import './Input.css';
import DatePicker from "react-datepicker";

import "react-datepicker/dist/react-datepicker.css";

const input = ( props ) => {



    let inputElement = null;
    const inputClasses = ['InputElement'];

    if (props.invalid && props.shouldValidate && props.touched) {
        inputClasses.push('Invalid');
    }

    switch ( props.elementType ) {
        case ( 'input' ):
            inputElement = <input
                className={inputClasses.join(' ')}
                {...props.elementConfig}
                value={props.value}
                onChange={props.changed} />;
            break;
        case ( 'textarea' ):
            inputElement = <textarea
                className={inputClasses.join(' ')}
                {...props.elementConfig}
                value={props.value}
                onChange={props.changed} />;
            break;
        case ( 'select' ):
            inputElement = (
                <DatePicker 
                    placeholderText="Click to select a date"
                    selected={props.startDate}
                    onChange={props.dateChange}
                    // value={props.value}
                    showTimeSelect
                    timeFormat="HH:mm"
                    timeIntervals={15}
                    dateFormat="MMMM d, yyyy h:mm aa"
                    timeCaption="time"
                />

            );
            break;
        // case ( 'select' ):
        //     inputElement = (
        //         <Calendar
        //             newDate={props.value}/>
        //
        //     );
        //     break;
        default:
            inputElement = <input
                className={inputClasses.join(' ')}
                {...props.elementConfig}
                value={props.value}
                onChange={props.changed} />;
    }

    return (
        <div className="Input">
            <label className="Label">{props.label}</label>
            <form style={{textAlign: 'left'}}>
            {props.name}&nbsp;&nbsp;{inputElement}
            </form>
        </div>
    );

};

export default input;