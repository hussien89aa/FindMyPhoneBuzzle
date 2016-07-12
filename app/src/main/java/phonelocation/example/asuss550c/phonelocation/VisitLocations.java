package phonelocation.example.asuss550c.phonelocation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.LatLng;

public class VisitLocations extends FragmentActivity implements OnStreetViewPanoramaReadyCallback {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visit_locations);


        StreetViewPanoramaFragment streetViewPanoramaFragment =
                (StreetViewPanoramaFragment) getFragmentManager()
                        .findFragmentById(R.id.map);
        streetViewPanoramaFragment.getStreetViewPanoramaAsync(this);
    }


    @Override
    public void  onStreetViewPanoramaReady(StreetViewPanorama panorama) {
     try{

         String CurrentString= SettingSaved.Userlocationinthemap;
         String[] separated1 = CurrentString.split("%");
         String MyLogLat=separated1[1].substring(29,separated1[1].length()-1);
         String[] separated = MyLogLat.split(",");
         double lat=Double.parseDouble(separated[0]);
         double lag=Double.parseDouble( separated[1]);
         panorama.setPosition(new LatLng(lat,lag));
     }
     catch ( Exception ex){}
    }
}

