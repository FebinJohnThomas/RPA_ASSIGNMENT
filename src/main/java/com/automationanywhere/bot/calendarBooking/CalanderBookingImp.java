package com.automationanywhere.bot.calendarBooking;

import com.automationanywhere.bot.emailNotification.EmailNotification;

import java.io.FileInputStream;
import java.util.*;
import java.io.IOException;
import java.security.GeneralSecurityException;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import lombok.extern.slf4j.Slf4j;

import javax.mail.MessagingException;
@Slf4j
public class CalanderBookingImp {

    private Calendar calendarService;
    private String message ;
    private static final String CREDENTIALS_FILE_PATH = "**";

    private  Date startDate = new Date(123,2,22,20,00);

    private  Date endDate = new Date(123,2,22,21,00);

    private final String emailId = "**";

    private final String applicationName = "**";


    public CalanderBookingImp(Calendar calendarService) {
        this.calendarService = calendarService;
    }
    public CalanderBookingImp(){
    }

    public String bookFirstFreeTimeSlot() throws IOException, GeneralSecurityException {
        // Build the HTTP transport and JSON factory


        try {
            HttpTransport httpTransport = new NetHttpTransport();
            JsonFactory jsonFactory = new JacksonFactory();


            // Set up the query for the freebusy request
            FreeBusyRequest freeBusyRequest = new FreeBusyRequest();
            freeBusyRequest.setTimeZone(TimeZone.getDefault().getID());

            List<FreeBusyRequestItem> items = new ArrayList<>();
            items.add(new FreeBusyRequestItem().setId(emailId));
            freeBusyRequest.setItems(items);

              DateTime timeMin = new DateTime(startDate, TimeZone.getDefault());
              Date timeMaxendDate = new Date(endDate.getTime() + 86400000);
              DateTime timeMax = new DateTime(timeMaxendDate, TimeZone.getDefault());


            freeBusyRequest.setTimeMin(timeMin);
            freeBusyRequest.setTimeMax(timeMax);

            // Create the credentials object for the user
            GoogleCredential credential = GoogleCredential.fromStream(new FileInputStream(CREDENTIALS_FILE_PATH));
            if (credential.createScopedRequired()) {
                credential = credential.createScoped(Arrays.asList(CalendarScopes.CALENDAR));
            }

            // Build the calendar client
            Calendar calendarService = new Calendar.Builder(httpTransport, jsonFactory, credential)
                    .setApplicationName(applicationName)
                    .build();


            // Execute the freebusy request to get the list of busy time slots
            FreeBusyResponse freeBusyResponse = calendarService.freebusy().query(freeBusyRequest).execute();
            //Only considering primary calendar.
            List<TimePeriod> busySlots = freeBusyResponse.getCalendars().get(emailId).getBusy();

            // Find the first free time slot that fits the given duration
            DateTime start = new DateTime(startDate, TimeZone.getDefault());
            DateTime end = new DateTime(endDate, TimeZone.getDefault());

//            long durationInMinutes = (endDate.getTime() - startDate.getTime()) / 1000 / 60;
            boolean freeSlot = true;
            if(!busySlots.isEmpty()) {
                for(TimePeriod bs : busySlots ){

                    if (!isOverlapping(start,end,bs.getStart(),bs.getEnd())) {
                        freeSlot = false;
                        break;
                    }
                    else{
                        freeSlot = true;

                    }
                }
            }

            if(freeSlot) {
                bookEvent(start,end);
                try {
                    EmailNotification emailNotification = new EmailNotification();
                    // Send notification email to the user
                    emailNotification.sendEmail(emailId, "Calendar Booked", "You have a meeting in between "+startDate+" to "+endDate);
                    message = "Calendar booked in available free slot.";
                } catch (MessagingException m) {
                    // Handle email sending exception
                }
            }
            else {
                System.out.println("No free slot found");
                message = "No free slot found";
            }

            return message;
        }
        catch(Exception e){
            throw e;
        }
    }


    /*
   The method will check if the given interval of time for meeting overlaps any other meeting.
    */
    private boolean isOverlapping(DateTime startTime, DateTime endTime, DateTime existingStart, DateTime existingEnd){

        return startTime.getValue() < existingStart.getValue() && endTime.getValue() <= existingStart.getValue() ||
                startTime.getValue() >= existingEnd.getValue() && endTime.getValue() > existingEnd.getValue();
    }

   /*
   The method will book an event in the calendar.
    */
    private void bookEvent(DateTime startDate, DateTime endDate){
       try {
           String summary = "Calendar booked";
           String description = "Febin bot meeting";

           Event event = new Event()
                   .setSummary(summary)
                   .setDescription(description);

           EventDateTime eventStart = new EventDateTime()
                   .setDateTime(startDate)
                   .setTimeZone(TimeZone.getDefault().getID());
           event.setStart(eventStart);

           EventDateTime eventEnd = new EventDateTime()
                   .setDateTime(endDate)
                   .setTimeZone(TimeZone.getDefault().getID());
           event.setEnd(eventEnd);

           // Add the event to the calendar
           Event createdEvent = calendarService.events().insert(emailId, event).execute();
           System.out.printf("Event created: %s\n", createdEvent.getHtmlLink());
       }
       catch(Exception e){
         e.printStackTrace();
       }
    }
}
