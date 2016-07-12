package phonelocation.example.asuss550c.phonelocation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class FindMyphone extends AppCompatActivity {

    ListView listv;
    public ArrayList<ListItem> list ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_myphone);
        listv =(ListView) findViewById (R.id.listView) ;


        listv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position!=listv.getCount()-1){
                Intent newpage;
                ListItem temp=list.get(position); // case select the item

                try {
                    SmsManager smsManagersend = SmsManager.getDefault();
                    smsManagersend.sendTextMessage(temp.Detals, null,"Yo@urLoca#5tionPlea%se", null, null);
                    MessageSend( getResources().getString(R.string.WatingStart)+ " "+ temp.Title+ " " + getResources().getString(R.string.WatingEnd));
                } catch(Exception e)   {
                    newpage=new Intent( FindMyphone.this,SubSetting.class);
                    startActivity(newpage);
                    MessageSend( getResources().getString(R.string.WatingError));
                }

            }

            }
        });
    }
    @Override
    protected void onResume() {
       // UpdetListContact();
        //ask for permsion
        Location location=   getlocation();
        super.onResume();

    }
    void     UpdetListContact(){
        listv.setAdapter(new VivzAdapter(this));
    }
    public void MessageSend(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_myphone, menu);
        return true;
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
        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    Boolean IsDisplayMessage=false;
    class VivzAdapter extends BaseAdapter
    {


        Context context;
        VivzAdapter (Context context)
        { this.context=context;
            list=new ArrayList<ListItem>();

            // get all contact to list
            ArrayList<ListItem> list_contact=new ArrayList<ListItem>() ;
            Cursor cursor = getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);
            while (cursor.moveToNext()) {
                String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                list_contact.add(new ListItem(name, ManagmentOperations.FormatPhoneNumber(phoneNumber)   ,R.drawable.phonelocation));


            }

// if the name is save chane his text
            // case who find me
            String IsFound=null;
            for (ListItem cs : list_contact) {

                    IsFound=SettingSaved.WhoIFindIN.get(cs.Detals);  // for case who i could find list

                if (IsFound!=null)
                    list.add(new ListItem(cs.Title, cs.Detals, R.drawable.dmap));

            }
            // add new one
            list.add(new ListItem("Add$New", "no_desc", R.drawable.dmap));


//ask for add users to track you
if (list.size()==1  && IsDisplayMessage==false) {
    IsDisplayMessage=true;

    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setMessage(getResources().getString(R.string.settingPage))
            .setCancelable(false)
            .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                    SettingSaved.WhoFindMeTag=1;
                    Intent newpage=new Intent(  getApplicationContext(),ContactList.class);
                    startActivity(newpage);
                    //finish();
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

            if (temp.Title.equals("Add$New")){

                View row = inflater.inflate(R.layout.single_row_contact_add, viewGroup, false);

                Button buaddnew = (Button) row.findViewById(R.id.buaddnew);
                buaddnew.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SettingSaved.WhoFindMeTag=1;
                        Intent newpage=new Intent(  getApplicationContext(),ContactList.class);
                        startActivity(newpage);
                    //  finish();
                    }
                });

                return row;

            }
            else {
                View row = inflater.inflate(R.layout.single_row_conact, viewGroup, false);

                TextView title = (TextView) row.findViewById(R.id.textView1);
                TextView detals = (TextView) row.findViewById(R.id.textView2);
                ImageView image = (ImageView) row.findViewById(R.id.imageView);


                title.setText(temp.Title);
                detals.setText(temp.Detals);// it updated
                image.setImageResource(temp.ImageURL);

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
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) !=
                            PackageManager.PERMISSION_GRANTED  ){
                        requestPermissions(new String[]{
                                        android.Manifest.permission.READ_CONTACTS},
                                REQUEST_CODE_ASK_PERMISSIONS);




                        return null;
                    }}
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
