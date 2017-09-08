package soluces.com.pennontautocars.com.app;

import android.util.Log;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Lincoln on 05/01/16.
 */
public class Config {

    // flag to identify whether to show single line
    // or multi line test push notification tray
    public static boolean appendNotificationMessages = true;

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String SENT_TOKEN_TO_SERVER = "sentTokenToServer";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // type of push messages
    public static final int PUSH_TYPE_CHATROOM = 1;
    public static final int PUSH_TYPE_USER = 2;

    // id to handle the notification in the notification try
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }


        /*Timestamp stamp = new Timestamp(time);
        Date date = new Date(stamp.getTime());

        String semaine = date.toString().substring(0,4);
        String mois = date.toString().substring(4,8);
        String jour =date.toString().substring(8,10);
        String annee =date.toString().substring(23,28);*/


        long timestampLong = time;
        Date d = new Date(timestampLong);
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        c.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
        int heures = c.get(Calendar.HOUR_OF_DAY);

        String minute = String.valueOf(c.get(Calendar.MINUTE));

        int mois = c.get(Calendar.MONTH);
        int annee = c.get(Calendar.YEAR);

        String annee_ = String.valueOf(annee).substring(2,4);

        String date_ = conversionLettre(c.get(Calendar.DAY_OF_WEEK))+", "+c.get(Calendar.DAY_OF_MONTH)+" "
                +conversionLettreMois(mois)+". "+annee_;
        String date_inf =heures+":"+minute ;

        long now = System.currentTimeMillis();
        if (time <= 0) {
            return null;
        }
        else if( time > now ){
            time = now;
        }

        final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
           /* return "maintenant";*/
            return ""+date_inf;
        }
        else if (diff < 2 * MINUTE_MILLIS) {
            return ""+date_inf;
        }
        else if (diff < 50 * MINUTE_MILLIS) {
            return ""+date_inf;
        }
        else if (diff < 90 * MINUTE_MILLIS) {
            return ""+date_;
        }
        else if (diff < 24 * HOUR_MILLIS) {
            return ""+date_inf;
        }
        else if (diff < 48 * HOUR_MILLIS) {
            return ""+date_;
        }
        else {

            return ""+date_;
        }
    }

    public static String conversionLettre(int day){
        String day_week [] ={"Sam","Dim","Lun","Mar","Mer","Jeu","Ven"};
        for(int i =0;i<day_week.length;i++ ){
            if(i == day){
                return day_week[i];
            }
        }
        return null;
    }

    public static String conversionLettreMois(int mois){
        String day_mois [] ={"Janv","Fév","Mar","Avr","Mai","Juin","Jui","Août","Sept","Oct","Nov","Déc"};
        for(int i =0;i<day_mois.length;i++ ){
            if(i == mois){
                return day_mois[i];
            }
        }
        return null;
    }
}
