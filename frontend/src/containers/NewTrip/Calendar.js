import {SingleDatePicker} from "react-dates";
import React, {PureComponent} from "react";

import 'react-dates/initialize';
import 'react-dates/lib/css/_datepicker.css';

class Calendar extends PureComponent {

    state = {
        date: null,
        focused: false,
        id: ''
    };

    render() {
        return (
            <SingleDatePicker
                date={this.state.date} // momentPropTypes.momentObj or null
                onDateChange={date => this.setState({date})} // PropTypes.func.isRequired
                focused={this.state.focused} // PropTypes.bool
                onFocusChange={({focused}) => this.setState({focused})} // PropTypes.func.isRequired
                id="your_unique_id" // PropTypes.string.isRequired,
            />

        )
    }
}

export default Calendar;