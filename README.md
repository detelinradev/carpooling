# Carpooling app
It is a final project for Telerik Academy Alpha track 'February 2019, made by me in collaboration with [Anton Madzharov](https://github.com/antonmadzharov)


### **Web-based application giving opportunities for the user to share car journeys**
 - It has implemented functionality for creating a shared journey and joining already created journey
 - The project allows two roles which a user can have: admin and user
 - User can create, edit or join the trip and admin could edit or delete the user and delete trip
 - Technologies - ReactJs, REST, Spring Boot, JPA, MariaDB

### **Simple structure of the project :**
![seYw4vJZ6JGNOUEI3OZCRTw (1)](https://user-images.githubusercontent.com/39421427/64018341-dc85b980-cb34-11e9-8fd6-3a355521f27e.png)

### **We have used for front-end ReactJs**
  - With the help of Redux, we greatly improved the managing of the state
  - Our components are asynchronous, which means a component being load only if it is called
  - Routing is implemented, and therefore we are using links when moving between components instead of reloading it
  - We added global error handler for responses from the server, yet there are local handlers too, for more precise response messages
  - The origin and the destination for the trip in the app are being sent to network-based API, which delivers the duration and the         distance of the trip
  - Validation for the user input is achieved with as many predefined options as possible, as drop-downs, calendars ect., and use of       validation rules like numeric type and regex check for input fields
  
 ### **Back-end is written in Java 8, it is Spring Boot Gradle based application**
  - Thus it has a layered structure, in 3 main layers, Rest Controllers, Services and JPA Repositories, and we are using Dependency         Injection to handle dependencies between modules
  - We are using JWT Security, with identification token bearing the role of the user
  - We chose Global exception handler and optional return types over try-catch blocks 
  - The repositories are JPA, what delivers all the database connection functionality in just interfaces, with help of JPQL queries
  - We used Lombok for cutting out the boilerplate code
  - We chose MapStruct as more fast and powerful dto mapper tool than other similar tools 
  - We made validation of the user input with the help of Java Validation API and use of javax.validation for bean validation

**Documentation of the project can be found [here](https://documenter.getpostman.com/view/7601087/SVYwLwZa)**

## **Application gallery**

**We provide a gallery of screenshots for the possible customer interactions with the application**

 This is how the application looks for the unregistered user. Here we have just `Home` component and `Login` component available
 
![2019-08-30 (2)](https://user-images.githubusercontent.com/39421427/64017472-46e92a80-cb32-11e9-81fc-d868cd52bcf7.png)


 That is the `Sign-in` module. We need to enter username and password to authenticate successfully
 
![2019-08-30 (3)](https://user-images.githubusercontent.com/39421427/64017473-4781c100-cb32-11e9-93c3-df338de5ffa2.png)


Here is the `Sign-up` module for new users. We need to fill all required fields to make an account in the application.
 - username has to be unique for the application, min length 2 symbols, max length 20 symbols
 - password with small and capital letters, digits and special symbols, at last, 8 symbols long
 - first and last name should contain only letters, min length 1 symbol, max length 20 symbols
 - the email should follow xxxxx@yyyyy.zzzzz pattern
 - phone number should be 10 symbols long, consists only of numbers
 
![2019-08-30 (4)](https://user-images.githubusercontent.com/39421427/64017474-4781c100-cb32-11e9-937d-33c9a57f44ff.png)


 This is the `Home` window for the authenticated user. Here he could see a leaderboard with the TOP 10 drivers and TOP 10 passengers based on the rating they receive after each trip. The leaderboard is visible in the whole application under the other components. As well one can see all possible links in the application, listed on the right side of the navbar on the top of the window
 
![2019-08-30 (5)](https://user-images.githubusercontent.com/39421427/64017475-4781c100-cb32-11e9-8b1e-a3d72c232ad6.png)


That is the `Profile` page, where the user could see all information about himself as well as edited it. He has options to edit his phone, email and password and add an avatar. If he wants to create a trip, it is mandatory to create a car as well.

![2019-08-30 (6)](https://user-images.githubusercontent.com/39421427/64017476-4781c100-cb32-11e9-9bb8-2416136d47b5.png)


Here the user did upload a picture and receives a message for the successful operation

![2019-08-30 (7)](https://user-images.githubusercontent.com/39421427/64017477-481a5780-cb32-11e9-9f4d-f7f6d8798ff9.png)


When you press the edit button modal with possible actions appears

![2019-08-30 (8)](https://user-images.githubusercontent.com/39421427/64017478-481a5780-cb32-11e9-8233-11e9d251348c.png)


Here data is successfully changed and there is a message for it. Changes are asynchronous and the whole page is not refreshed

![2019-08-30 (9)](https://user-images.githubusercontent.com/39421427/64017480-481a5780-cb32-11e9-9d30-ce5ce5ec6b27.png)


That is the form for creating a car. You have to enter the brand, model and colour of your car and year of registration between 1950 and the current year. Air-conditioned should be 'yes' or 'no'

![2019-08-30 (10)](https://user-images.githubusercontent.com/39421427/64017481-481a5780-cb32-11e9-8576-10afd7b29be9.png)


When a car has successfully created, another component appears under the main one with information about your car

![2019-08-30 (11)](https://user-images.githubusercontent.com/39421427/64017482-48b2ee00-cb32-11e9-90f9-6a379bb994de.png)


If the user presses the upload button for uploading an avatar by mistake without any fail selected, there is a warning message for it 

![2019-08-30 (12)](https://user-images.githubusercontent.com/39421427/64017483-48b2ee00-cb32-11e9-8d1e-15a2e4832b0b.png)


Here the picture is successfully uploaded

![2019-08-30 (13)](https://user-images.githubusercontent.com/39421427/64017418-3fc21c80-cb32-11e9-8c64-102c7e8da130.png)


Now that the user has a car, he can create a trip. Origin and destination should be entered, information is sent to Bing API for acquiring distance and time for finishing the trip

![2019-08-30 (14)](https://user-images.githubusercontent.com/39421427/64017419-3fc21c80-cb32-11e9-8a59-2be53783df54.png)


There is a calendar for the departure time to make sure user input is in the format we expected

![2019-08-30 (15)](https://user-images.githubusercontent.com/39421427/64017421-405ab300-cb32-11e9-8236-388681aa82b9.png)


Most of the rest options are drop-down lists which makes it easier and more secure to fill the form. Button 'create' becomes active when all fields have values and they passed the validation provided

![2019-08-30 (16)](https://user-images.githubusercontent.com/39421427/64017422-405ab300-cb32-11e9-98b2-0f5645e414b3.png)


After pressing create button user is redirected to `My Trips` component, where she could see the trip that she just created. The role of the user in the trip appears on the top border in the centre as `driver` or `passenger`

![2019-08-30 (18)](https://user-images.githubusercontent.com/39421427/64017423-405ab300-cb32-11e9-83ed-876dd032b665.png)


You could see full info for the trip by pressing the `Details` button. Now you have options to edit trip and add a comment with additional information for users willing to join your trip. The option for adding a comment is only available for driver and passengers that made a request to join the trip, or that have been already approved

![2019-08-30 (19)](https://user-images.githubusercontent.com/39421427/64017424-405ab300-cb32-11e9-86df-99a5c8be75bd.png)


Here comment is created and become visible in the comments area with your name and picture over it

![2019-08-30 (20)](https://user-images.githubusercontent.com/39421427/64017425-40f34980-cb32-11e9-974c-68dfa7bd2097.png)


You could change all the details for the trip. When button `Update trip` is pressed, a form with trip details pops up. By simply pressing `update` button you can edit the trip.

![2019-08-30 (21)](https://user-images.githubusercontent.com/39421427/64017426-40f34980-cb32-11e9-8d94-71b88b28bb40.png)


Here the trip is updated with the information provided

![2019-08-30 (22)](https://user-images.githubusercontent.com/39421427/64017427-40f34980-cb32-11e9-8b0b-a7b1ddcd4d25.png)


If you chose a value that not follow the rules, for example, `available seats = 30`, you will receive a message that operation is not successful

![2019-08-30 (23)](https://user-images.githubusercontent.com/39421427/64017428-40f34980-cb32-11e9-9410-a3cb74ac1a82.png)


Now once the trip is created we need to sign in with a different user and join the trip.
That is how the `Search trips` form looks like. All fields are optional, if you don't choose any, it will load all the trips available.
Almost all the fields are predefined, so we could rely on the user's input validity

![2019-08-30 (24)](https://user-images.githubusercontent.com/39421427/64017429-418be000-cb32-11e9-8434-10956276e313.png)


Now our trip is available and visible for the users to join

![2019-08-30 (25)](https://user-images.githubusercontent.com/39421427/64017430-418be000-cb32-11e9-82a0-cf8227d3bf53.png)


By pressing the `Details` button we redirect to a component that shows all the info for the trip as well as button `Join trip`

![2019-08-30 (26)](https://user-images.githubusercontent.com/39421427/64017432-418be000-cb32-11e9-82fa-2045cee308dd.png)


When the user wants to join the trip, and he presses the `Join trip` button, the button changes to `Request sent` and new component shows up with user name and picture, his rating as passenger and status of the request, what initially is `pending`

![2019-08-30 (27)](https://user-images.githubusercontent.com/39421427/64017433-42247680-cb32-11e9-95f3-e8e70f842fad.png)


Now one can find it in his `My trips` page where his role on the top of the border will be `passenger`

![2019-08-30 (28)](https://user-images.githubusercontent.com/39421427/64017434-42247680-cb32-11e9-80e2-3bf66818957a.png)


By clicking `details` button user will see the detailed view of the trip with all the information and button `leave trip` what allows him to cancel his participation in the trip. Now he can add comments as well.

![2019-08-30 (29)](https://user-images.githubusercontent.com/39421427/64063937-fe01a680-cc03-11e9-83c2-2cf5d1312739.png)


Here we swap the users again and log in with the driver of the trip. In her `My trip` page she can see the trip

![2019-08-30 (31)](https://user-images.githubusercontent.com/39421427/64017436-42247680-cb32-11e9-848b-a85b0a88371d.png)


When she opens the full view she sees the pending passenger request and has an option to accept it, reject it or marked it as absent, what is possible only if the request has been accepted and the trip is not `ONGOING` or `DONE`.

![2019-08-30 (32)](https://user-images.githubusercontent.com/39421427/64017437-42bd0d00-cb32-11e9-8178-1dc247476f0f.png)


Here just for the sake of brevity, we will update the available seat to 1, so we can fill all the seats with just that example

![2019-08-30 (35)](https://user-images.githubusercontent.com/39421427/64017438-42bd0d00-cb32-11e9-85ca-b2e233cff50c.png)


Now we could choose between options for passenger status, if we choose `ACCEPTED` that will automatically change the trip seats and the server will check if they are 0 and will change trip status to `BOOKED`, if we mark `REJECTED` that will remove the passenger from the trip and will be no longer visible in your trip or in the main search trips page, but will be still visible in the passenger's my trip page with status `Driver denied`

![2019-08-30 (36)](https://user-images.githubusercontent.com/39421427/64017439-42bd0d00-cb32-11e9-872b-be557da6aee8.png)


Here the passenger is accepted and because we changed available trip seats to 1, the trip status becomes `BOOKED` automatically. All passengers that left with passenger status `PENDING` will be automatically marked as `REJECTED`. If we decide to reject the passenger thereafter the seat will be recalculated to +1 and trip status changed appropriately

![2019-08-30 (37)](https://user-images.githubusercontent.com/39421427/64017440-42bd0d00-cb32-11e9-9f07-ddb6f7df50d0.png)


Now the driver could change the trip status to `ONGOING`, `DONE` or `CANCELED`

![2019-08-30 (38)](https://user-images.githubusercontent.com/39421427/64017441-4355a380-cb32-11e9-88c9-46b0e432496c.png)


It should be changed first to `ONGOING`. Now we can see `Update trip` button disappear as it is not expected to change something when a trip is already started

![2019-08-30 (39)](https://user-images.githubusercontent.com/39421427/64017442-4355a380-cb32-11e9-84ad-6cdd0f8efc88.png)


Only then `DONE` is available.  `Change trip status` button is not there anymore, as nothing more can happen with a trip when it is done. And now, fields with option to rate passenger and give feedback to passenger appears.

![2019-08-30 (40)](https://user-images.githubusercontent.com/39421427/64017443-4355a380-cb32-11e9-8c41-7aca708bc57d.png)


Rate value should be between 1 and 5

![2019-08-30 (41)](https://user-images.githubusercontent.com/39421427/64017444-4355a380-cb32-11e9-9e4d-985a0b047193.png)


Feedback is just String and is restricted to 250 symbols

![2019-08-30 (42)](https://user-images.githubusercontent.com/39421427/64017445-43ee3a00-cb32-11e9-95a5-ad3f76849588.png)


Here we log in with the passenger's account and on the same trip we could see the fields to rate the driver and give feedback to the driver as well as in the driver's account

![2019-08-30 (43)](https://user-images.githubusercontent.com/39421427/64017446-43ee3a00-cb32-11e9-975d-a43fa1717851.png)


If we don't want to see this trip anymore we can cancel it. The option is available in the whole cycle of trip statuses and passenger statuses, as this is the option to delete the trip from the list with your trips as well as the option to cancel it before the trip starts
![2019-08-30 (46)](https://user-images.githubusercontent.com/39421427/64017451-4486d080-cb32-11e9-979e-1f679405b74c.png)


Once you cancel it, you will be redirected to your `My trips` page where it will no longer appear

![2019-08-30 (47)](https://user-images.githubusercontent.com/39421427/64017452-4486d080-cb32-11e9-99c0-ecdce96edab7.png)


Now back in the driver's account. Where the button `Delete trip` is still available, so if for any reason we don't want to see this trip anymore we can use it, and delete it permanently. That is not true delete, we decide to implement  "silent" delete and just patch a field isDeleted in the database, what gives us option to recover the data, if it becomes of any interest. All search methods have an appropriate filter to not show deleted trips

![2019-08-30 (48)](https://user-images.githubusercontent.com/39421427/64017453-4486d080-cb32-11e9-9677-29c8db8a9b59.png)


Now this is done

![2019-08-30 (50)](https://user-images.githubusercontent.com/39421427/64017456-451f6700-cb32-11e9-8ea6-1ee308a29310.png)


And we are redirected to the `My trip` page where is nothing, the trip will not appear in the main search page too

![2019-08-30 (51)](https://user-images.githubusercontent.com/39421427/64017457-451f6700-cb32-11e9-8fdd-8093a4482b86.png)


It is the time for the admin part.
We now login with an admin account, and we can see a new link appears in the navbar - `Admin`

![2019-08-30 (52)](https://user-images.githubusercontent.com/39421427/64017458-451f6700-cb32-11e9-8e77-c1949625bb4c.png)


In the `Admin` page we can see search form like in `Search trips`, but for users. All fields are optional too, if we don't choose any, it will show all users

![2019-08-30 (53)](https://user-images.githubusercontent.com/39421427/64017459-45b7fd80-cb32-11e9-93e0-14c10a1221d6.png)


Now we find the user that we created in the beginning

![2019-08-30 (54)](https://user-images.githubusercontent.com/39421427/64017460-45b7fd80-cb32-11e9-8edb-41ac3f769dd7.png)


Here is the full user information. We have options to edit or delete the user

![2019-08-30 (55)](https://user-images.githubusercontent.com/39421427/64017461-45b7fd80-cb32-11e9-95d5-314c4b562ad7.png)


That is the form with the available fields for editing, username, first name and last name are not expected to be changed.

![2019-08-30 (57)](https://user-images.githubusercontent.com/39421427/64017462-45b7fd80-cb32-11e9-812b-2dde1eab39ba.png)


Here we update the information, and the user is now with user role `ADMIN`

![2019-08-30 (58)](https://user-images.githubusercontent.com/39421427/64017464-46509400-cb32-11e9-8121-9fd678aec2d6.png)


And the other available option is to delete the user. Here "silent" delete is implemented as well. One thing to point out: when the user is "deleted" all his trips are "deleted" as well

![2019-08-30 (59)](https://user-images.githubusercontent.com/39421427/64017465-46509400-cb32-11e9-8034-edb9a54a503f.png)


Now we can not find a user with the first name "Mila"

![2019-08-30 (61)](https://user-images.githubusercontent.com/39421427/64017466-46509400-cb32-11e9-8fa8-262449492613.png)

Being an admin also allows a user to delete a trip. Only for `ADMIN` role when in main search trips page, user open details for the trip, there is a button `Delete trip` 

![2019-08-30 (63)](https://user-images.githubusercontent.com/39421427/64017467-46e92a80-cb32-11e9-9e84-b0c6772403b2.png)


Here is the message for the successful operation

![2019-08-30 (64)](https://user-images.githubusercontent.com/39421427/64017470-46e92a80-cb32-11e9-9860-6d0bc11cc3dd.png)



   ### **Hope you would like the application, we did it all with a lot of passion and positive vibes** :smiley: :+1:


























































