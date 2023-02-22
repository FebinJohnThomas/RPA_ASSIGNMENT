# RPA_ASSIGNMENT

-> This project developed over automation anywhere SDK which allows to create custom RPA applications.
-> To use this SDK it should be installed in your device first, which can be downloaded through automation anywhere official site.
-> Project involves mainly 3 java classes in which CalendarBookingBot class is performed with Automation anywhere SDK annotations
which allows to communicate with bot application.
-> Actual purpose of the application is to book a Google calendar event according to availability at the time provided.
-> The application can be tested by running the TestCalendarBooking class in the package "src/test/java".



# Things to be done in CalendarBookingImp class in order to run the Application.

-> The main purpose of the class is to book the event in google calendar for the given user email Id.
-> "CREDENTIALS_FILE_PATH" this variable requires the path to the json file showing the service account credential.
This json file will be provided by the Google account when we create an application in it.
-> "startDate" will be the date you are planning to start thr event an example is given there.
-> "endDtae" will be the date you are planning to end the event.
-> "applicationName" the Google application name should be given there.
-> "emailId" the email id on which the event to be booked.



# Things to be done in EmailNotificationClass class in order to run the Application.

-> The main purpose of the class is to sent mail to the user when an event is created.
-> "userName" which will be the mail id used to send the mail to the user, it should be smtp enabled in order to send mail.
-> "password" password of the mail id used to send mail.
-> "host" the smtp host where the email id used to send email is provided.

