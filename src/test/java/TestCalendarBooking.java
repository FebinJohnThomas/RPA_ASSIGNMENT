import com.automationanywhere.bot.emailNotification.EmailNotification;
import com.automationanywhere.botcommand.data.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCalendarBooking {
    @Test
    public void testCalendarBooking(){
//        String passValue = "Initialize";
//        CalanderBookingBot calanderBookingBot = new CalanderBookingBot();
//        Value<String> string = calanderBookingBot.action(passValue);
//        Assert.assertEquals("Calendar booked in available free slot.",string);
        try {
            EmailNotification.sendEmail("febin3898@gmail.com", "booking", "new");
        }
        catch(Exception e){

        }
    }
}
