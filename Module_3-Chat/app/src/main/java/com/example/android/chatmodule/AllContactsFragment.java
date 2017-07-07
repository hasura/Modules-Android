package com.example.android.chatmodule;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import io.hasura.sdk.Callback;
import io.hasura.sdk.Hasura;
import io.hasura.sdk.HasuraClient;
import io.hasura.sdk.exception.HasuraException;

import static android.support.v4.content.PermissionChecker.checkSelfPermission;

/**
 * Created by amogh on 6/7/17.
 */

public class AllContactsFragment extends Fragment{

    View parentViewHolder;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    AllContactsAdapter adapter;

    HasuraClient client = Hasura.getClient();

    Context context;

    int userId;

    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    public void showContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(context,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, PERMISSIONS_REQUEST_READ_CONTACTS);
        } else {
            Background background = new Background(new Background.Results() {
                @Override
                public void getResult(List<Contact> contactList) {
                    adapter.setContacts(contactList);
                }
            });
            background.execute(context);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_READ_CONTACTS) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted
                showContacts();
            } else {
                Toast.makeText(context, "Until you grant the permission, we canot display the names", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        parentViewHolder = inflater.inflate(R.layout.fragment_allcontacts, container, false);

        context = parentViewHolder.getContext();

        recyclerView = (RecyclerView) parentViewHolder.findViewById(R.id.all_contacts_recyclerview);

        linearLayoutManager = new LinearLayoutManager(context);
        adapter = new AllContactsAdapter(new AllContactsAdapter.Interactor() {
            @Override
            public void onContactClicked(Contact contact, int position) {
                String mobile;
                mobile = contact.getMobile();
                //Checking if contact exists
                checkForUser(contact.getName(),mobile);
            }
        });
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);



        showContacts();

        return parentViewHolder;
    }

    public int checkForUser(final String name, String mobile){
        String[] temp;
        String cell = mobile.replaceAll("[+-/]","");
        if(cell.contains(" ")){
            temp = cell.split(" ");
            cell = temp[0];
            cell = cell + temp[1];
        }
        cell = cell.substring(cell.length()-10,cell.length());

        client.useDataService()
                .setRequestBody(new CheckUserQuery(cell))
                .expectResponseTypeArrayOf(UserDetails.class)
                .enqueue(new Callback<List<UserDetails>, HasuraException>() {
                    @Override
                    public void onSuccess(List<UserDetails> userDetails) {
                        if(userDetails.size() > 0)
                            userId = userDetails.get(0).getId();
                        if(userId > 0){
                            Global.receiverId = userId;
                            Intent i = new Intent(context,ChattingActivity.class);
                            startActivity(i);
                            getActivity().finish();
                        }else
                            Toast.makeText(context, name + " does not use this app", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(HasuraException e) {
                        Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
        Log.i("Mobile", mobile);
        return 1;
    }
}

class Background extends AsyncTask<Context, Integer, List<Contact>> {

    List<Contact> contacts = new ArrayList<>();
    Context context;

    Results results;

    public Background(Results results){
        this.results = results;
    }

    @Override
    protected List<Contact> doInBackground(Context... params) {
        context = params[0];
        contacts = getAllContacts(params[0]);
        return null;
    }

    protected void onPostExecute(List<Contact> contactList) {
        results.getResult(contacts);
    }


    public List<Contact> getAllContacts(Context context){


        ContentResolver cr = context.getContentResolver();
        Cursor managedCursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null,null);


        if (managedCursor.moveToFirst()){

            do {

                String id = managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.Contacts._ID));

                if(Integer.parseInt(managedCursor.getString(managedCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        Contact contact = new Contact();

                        String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        contact.setMobile(contactNumber);
                        contact.setName(contactName);
                        contacts.add(contact);
                        break;
                    }
                    pCur.close();
                }
            }while (managedCursor.moveToNext());
        }
        return contacts;
    }

    public interface Results{
        void getResult(List<Contact> contactList);
    }

}
