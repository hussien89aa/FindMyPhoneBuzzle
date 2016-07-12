package phonelocation.example.asuss550c.phonelocation;

import android.content.Context;
import android.database.Cursor;

import android.provider.ContactsContract;

public class ManagmentOperations {

     public static String FormatPhoneNumber(String Oldnmber){
      try{
          String numberOnly= Oldnmber.replaceAll("[^0-9]", "");
           return(numberOnly);
      }
      catch (Exception ex){
          return(" ");
      }
    }
    public static boolean IsPhoneAut(String PhoneNumber,String ListPhone){  // return index of phone if avaulbe true
    boolean IsFound=true;


        String[] MyPhones = ListPhone.split(":");

        for(int i=0;i<MyPhones.length;i++ ) {
            if((PhoneNumber.contains(MyPhones[i])  || MyPhones[i].contains(PhoneNumber))&& MyPhones[i].length()>0)
            {
                IsFound=false;
                break;
            }
        }

        return(IsFound);
    }
    public static   String NumberToName(Context context,String number){

        String UsernName=number ;
        try {


        Cursor cursor = context.getContentResolver().query( ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,null, null);

        while (cursor.moveToNext()) {
            String name =cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

           String phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            phoneNumber= ManagmentOperations.FormatPhoneNumber(phoneNumber);
if ( phoneNumber.contains(number) || number.contains(phoneNumber) ){
    UsernName= name;
    break;
}
        }
        }catch (Exception ex){}

        return UsernName;
    }

}
