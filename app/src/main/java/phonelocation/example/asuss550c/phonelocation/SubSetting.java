package phonelocation.example.asuss550c.phonelocation;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class SubSetting extends AppCompatActivity {
 Button bustopNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_setting);
        bustopNext=(Button)findViewById(R.id.bustopNext);
        bustopNext.setVisibility(View.GONE);

       try
       {
           LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
           if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
           {
               startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
               MessageSend("This App Use GPS please go Head and enable it");

           }
       }
       catch (Exception ex){}


Intent intent=new Intent(this,PhoneVerify.class);
      startActivity(intent);
        Location loc =getlocation();
    }

    public void MessageSend(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }



    // save setting
    public void WhoFindMe(View view){
        SettingSaved.WhoFindMeTag=0;
        Intent newpage=new Intent(  this,ContactList.class);
        startActivity(newpage);
        bustopNext.setVisibility(View.VISIBLE);
    }

    public void WhoIFind(View view){
        SettingSaved.WhoFindMeTag=1;
        Intent newpage=new Intent(  this,ContactList.class);
        startActivity(newpage);
        bustopNext.setVisibility(View.VISIBLE);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK )
            {
                SaveFindCode();
        }
        return super.onKeyDown(keyCode, event);
    }
// saving use find code
void  SaveFindCode(){
    EditText MyCode=(EditText) findViewById(R.id.EDTpassword);
    if(MyCode.getText().length()>0)
    {   SettingSaved.FindCode=MyCode.getText().toString();
        SettingSaved settingSaved=new SettingSaved(this);
        settingSaved.SaveData();}
}
    public void buNext(View view) {
        SaveFindCode();
        this.finish();
    }

    //access to permsions
    Location getlocation(){
        Location myLocation=null;
        try{
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {

                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED  ) {

                        if (!shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(getResources().getString(R.string.RequestPermision))
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
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
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                                            dialog.cancel();
                                        }
                                    });
                            final AlertDialog alert = builder.create();
                            alert.show();



                            return null ;
                        }

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
                    Toast.makeText( this, "Find location Denied we cannot process your add account", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}
