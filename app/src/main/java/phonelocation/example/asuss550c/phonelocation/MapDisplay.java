package phonelocation.example.asuss550c.phonelocation;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MapDisplay extends AppCompatActivity implements OnMapReadyCallback {
   // static final LatLng TutorialsPoint = new LatLng(21 , 57);
   LatLng sydney ;
    int Viewway=0 ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_display);

        addMarker();
        viewmaps();

       // createMapView();
      //  addMarker();
    }
    private void viewmaps(){
        try {
            MapFragment mapFragment = (MapFragment) getFragmentManager()
                    .findFragmentById(R.id.map);

            mapFragment.getMapAsync(this);
        }
        catch(Exception ex){}
          }

    @Override
    public void onMapReady(GoogleMap map) {
switch (Viewway){
        case 0: {
            map.setMyLocationEnabled(true);
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
            break;
        }
    case 1: {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 2));
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        break;
    }
    case 2: {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18));
        break;
    }
    case 3: {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 2));
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        break;
    }

}

            String CurrentString=SettingSaved.Userlocationinthemap;
            String[] separated = CurrentString.split("%");

        map.addMarker(new MarkerOptions()


                .title(separated[0])
                .snippet(separated[2])
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.imagecover_flag))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(sydney));


    }

    private void addMarker(){

        String CurrentString=SettingSaved.Userlocationinthemap;
        String[] separated1 = CurrentString.split("%");
            String MyLogLat=separated1[1].substring(29,separated1[1].length()-1);
            String[] separated = MyLogLat.split(",");
        double lat=Double.parseDouble(separated[0]);
        double lag=Double.parseDouble( separated[1]);
        /** Make sure that the map has been initialised **/
          sydney = new LatLng(lat,lag);
          //   Toast.makeText(this,  separated[0]+":"+separated[1], Toast.LENGTH_LONG).show();
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map_display, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.StreetView) {
          Intent  newpage=new Intent(  this,VisitLocations.class);
            startActivity(newpage);
        }

        if (id == R.id.Findroute) {
            Location lastloc=SettingSaved.getLocation(this);
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?saddr="+lastloc.getLatitude()+"," + lastloc.getLongitude() + "&daddr=" + sydney.latitude + "," + sydney.longitude));
            startActivity(i);
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.goback) {
            CheckRate();
        }

        return super.onOptionsItemSelected(item);
    }
    // @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            CheckRate();

        }

        return super.onKeyDown(keyCode, event);
    }
    void  CheckRate(){
        // rate app
        if(SettingSaved.IsRated==0) {
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            //Try Google play
                            intent.setData(Uri.parse("market://details?id=phonelocation.example.asuss550c.phonelocationphone"));
                            if (!MyStartActivity(intent)) {
                                //Market (Google play) app seems not installed, let's try to open a webbrowser
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=phonelocation.example.asuss550c.phonelocationphone"));
                                MyStartActivity(intent) ;

                            }
                            SettingSaved.IsRated = 1;
                            SettingSaved sv = new SettingSaved(getApplicationContext());
                            sv.SaveData();
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            finish();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.RateDesc)).setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("later", dialogClickListener).show();
        }
        else
            this.finish();
    }
    // rating app
    private boolean MyStartActivity(Intent aIntent) {
        try
        {
            startActivity(aIntent);
            return true;
        }
        catch (ActivityNotFoundException e)
        {
            return false;
        }
    }
}
