package phonelocation.example.asuss550c.phonelocation;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//ful screen activity

  setContentView(R.layout.activity_main);


        // load setting informatin if we have
        SettingSaved settingSaved = new SettingSaved(this);
        settingSaved.LoadData();

        SettingSaved.ISLocForLocation = true;

        ListView list = (ListView) findViewById(R.id.listView);

        list.setAdapter(new VivzAdapter(this));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent newpage;
                switch (position) {
                    case 0:

                        newpage = new Intent(MainActivity.this, FindMyphone.class);
                        startActivity(newpage);
                        LoadAdmob();
                        break;
                    case 1:
                        MyLOC();

                        break;

                    case 2:
                        //newpage=new Intent( MainActivity.this,Registeration.class);
                        // startActivity(newpage);
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml(getResources().getString(R.string.sharemessage) + "  https://play.google.com/store/apps/details?id=phonelocation.example.asuss550c.phonelocationphone"));
                        startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.ShareUse)));
                        break;


                    case 3:
                        newpage = new Intent(MainActivity.this, SubSetting.class);
                        startActivity(newpage);


                }
            }
        });
//ask for permsion
        Location location = getlocation();

        //Tracking


    }

    public void MyLOC(){

        try {
            Location lastloc=SettingSaved.getLocation(this);

            SmsManager smsManagersend = SmsManager.getDefault();
            if(SettingSaved.UserPhoneNumber.length()>4) {  // in case we have saved list phone
                for (Map.Entry m : SettingSaved.WhoFindMeIN.entrySet())
                    smsManagersend.sendTextMessage(m.getKey().toString(), null,"%"+ getResources().getString(R.string.HelpMSG) + "%https://www.google.com/maps/@"+lastloc.getLatitude()+","+ lastloc.getLongitude()+",15z/data=!4m2!3m1!1s0x0000000000000000:0x08cdd5fc704c0eb2", null, null);
    MessageSend( getResources().getString(R.string.MsgSent) );
            }
        } catch(Exception e)   {
            MessageSend( getResources().getString(R.string.MsgFail)  );  }
    }
public void MessageSend(String msg){
    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
}



    @Override
    protected void onResume() {
        super.onResume();

    }
    InterstitialAd mInterstitialAd;
    private  void    LoadAdmob(){
        try{
            mInterstitialAd = new InterstitialAd(this);
            mInterstitialAd.setAdUnitId(getResources().getString(R.string.Pop_ad_unit_id));
            AdRequest adRequest = new AdRequest.Builder()
                    //  .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .build();
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {

                }

                @Override
                public void onAdLoaded() {
                    DisplayAdmob();
                }
            });
        } catch (Exception ex) {
        }
    }
    private void DisplayAdmob() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        if ( !mInterstitialAd.isLoaded()) {
            AdRequest adRequest = new AdRequest.Builder().build();
            mInterstitialAd.loadAd(adRequest);
        }
    }

    // @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
           // onQuitPressed();


        }

        return super.onKeyDown(keyCode, event);
    }


    /// liste item

    class VivzAdapter extends BaseAdapter
    {

        ArrayList<ListItem> list ;
        Context context;
        VivzAdapter (Context c)
        { context=c;
//
            list=new ArrayList<ListItem>();
            list.add(new ListItem(getResources().getString(R.string.FindTitle), getResources().getString(R.string.FindDesc) ,R.drawable.dmap));
            list.add(new ListItem(getResources().getString(R.string.HelpTitle), getResources().getString(R.string.HelpDesc) ,R.drawable.helpmep ));
             list.add(new ListItem(getResources().getString(R.string.ShareTitle),  getResources().getString(R.string.ShareDesc) ,R.drawable.newaccount ));
            list.add(new ListItem(getResources().getString(R.string.SettingTitle),  getResources().getString(R.string.SettingDesc) ,R.drawable.settingp));
            list.add(new ListItem("ads",  "ads display" ,R.drawable.settingmap));


        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            ListItem temp=list.get(i);
            if(!temp.Title.equals("ads")) { // isnot ads
            View row= inflater.inflate( R.layout.singlerow,viewGroup,false );
            TextView title=(TextView) row.findViewById(R.id.textView1);
            TextView detals=(TextView) row.findViewById(R.id.textView2);
            ImageView image =(ImageView) row.findViewById(R.id.imageView);



            title.setText(temp.Title);
            detals.setText(temp.Detals);// it updated
            image.setImageResource(temp.ImageURL);

            return row;
            }

            else
            {
                View row = inflater.inflate(R.layout.ads_row, viewGroup, false);

                AdView mAdView = (AdView) row.findViewById(R.id.adView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);

                return row;


            }




        }



    }


    //access to permsions
    Location getlocation(){
        Location myLocation=null;
        try{
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {

                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED  ){

                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(getResources().getString(R.string.RequestPermision))
                                    .setCancelable(false)
                                    .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                                                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                                                            android.Manifest.permission.RECEIVE_SMS,
                                                            android.Manifest.permission.READ_SMS,
                                                            android.Manifest.permission.SEND_SMS,
                                                    },
                                                    REQUEST_CODE_ASK_PERMISSIONS);
                                        }
                                    })
                                    .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();



                        return null;
                    }}
                myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (myLocation == null) {
                    myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

                }

            }
        }
        catch (Exception ex){

        }
        return myLocation;
    }
    //get acces to location permsion
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // Permission Denied
                    Toast.makeText( this, getResources().getString(R.string.DenailMessage), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
