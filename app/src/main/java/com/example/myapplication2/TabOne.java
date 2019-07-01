package com.example.myapplication2;


import android.app.AlertDialog;
import android.database.Cursor;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Locale;

public class TabOne extends Fragment{
    private ArrayList<Contact> ArrayListOfEdit = new ArrayList<>();
    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        final Fragment fragment = this;

        final DBcontactHelper db = new DBcontactHelper(getContext());
        ArrayListOfEdit=db.getAllContacts();
        final RecyclerViewDataAdapter recyclerDataAdapter = new RecyclerViewDataAdapter(getActivity(),getContext(), fragment, ArrayListOfEdit);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setAdapter(recyclerDataAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        


        final EditText search = (EditText) view.findViewById(R.id.searchEdit);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = search.getText().toString()
                        .toLowerCase(Locale.getDefault());
                recyclerDataAdapter.filter(text);
            }
        });


        ImageView buttonLoading = (ImageView) view.findViewById(R.id.loadingContacts);
        buttonLoading.setImageResource(R.drawable.down);
        buttonLoading.setBackground(new ShapeDrawable(new OvalShape()));
        buttonLoading.setClipToOutline(true);
        buttonLoading.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Cursor c =  getContext().getContentResolver().query(
                        ContactsContract.CommonDataKinds
                                .Phone.CONTENT_URI,  // 조회할 컬럼명
                        null, // 조회할 컬럼명
                        null, // 조건 절
                        null, // 조건절의 파라미터
                        null);// 정렬 방향

                c.moveToFirst(); // 커서를 처음위치로 이동시킴
                String name;
                String phoneNumber;

                do {
                    name = c.getString
                            (c.getColumnIndex(ContactsContract
                                    .CommonDataKinds.Phone.DISPLAY_NAME));
                    phoneNumber = c.getString
                            (c.getColumnIndex(ContactsContract
                                    .CommonDataKinds.Phone.NUMBER));

                    Contact contact = new Contact();
                    contact.setName(name);
                    contact.setPhoneNumber(phoneNumber);
                    db.addContact(contact);
                } while (c.moveToNext());
                ArrayListOfEdit=db.getAllContacts();
                RecyclerViewDataAdapter recyclerDataAdapter = new RecyclerViewDataAdapter(getActivity(),getContext(),fragment,ArrayListOfEdit);
                RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                recyclerView.setAdapter(recyclerDataAdapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            }

        });




        Button buttonInsert = (Button) view.findViewById(R.id.addContact);
        buttonInsert.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View view = LayoutInflater.from(getContext()).inflate(R.layout.edit_box, null, false);
                builder.setView(view);

                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);
                final EditText editTextName = (EditText) view.findViewById(R.id.edittext_dialog_name);
                final EditText editTextPN = (EditText) view.findViewById(R.id.edittext_dialog_pn);
                final EditText editTextAddress = (EditText) view.findViewById(R.id.edittext_dialog_address);
                final EditText editTextEmail = (EditText) view.findViewById(R.id.edittext_dialog_email);
                final EditText editTextBirthday = (EditText) view.findViewById(R.id.edittext_dialog_birthday);
                final EditText editTextMemo = (EditText) view.findViewById(R.id.edittext_dialog_memo);

                ButtonSubmit.setText("Update");

                final AlertDialog dialog = builder.create();

                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String strName = editTextName.getText().toString();
                        String strPN = editTextPN.getText().toString();
                        String strAddress = editTextAddress.getText().toString();
                        String strEmail = editTextEmail.getText().toString();
                        String strBirthday = editTextBirthday.getText().toString();
                        String strMemo = editTextMemo.getText().toString();

                        Contact contact = new Contact(strName,strPN, strAddress,strEmail,strBirthday,strMemo);
                        db.addContact(contact);
                        ArrayListOfEdit=db.getAllContacts();
                        dialog.dismiss();
                        RecyclerViewDataAdapter recyclerDataAdapter = new RecyclerViewDataAdapter(getActivity(),getContext(),fragment,ArrayListOfEdit);
                        RecyclerView recyclerView = (RecyclerView) getView().findViewById(R.id.recyclerView);
                        recyclerView.setAdapter(recyclerDataAdapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                });

                dialog.show();
            }
        });

        return view;
    }
}
