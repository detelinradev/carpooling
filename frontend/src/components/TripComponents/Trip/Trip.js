import React from 'react';
import './Trip.css';
import Button from "@material-ui/core/Button";
import Auxiliary from "../../../hoc/Auxiliary/Auxiliary";
import {FaUserEdit} from "react-icons/fa";

 const trip = (props) => {
    let image=null;
   const getAvatarResponse = async () => {
      await fetch('http://localhost:8080/users/avatar/' + props.data.driver.modelId,
           {headers: {"Authorization": props.token}})
           .then(response => response.blob())
           .then(blob =>  URL.createObjectURL(blob))
           .then(response => image = response);
   };
    //
    // const fetchImage = async () => {
    //      const token = sessionStorage.getItem("jwt");
    //      await fetch(`http://localhost:8080/image/downloadImage`,
    //          { headers: {Authorization: token}}
    //      )
    //      // .then(validateResponse)
    //          .then(response => response.blob())
    //          .then(blob => {
    //              this.setState({ src: URL.createObjectURL(blob) })
    //          })
    //
    //  };

    return (

        <Auxiliary>
            <div className=" Post" >
                <div className="proba Trip additional-details hed">{props.data.origin} -> {props.data.destination}</div>
                <div className="Trip additional-details  cardcont  meta-data-container">
                    <p className="image">
                        <img id="postertest" className='poster' style={{width: 128}}
                             src={getAvatarResponse()} alt={''}/>
                        <p className="meta-data">{props.data.driver.firstName} {props.data.driver.lastName}</p></p>
                    <div className="edit">
                        <Button onClick={() => props.showFullTrip(props.data)}><h3
                            className="header">DETAILS <FaUserEdit/></h3>
                        </Button>
                    </div>
                    <p className="row-xs-6 info"> Departure Time<p className="meta-data">{props.data.departureTime}</p>
                    </p>
                    <p className="row-xs-6 info">Available Seats<p
                        className="meta-data">{props.data.availablePlaces}</p>
                    </p>
                    <p className="row-xs-6 info">Price<p className="meta-data">{props.data.costPerPassenger} leva</p>
                    </p>
                    <p className="row-xs-6 info">Car<p className="meta-data">{props.data.car.brand} {props.data.car.model}</p></p>
                    <p className="row-xs-6 info">Status<p className="meta-data">{props.data.tripStatus}</p></p>
                    <p className="row-xs-3 info">Message<p className="meta-data">{props.data.message}</p></p>
                    <p className="row-xs-6 info">Smoking allowed<p className="meta-data">{props.car.smokingAllowed}</p>
                    </p>
                    <p className="row-xs-6 info">Pets allowed<p className="meta-data">{props.car.petsAllowed}</p></p>
                    <p className="row-xs-6 info">Luggage allowed<p className="meta-data">{props.car.luggageAllowed}</p>
                    </p>
                    <p className="row-xs-6 info">Air-conditioned<p className="meta-data">{props.car.airConditioned}</p>
                    </p>

                </div>

            </div>
        </Auxiliary>);


};
export default trip;
