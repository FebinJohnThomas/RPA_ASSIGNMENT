import com.automationanywhere.bot.calendarBooking.CalanderBookingImp;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;
import com.google.api.client.util.DateTime;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "CalanderBooking", label = "App for booking google Calendar",
        node_label = "App for booking google Calendar", description = "App helps to book google calendar according to free available slot", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "Message", return_type = STRING, return_required = true)
public class CalanderBookingBot {

    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    //The parameters secondString is not required which given just for showing how it can be implemented when
    // there is a need for it.
    @Execute
    public Value<String> action(

            @Idx(index = "1", type = TEXT)
            @Pkg(label = "gmail address of the calendar you want to book.")
            @NotEmpty
            String email,
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "Start date and time for the event. Provide the dateTime in the format 'yyyy-MM-dd'T'HH:mm:ss' ")
            @NotEmpty
            String startDateTime,
            @Idx(index = "3", type = TEXT)
            @Pkg(label = "Start date and time for the event. Provide the dateTime in the format 'yyyy-MM-dd'T'HH:mm:ss'")
            @NotEmpty
            String endDateTime
            ) {

        String message = null;
        //Business logic
        try {
            CalanderBookingImp calanderBookingImp = new CalanderBookingImp();

             message = calanderBookingImp.bookFirstFreeTimeSlot(email,startDateTime,endDateTime);
        }
        catch(Exception e){
            throw new BotCommandException("Error while booking calendar.");
        }

        //Return StringValue.
        return new StringValue(message);
    }
}
