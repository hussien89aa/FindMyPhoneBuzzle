package phonelocation.example.asuss550c.phonelocation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.telephony.SmsManager;
import android.widget.Toast;
import java.lang.String;
import java.util.HashMap;
import java.util.Map;


public class SettingSaved  {
  public static String UserPhoneNumber="";

    // this varible find user location
    public static String Userlocationinthemap="77:43";
    public static final String MyPREFERENCES = "MyPrefsFindPhone" ;
    /// this varible for loc for my locations
    public static boolean ISLocForLocation=false ;

    public static Map<String,String> WhoFindMeIN=new HashMap<String,String>();
    public static    Map<String,String> WhoIFindIN=new HashMap<String,String>();
    public static int WhoFindMeTag=0;  // 0 for who find me // 1 for who i find
    public static String FindCode="No_code";
    public  static  int IsRated=0;
    Context context;
    SharedPreferences sharedpreferences;
    public  SettingSaved(Context context) {
        this.context=context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

    }
    public  void sendm(String senderNum ){
        Location lastloc=SettingSaved.getLocation(context);

        SmsManager smsManagersend = SmsManager.getDefault();
        smsManagersend.sendTextMessage(senderNum, null, "%Here is my phone %https://www.google.com/maps/@" + lastloc.getLatitude() + "," + lastloc.getLongitude() + ",15z/data=!4m2!3m1!1s0x0000000000000000:0x08cdd5fc704c0eb2", null, null);

    }


    public  void SaveData( )  {

        //get list of user authorize to find me
        String StringWhoFindMeIN="";
        for(Map.Entry m:WhoFindMeIN.entrySet())
           {
                StringWhoFindMeIN = StringWhoFindMeIN + "%" + m.getKey() + "%" + m.getValue();
           }
        //==================

        /// save list of who i could find.
        String StringWhoColudIfind="";
        for(Map.Entry m:WhoIFindIN.entrySet())
        {
                StringWhoColudIfind = StringWhoColudIfind + "%" + m.getKey()+"%"+m.getValue() ;
        }

        //save data
        try

        {

            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("WhoFindMeIN",StringWhoFindMeIN);
            editor.putString("WhoCouldIFind",StringWhoColudIfind);
            editor.putString("FindCode",FindCode);
            editor.putInt("IsRated",IsRated);

            editor.commit();
            LoadData( );
        }

        catch( Exception e)

        {

        }
        // Show Alert
          Toast.makeText(context, context.getResources().getString(R.string.UpdateMsg) , Toast.LENGTH_LONG).show();


}
    public  void LoadData(  ) {


        try {
// who could find me
           String StringWhoFindMeIN= sharedpreferences.getString("WhoFindMeIN", "empty");
            String AlowsTrackPhone="";

            if(!StringWhoFindMeIN.equals("empty")) {
                String[] WhoFindMe = StringWhoFindMeIN.split("%");
                WhoFindMeIN.clear();
                if (WhoFindMe.length>1) //avoid error
                for (int i = 0; i < WhoFindMe.length; i = i + 2) {
                    WhoFindMeIN.put(WhoFindMe[i], WhoFindMe[i + 1]);
                    AlowsTrackPhone = AlowsTrackPhone + ":" + WhoFindMe[i]; // load list find my phone
                }
                UserPhoneNumber = AlowsTrackPhone;
            }

            //who i find
            String StringWhoIFind= sharedpreferences.getString("WhoCouldIFind", "empty");
            if(!StringWhoIFind.equals("empty")) {
                String[] WhoIFind = StringWhoIFind.split("%");
                WhoIFindIN.clear();
                if (WhoIFind.length>1) //avoid error
                for (int i = 0; i < WhoIFind.length; i = i + 2)
                    WhoIFindIN.put(WhoIFind[i], WhoIFind[i + 1]);
            }
            String FindCodeMe=sharedpreferences.getString("FindCode", "empty");
            if(!FindCodeMe.equals("empty"))
                FindCode=sharedpreferences.getString("FindCode", "empty");

            // load ofr first time
            if(FindCodeMe.equals("empty")&&StringWhoIFind.equals("empty")&& StringWhoFindMeIN.equals("empty"))
            {
                Intent imap = new Intent(context,SubSetting.class);
                imap.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(imap);
            }

            IsRated=sharedpreferences.getInt("IsRated", 0);
        } catch (Exception e) {

        }
    }
    public static Location getLocation(Context conetxt) {
       try{
        //   if (MyLocationListener.location!= null)
         //      return MyLocationListener.location;
           LocationManager locationManager = (LocationManager) conetxt.getSystemService(Context.LOCATION_SERVICE);
           if (locationManager != null) {

               Criteria  criteria = new Criteria();
               criteria.setAccuracy(Criteria.ACCURACY_FINE);

               criteria.setCostAllowed(false);

               String  provider = locationManager.getBestProvider(criteria, false);

               Location lastKnownLocationGPS = locationManager.getLastKnownLocation(provider); //(LocationManager.GPS_PROVIDER);
               if (lastKnownLocationGPS != null) {
                   return lastKnownLocationGPS;
               } else {
                   Location loc =  locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                   return loc;
               }
           } else {
               return null;
           }
       }
       catch (Exception ex){return null;}
    }
}
