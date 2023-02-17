package com.automationanywhere.bot.calendarBooking;

import com.automationanywhere.bot.emailNotification.EmailNotification;

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
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.FreeBusyRequest;
import com.google.api.services.calendar.model.FreeBusyRequestItem;
import com.google.api.services.calendar.model.FreeBusyResponse;
import com.google.api.services.calendar.model.TimePeriod;
import javax.mail.MessagingException;

public class CalanderBookingImp {

    private Calendar calendarService;

    public CalanderBookingImp(Calendar calendarService) {
        this.calendarService = calendarService;
    }
    public CalanderBookingImp(){
    }

    public String bookFirstFreeTimeSlot() throws IOException, GeneralSecurityException {
        // Build the HTTP transport and JSON factory
        Date startDate = new Date(); ;
        Date endDate = new Date(2023, 2, 28);;
        String summary = "Calendar booked"; 
        String description = "Febin bot meeting";
        
//        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        // Set up the query for the freebusy request
        FreeBusyRequest freeBusyRequest = new FreeBusyRequest();
        freeBusyRequest.setTimeZone(TimeZone.getDefault().getID());

        List<FreeBusyRequestItem> items = new ArrayList<>();
        items.add(new FreeBusyRequestItem().setId("febin3898@gmail.com"));
        freeBusyRequest.setItems(items);

        // Create the credentials object for the user
        GoogleCredential credential = GoogleCredential.getApplicationDefault(httpTransport, jsonFactory);
        if (credential.createScopedRequired()) {
            credential = credential.createScoped(Arrays.asList(CalendarScopes.CALENDAR));
        }

        // Build the calendar client
        Calendar calendarService = new Calendar.Builder(httpTransport, jsonFactory, credential)
                .setApplicationName("A360CalenderChecker")
                .build();

        // Execute the freebusy request to get the list of busy time slots
        FreeBusyResponse freeBusyResponse = calendarService.freebusy().query(freeBusyRequest).execute();
        List<TimePeriod> busySlots = freeBusyResponse.getCalendars().get("primary").getBusy();

        // Find the first free time slot that fits the given duration
        DateTime start = new DateTime(startDate, TimeZone.getDefault());
        DateTime end = new DateTime(endDate, TimeZone.getDefault());
        long durationInMinutes = (endDate.getTime() - startDate.getTime()) / 1000 / 60;
        boolean foundSlot = false;

        for (TimePeriod busySlot : busySlots) {
            Date busyStart = new Date(busySlot.getStart().getValue());
            Date busyEnd = new Date(busySlot.getEnd().getValue());
            long diffInMinutes = (busyStart.getTime() - start.getValue()) / 1000 / 60;
            if (diffInMinutes >= durationInMinutes) {
                // Found a free slot that fits the duration, create the event
                Event event = new Event()
                        .setSummary(summary)
                        .setDescription(description);

                EventDateTime eventStart = new EventDateTime()
                        .setDateTime(start)
                        .setTimeZone(TimeZone.getDefault().getID());
                event.setStart(eventStart);

                EventDateTime eventEnd = new EventDateTime()
                        .setDateTime(new DateTime(busyStart.getTime()))
                        .setTimeZone(TimeZone.getDefault().getID());
                event.setEnd(eventEnd);

                // Add the event to the calendar
                Event createdEvent = calendarService.events().insert("primary", event).execute();
                System.out.printf("Event created: %s\n", createdEvent.getHtmlLink());

                foundSlot = true;
                break;
            }
        }

        String message = null;

        if (foundSlot) {
            try {
                // Send notification email to the user
                EmailNotification.sendEmail("febin3898@gmail.com", "Calendar Booked", "You have a meeting in between"+"");
                message = "Calendar booked in available free slot.";
            } catch (MessagingException m) {
                // Handle email sending exception
            }
        } else {
            System.out.println("No free slot found");
            message ="No free slot found";
        }

        return message;
    }
}
