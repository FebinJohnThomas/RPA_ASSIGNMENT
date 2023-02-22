import com.automationanywhere.bot.calendarBooking.CalanderBookingImp;
import com.automationanywhere.botcommand.data.Value;
import com.automationanywhere.botcommand.data.impl.StringValue;
import com.automationanywhere.botcommand.exception.BotCommandException;
import com.automationanywhere.commandsdk.annotations.*;
import com.automationanywhere.commandsdk.annotations.rules.NotEmpty;
import com.automationanywhere.commandsdk.i18n.Messages;
import com.automationanywhere.commandsdk.i18n.MessagesFactory;

import static com.automationanywhere.commandsdk.model.AttributeType.TEXT;
import static com.automationanywhere.commandsdk.model.DataType.STRING;

//BotCommand makes a class eligible for being considered as an action.
@BotCommand

//CommandPks adds required information to be dispalable on GUI.
@CommandPkg(
        //Unique name inside a package and label to display.
        name = "CalanderBooking", label = "[[CalanderBooking.label]]",
        node_label = "[[CalanderBooking.node_label]]", description = "[[CalanderBooking.description]]", icon = "pkg.svg",

        //Return type information. return_type ensures only the right kind of variable is provided on the UI.
        return_label = "[[CalanderBooking.return_label]]", return_type = STRING, return_required = true)
public class CalanderBookingBot {

    //Identify the entry point for the action. Returns a Value<String> because the return type is String.
    //The parameters secondString is not required which given just for showing how it can be implemented when
    // there is a need for it.
    @Execute
    public Value<String> action(
            @Idx(index = "2", type = TEXT)
            @Pkg(label = "[[CalanderBooking.secondString.label]]")
            @NotEmpty
            String secondString) {

        String message = null;
        //Business logic
        try {
            CalanderBookingImp calanderBookingImp = new CalanderBookingImp();
             message = calanderBookingImp.bookFirstFreeTimeSlot();
        }
        catch(Exception e){
            throw new BotCommandException("Error while booking calendar.");
        }

        //Return StringValue.
        return new StringValue(message);
    }
}
