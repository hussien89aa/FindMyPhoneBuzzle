package phonelocation.example.asuss550c.phonelocation;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;


public class ContactList extends AppCompatActivity {
      ListView listv;
  public   ArrayList<ListItem> list ;
    private VivzAdapter adpter;
    String SearchText="%";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
    list=new ArrayList<ListItem>();
//ask for permsion
        Location location=   getlocation();

         listv =(ListView) findViewById (R.id.listView) ;
        adpter=new VivzAdapter(this,list);
        listv.setAdapter(adpter);

        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                ListItem temp=list.get(position); // case select the item

                if (temp.ImageURL==R.drawable.gpson)  // case
                {  // chaneg to off we delect it
                    if ( SettingSaved.WhoFindMeTag==0) // case who find me
                        SettingSaved.WhoFindMeIN.remove(temp.Detals);
                    else
                        SettingSaved.WhoIFindIN.remove(temp.Detals);
                }

                else  /// in case add item to list
                {

                      if ( SettingSaved.WhoFindMeTag==0) // case who find me
                    {
                        SettingSaved.WhoFindMeIN.put(temp.Detals, temp.Title);
                        MsgSent(temp.Detals,"Hi, I Added you as person who could find me , you could find me use app https://goo.gl/BMGPm9");
                    }
                    else {
                        SettingSaved.WhoIFindIN.put(temp.Detals, temp.Title);
                        MsgSent(temp.Detals,"Hi, I Added you as person who could i find, please add me as person who could find you in app, https://goo.gl/BMGPm9");

                    }
                }
                SaveData();  // save change

            }
        });
    }


    private void SaveData(){
        SettingSaved settingSaved=new SettingSaved(this);
        settingSaved.SaveData();
        settingSaved. LoadData(); // load data
        UpdetListContact( );// init the contact list
       // adpter.notifyDataSetChanged();
    }
    SearchView searchView;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_list, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        //final Context co=this;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>0)
                    SearchText="%" +newText+ "%";
                else
                    SearchText="%";

                UpdetListContact();

                return false;
            }
        });
        //   searchView.setOnCloseListener(this);
        return true;
    }

    void MsgSent(final  String Phone,final String Message){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.smsSend))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {

                        try {
                            SmsManager smsManagersend = SmsManager.getDefault();
                            for (Map.Entry m : SettingSaved.WhoFindMeIN.entrySet())
                                smsManagersend.sendTextMessage(Phone, null,Message, null, null);

                        } catch(Exception e)   {
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.goback) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
    class VivzAdapter extends BaseAdapter
    {


        Context context;
        ArrayList<ListItem> list;
        VivzAdapter (Context c,ArrayList<ListItem> list)
        { context=c;
            this.list=list;
        }


        @Override
        public int getCount() {
            return this.list.size();
        }

        @Override
        public Object getItem(int i) {
            return this.list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            LayoutInflater inflater =( LayoutInflater ) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row= inflater.inflate( R.layout.single_row_conact,viewGroup,false );
            TextView title=(TextView) row.findViewById(R.id.textView1);
            TextView detals=(TextView) row.findViewById(R.id.textView2);
            ImageView image =(ImageView) row.findViewById(R.id.imageView);
            ListItem temp=this.list.get(i);


            title.setText(temp.Title);
            detals.setText(temp.Detals);// it updated
            image.setImageResource(temp.ImageURL);

            return row;






        }



    }

    void UpdetListContact( ){
        try{


        list.clear();
        // get all contact to list
        String selection = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME+" like'" + SearchText +"'";
        ArrayList<ListItem> list_contact=new ArrayList<ListItem>() ;
        Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, selection,null,  "upper("+ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + ") ASC");
        while (cursor.moveToNext()) {
            String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

            String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            list_contact.add(new ListItem(name, ManagmentOperations.FormatPhoneNumber(phoneNumber)    ,R.drawable.phonelocation));


        }

// if the name is save chane his text
        // case who find me
        String IsFound=null;
        for (ListItem cs : list_contact) {
            if ( SettingSaved.WhoFindMeTag==0)
                IsFound=SettingSaved.WhoFindMeIN.get(cs.Detals); // for case who could find me list
            else
                IsFound=SettingSaved.WhoIFindIN.get(cs.Detals);  // for case who i could find list

            if (IsFound!=null)
                list.add(new ListItem(cs.Title, cs.Detals, R.drawable.gpson));
            else
                list.add(new ListItem(cs.Title, cs.Detals, R.drawable.gpsoff));

        }

        }
        catch (Exception ex){}
        adpter.notifyDataSetChanged();
    }

    //access to permsions
    Location getlocation(){
        Location myLocation=null;
        try{
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager != null) {

                if ( Build.VERSION.SDK_INT >= 23){
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) !=
                            PackageManager.PERMISSION_GRANTED  ){

                        requestPermissions(new String[]{
                                        android.Manifest.permission.READ_CONTACTS},
                                REQUEST_CODE_ASK_PERMISSIONS);

                        return null;
                    }
                }
                UpdetListContact();// init the contact list
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
                    UpdetListContact();// init the contact list
                } else {
                    // Permission Denied
                    Toast.makeText( this,getResources().getString(R.string.DenailMessage) , Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
