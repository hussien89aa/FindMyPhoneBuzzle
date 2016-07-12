package phonelocation.example.asuss550c.phonelocation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;


public class IncomingSms extends BroadcastReceiver  {


static int NotifyID=1;
    public void onReceive(Context context, Intent intent) {
        // if he have record online only when call
        String action=intent.getAction();
        final Bundle bundle = intent.getExtras();

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                SmsMessage[] messages = new SmsMessage[pdusObj.length];
                for (int i = 0; i < messages.length; i++) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        String format = bundle.getString("format");
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i], format);
                    }
                    else {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    }
                   // SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNum =messages[i].getOriginatingAddress();
                     String message =messages[i].getMessageBody();//

                    if (   (( ManagmentOperations.IsPhoneAut(senderNum,SettingSaved.UserPhoneNumber)==true) && (message.equals("Yo@urLoca#5tionPlea%se")))||(message.equals("Yfo@urLoca#tionPlea%se")) || ((message.equals(SettingSaved.FindCode)) &&(!SettingSaved.FindCode.equals("No_code")) ) )
                    {  // Toast.makeText(context, SettingSaved.UserPhoneNumber, Toast.LENGTH_LONG).show();
                        SettingSaved settingSaved=new SettingSaved(context);
                        settingSaved.sendm( senderNum );
                     //   Toast.makeText(context, "goret", Toast.LENGTH_LONG).show();
                    }
                    // this code for admins who recives location and change it to real data
                   else if (( ManagmentOperations.IsPhoneAut(senderNum,SettingSaved.UserPhoneNumber)==true)  ||    (message.charAt(0) == '%') ) {
                         // for send messagemessagelocatin bay asking for location
                        if (message.charAt(1) == '%') {
                            String Myinfo=message.substring(1,message.length()-1);
                            String[] separated = Myinfo.split("%");
                            if (separated.length == 1) {
                                SettingSaved.Userlocationinthemap = Myinfo +"%"+ senderNum ;
                                Toast.makeText(context,context.getResources().getString(R.string.ReciveLocation), Toast.LENGTH_LONG).show();
                                try {

                                    NewMessageNotification tn=new NewMessageNotification();
                                    tn.notify(context, ManagmentOperations.NumberToName(context,senderNum),message ,NotifyID);
                                    NotifyID++;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    }

                } // end for loop
            } // bundle is null

        } catch (Exception e) {
           // Log.e("SmsReceiver", "Exception smsReceiver" +e);

        }
    }

  //}  /only when reciev message



}
