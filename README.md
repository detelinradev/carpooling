# Carpooling app
It is a final project for Telerik Academy Alpha track 'february 2019, made by me in collaboration with [Anton Madzharov](https://github.com/antonmadzharov)


### **Web-based application giving opportunities for user to share car journeys**
 - It has implemented functionality for creating shared journey and joining already created journey
 - Project allows two roles which a user can have: admin and user
 - User is able to create, edit or join trip and admin could edit or delete user and delete trip
 - Technologies - ReactJs, REST, Spring Boot, JPA, MariaDB

### **Simple structure of the project :**
![seYw4vJZ6JGNOUEI3OZCRTw (1)](https://user-images.githubusercontent.com/39421427/64018341-dc85b980-cb34-11e9-8fd6-3a355521f27e.png)

### **We have used for front-end ReactJs**
  - With help of Redux, we greatly improved managing of the state
  - Our components are asynchronus, what means a component being load only if it is called
  - Routing is implemented, and therefore we are using links when moving between components instead of reloading it
  - We added global error handler for responses from server, yet there is local handlers too, for more precise response messages
  - The origin and the destination for the trip in the app are being send to network based API, what delivers the duration and the         distance of the trip
  - Validation for the user input is achieved with as much predefined options as possible, as drop-downs, calendars ect., and use of       validation rules like numeric type and regex check for input fields
  
 ### **Back-end is written on Java 8, it is Spring Boot Gradle based application**
  - Thus it has layered structure, in 3 main layers, Rest Controllers, Services and JPA Repositories, and we are using Dependency         Injection to handle dependencies between modules
  - We are using JWT Security, with identification token bearing the role of the user
  - We chose Global exception handler and optional return types over try catch blocks 
  - The repositories are JPA, what delivers all the database connection functionality in just interfaces, with help of JPQL querries
  - We used Lombok for cutting out the boilerplate code
  - We chose MapStruct as more fast and powerfull dto mapper tool than other similar tools 
  - We made validation of the user input with help of Java Validation API and use of javax.validation for bean validation

**Documentation of the project can be found [here](https://documenter.getpostman.com/view/7601087/SVYwLwZa)**

## **Application gallery**

**We provide a gallery of screen shots for the possible customer interactions with the application**

 This is how the application looks for unregistered user. Here we have just Home component and Login component available
 
![2019-08-30 (2)](https://user-images.githubusercontent.com/39421427/64017472-46e92a80-cb32-11e9-81fc-d868cd52bcf7.png)


 That is Sign-in module. We need to enter username and password to authenticate successfully
 
![2019-08-30 (3)](https://user-images.githubusercontent.com/39421427/64017473-4781c100-cb32-11e9-93c3-df338de5ffa2.png)


Here is Sign-up modul for new users. We need to enter all required data to make an account in the application.
 - username has to be unique for the application
 - password with small and capital letters, digits and special symbols, at last 8 symbols long
 - first and last name should contain only letters
 - email should follow xxxxx@yyyyy.zzzzz pattern
 - phone number should be 10 symbols long, consists only of numbers
 
![2019-08-30 (4)](https://user-images.githubusercontent.com/39421427/64017474-4781c100-cb32-11e9-937d-33c9a57f44ff.png)


 This is the Home window for authenticated user. Here he could see a leaderboard with the TOP 10 drivers and TOP 10 passengers based on the rating they receive after each trip as well as all the possible links in the application, listed on the right side of the navbar on the top of the window
 
![2019-08-30 (5)](https://user-images.githubusercontent.com/39421427/64017475-4781c100-cb32-11e9-8b1e-a3d72c232ad6.png)


That is the profile page, where user could see all the information about himself as well as edited it. He has an options to edit his phone, email and password and add an avatar. If he wants to create a trip, it is mandatory to create a car as well.

![2019-08-30 (6)](https://user-images.githubusercontent.com/39421427/64017476-4781c100-cb32-11e9-9bb8-2416136d47b5.png)


Here the user did upload a picture and recevies a message for successfull operation

![2019-08-30 (7)](https://user-images.githubusercontent.com/39421427/64017477-481a5780-cb32-11e9-9f4d-f7f6d8798ff9.png)


When you press the edit button modal with possible actions appears

![2019-08-30 (8)](https://user-images.githubusercontent.com/39421427/64017478-481a5780-cb32-11e9-8233-11e9d251348c.png)


Here data is successfully changed and there is a message for it. Changes are asynchronous and the whole page is not refreshed

![2019-08-30 (9)](https://user-images.githubusercontent.com/39421427/64017480-481a5780-cb32-11e9-9d30-ce5ce5ec6b27.png)


That is the form for creating a car. You have to enter brand, model and color of your car and year of registration between 1950 and current year. Air conditioned should be 'yes' or 'no'

![2019-08-30 (10)](https://user-images.githubusercontent.com/39421427/64017481-481a5780-cb32-11e9-8576-10afd7b29be9.png)


When car is successfully created another component appears under main one with information about your car

![2019-08-30 (11)](https://user-images.githubusercontent.com/39421427/64017482-48b2ee00-cb32-11e9-90f9-6a379bb994de.png)


If user presses the upload button by mistake without any fail selected, there is a warn message for it 

![2019-08-30 (12)](https://user-images.githubusercontent.com/39421427/64017483-48b2ee00-cb32-11e9-8d1e-15a2e4832b0b.png)


Here picture is successfully uploaded

![2019-08-30 (13)](https://user-images.githubusercontent.com/39421427/64017418-3fc21c80-cb32-11e9-8c64-102c7e8da130.png)


Now user have a car he can create a trip, origin and destination should be entered, information is sent to Bing API for acquiring distance and time for finishing the trip

![2019-08-30 (14)](https://user-images.githubusercontent.com/39421427/64017419-3fc21c80-cb32-11e9-8a59-2be53783df54.png)


There is a calendar for the departure time to make sure user input is in the format we expected

![2019-08-30 (15)](https://user-images.githubusercontent.com/39421427/64017421-405ab300-cb32-11e9-8236-388681aa82b9.png)


Most of the rest options are drop-down lists what makes it easier and more secure to fill the form. Button 'create' becomes active when all fields have values and they passed the validation provided

![2019-08-30 (16)](https://user-images.githubusercontent.com/39421427/64017422-405ab300-cb32-11e9-98b2-0f5645e414b3.png)


After pressing create button user is redirected to 'My Trips' component, where she could see the trip that she just created. The role of the user in the trip appears on the top border in the center as 'driver' or 'passenger'

![2019-08-30 (18)](https://user-images.githubusercontent.com/39421427/64017423-405ab300-cb32-11e9-83ed-876dd032b665.png)


You could see full info for the trip by pressing the 'Details' button. Now you have options to edit trip and add a comment that is visible only for the participants in the trip

![2019-08-30 (19)](https://user-images.githubusercontent.com/39421427/64017424-405ab300-cb32-11e9-86df-99a5c8be75bd.png)


Here comment is created and become visible in the comments area with your name and picture over it

![2019-08-30 (20)](https://user-images.githubusercontent.com/39421427/64017425-40f34980-cb32-11e9-974c-68dfa7bd2097.png)


You could change all the details for the trip. When button 'Update trip' is pressed, a form with trip details pop-up. By simply pressing 'update' button ypu can edit the trip.

![2019-08-30 (21)](https://user-images.githubusercontent.com/39421427/64017426-40f34980-cb32-11e9-8d94-71b88b28bb40.png)


Here the trips is updated with the information provided

![2019-08-30 (22)](https://user-images.githubusercontent.com/39421427/64017427-40f34980-cb32-11e9-8b0b-a7b1ddcd4d25.png)


If you chose value that not follow the rules, for example available seats = 30, you will receive message that operation is not successfull

![2019-08-30 (23)](https://user-images.githubusercontent.com/39421427/64017428-40f34980-cb32-11e9-9410-a3cb74ac1a82.png)


Now once the trip is created we need to sign in with different user and join the trip.
That is how 'Search trips' form looks like. All fields are optional, if you don't chose any, it will load all trips available.
Almost all the fields are predefined, so we could realy on the user's input validity

![2019-08-30 (24)](https://user-images.githubusercontent.com/39421427/64017429-418be000-cb32-11e9-8434-10956276e313.png)


Now our trip is available and visible for the users to join

![2019-08-30 (25)](https://user-images.githubusercontent.com/39421427/64017430-418be000-cb32-11e9-82a0-cf8227d3bf53.png)


By pressing 'Details' button we redirect to component that shows all the info for the trip as well as button 'Join trip'

![2019-08-30 (26)](https://user-images.githubusercontent.com/39421427/64017432-418be000-cb32-11e9-82fa-2045cee308dd.png)


When the user wants to join the trip, and he presses the 'Join trip' button, the button changes to 'Request sent' and new component shows up with user name and picture, his rating as passenger and status of the request, what initialy is 'pending'

![2019-08-30 (27)](https://user-images.githubusercontent.com/39421427/64017433-42247680-cb32-11e9-95f3-e8e70f842fad.png)


Now one can find it in his 'My trips' conteiner where his role on the top of the border will be 'passenger'

![2019-08-30 (28)](https://user-images.githubusercontent.com/39421427/64017434-42247680-cb32-11e9-80e2-3bf66818957a.png)



![2019-08-30 (29)](https://user-images.githubusercontent.com/39421427/64063937-fe01a680-cc03-11e9-83c2-2cf5d1312739.png)
![2019-08-30 (31)](https://user-images.githubusercontent.com/39421427/64017436-42247680-cb32-11e9-848b-a85b0a88371d.png)
![2019-08-30 (32)](https://user-images.githubusercontent.com/39421427/64017437-42bd0d00-cb32-11e9-8178-1dc247476f0f.png)
![2019-08-30 (35)](https://user-images.githubusercontent.com/39421427/64017438-42bd0d00-cb32-11e9-85ca-b2e233cff50c.png)
![2019-08-30 (36)](https://user-images.githubusercontent.com/39421427/64017439-42bd0d00-cb32-11e9-872b-be557da6aee8.png)
![2019-08-30 (37)](https://user-images.githubusercontent.com/39421427/64017440-42bd0d00-cb32-11e9-9f07-ddb6f7df50d0.png)
![2019-08-30 (38)](https://user-images.githubusercontent.com/39421427/64017441-4355a380-cb32-11e9-88c9-46b0e432496c.png)
![2019-08-30 (39)](https://user-images.githubusercontent.com/39421427/64017442-4355a380-cb32-11e9-84ad-6cdd0f8efc88.png)
![2019-08-30 (40)](https://user-images.githubusercontent.com/39421427/64017443-4355a380-cb32-11e9-8c41-7aca708bc57d.png)
![2019-08-30 (41)](https://user-images.githubusercontent.com/39421427/64017444-4355a380-cb32-11e9-9e4d-985a0b047193.png)
![2019-08-30 (42)](https://user-images.githubusercontent.com/39421427/64017445-43ee3a00-cb32-11e9-95a5-ad3f76849588.png)
![2019-08-30 (43)](https://user-images.githubusercontent.com/39421427/64017446-43ee3a00-cb32-11e9-975d-a43fa1717851.png)
![2019-08-30 (44)](https://user-images.githubusercontent.com/39421427/64017448-43ee3a00-cb32-11e9-9c13-5bfadb1f5e10.png)
![2019-08-30 (45)](https://user-images.githubusercontent.com/39421427/64017449-43ee3a00-cb32-11e9-832c-5c5e6bbc1e85.png)
![2019-08-30 (46)](https://user-images.githubusercontent.com/39421427/64017451-4486d080-cb32-11e9-979e-1f679405b74c.png)
![2019-08-30 (47)](https://user-images.githubusercontent.com/39421427/64017452-4486d080-cb32-11e9-99c0-ecdce96edab7.png)
![2019-08-30 (48)](https://user-images.githubusercontent.com/39421427/64017453-4486d080-cb32-11e9-9677-29c8db8a9b59.png)
![2019-08-30 (49)](https://user-images.githubusercontent.com/39421427/64017454-4486d080-cb32-11e9-9a8b-6bcc0cfcb3be.png)
![2019-08-30 (50)](https://user-images.githubusercontent.com/39421427/64017456-451f6700-cb32-11e9-8ea6-1ee308a29310.png)
![2019-08-30 (51)](https://user-images.githubusercontent.com/39421427/64017457-451f6700-cb32-11e9-8fdd-8093a4482b86.png)
![2019-08-30 (52)](https://user-images.githubusercontent.com/39421427/64017458-451f6700-cb32-11e9-8e77-c1949625bb4c.png)
![2019-08-30 (53)](https://user-images.githubusercontent.com/39421427/64017459-45b7fd80-cb32-11e9-93e0-14c10a1221d6.png)
![2019-08-30 (54)](https://user-images.githubusercontent.com/39421427/64017460-45b7fd80-cb32-11e9-8edb-41ac3f769dd7.png)
![2019-08-30 (55)](https://user-images.githubusercontent.com/39421427/64017461-45b7fd80-cb32-11e9-95d5-314c4b562ad7.png)
![2019-08-30 (57)](https://user-images.githubusercontent.com/39421427/64017462-45b7fd80-cb32-11e9-812b-2dde1eab39ba.png)
![2019-08-30 (58)](https://user-images.githubusercontent.com/39421427/64017464-46509400-cb32-11e9-8121-9fd678aec2d6.png)
![2019-08-30 (59)](https://user-images.githubusercontent.com/39421427/64017465-46509400-cb32-11e9-8034-edb9a54a503f.png)
![2019-08-30 (61)](https://user-images.githubusercontent.com/39421427/64017466-46509400-cb32-11e9-8fa8-262449492613.png)
![2019-08-30 (63)](https://user-images.githubusercontent.com/39421427/64017467-46e92a80-cb32-11e9-9e84-b0c6772403b2.png)
![2019-08-30 (64)](https://user-images.githubusercontent.com/39421427/64017470-46e92a80-cb32-11e9-9860-6d0bc11cc3dd.png)




























































