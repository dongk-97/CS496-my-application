package com.example.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        Intent intent = getIntent();
        final int data = intent.getIntExtra("id",0);
        final DBcontactHelper db = new DBcontactHelper(this);
        final Contact contact = db.getContact(data);
        final TextView nameContent = (TextView) findViewById(R.id.nameContent);
        final TextView phonenumberContent = (TextView) findViewById(R.id.phonenumberContent);
        final TextView addressContent = (TextView) findViewById(R.id.addressContent);
        final TextView emailContent = (TextView) findViewById(R.id.emailContent);
        final TextView birthdayContent = (TextView) findViewById(R.id.birthdayContent);
        final TextView memoContent = (TextView) findViewById(R.id.memoContent);
        final ImageView profileImage = (ImageView) findViewById(R.id.profileImageInProfile);

        nameContent.setText(contact.getName());
        phonenumberContent.setText(contact.getPhoneNumber());
        addressContent.setText(contact.getAddress());
        emailContent.setText(contact.getEmail());
        birthdayContent.setText(contact.getBirthday());
        memoContent.setText(contact.getMemo());
        profileImage.setImageResource(R.drawable.kakao_1);
        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
        profileImage.setClipToOutline(true);

        phonenumberContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = "tel:" + contact.getPhoneNumber();
                startActivity(new Intent("android.intent.action.DIAL", Uri.parse(tel)));
            }
        });

        ImageView phoneCall = (ImageView) findViewById(R.id.phoneImage);
        phoneCall.setImageResource(R.drawable.phoneimage);
        phoneCall.setBackground(new ShapeDrawable(new OvalShape()));
        phoneCall.setClipToOutline(true);

        phoneCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = "tel:" + contact.getPhoneNumber();
                startActivity(new Intent("android.intent.action.CALL", Uri.parse(tel)));
            }
        });

        ImageView message = (ImageView) findViewById(R.id.messageImage);
        message.setImageResource(R.drawable.message);
        message.setBackground(new ShapeDrawable(new OvalShape()));
        message.setClipToOutline(true);

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tel = "smsto:" + contact.getPhoneNumber();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(tel));
                startActivity(intent);

            }
        });





        ImageView backImage = (ImageView) findViewById(R.id.backImage);
        backImage.setImageResource(R.drawable.roundarrow);
        backImage.setBackground(new ShapeDrawable(new OvalShape()));
        backImage.setClipToOutline(true);

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        ImageView editImage = (ImageView) findViewById(R.id.editImage);
        editImage.setImageResource(R.drawable.edit);
        editImage.setBackground(new ShapeDrawable(new OvalShape()));
        editImage.setClipToOutline(true);

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                View view = LayoutInflater.from(ProfileActivity.this)
                        .inflate(R.layout.edit_box, null, false);
                builder.setView(view);
                final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);
                final EditText editTextName = (EditText) view.findViewById(R.id.edittext_dialog_name);
                final EditText editTextPN = (EditText) view.findViewById(R.id.edittext_dialog_pn);
                final EditText editTextAddress = (EditText) view.findViewById(R.id.edittext_dialog_address);
                final EditText editTextEmail = (EditText) view.findViewById(R.id.edittext_dialog_email);
                final EditText editTextBirthday = (EditText) view.findViewById(R.id.edittext_dialog_birthday);
                final EditText editTextMemo = (EditText) view.findViewById(R.id.edittext_dialog_memo);

                editTextName.setText(contact.getName());
                editTextPN.setText(contact.getPhoneNumber());
                editTextAddress.setText(contact.getAddress());
                editTextEmail.setText(contact.getEmail());
                editTextBirthday.setText(contact.getBirthday());
                editTextMemo.setText(contact.getMemo());

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

                        Contact contact = new Contact(data, strName, strPN, strAddress, strEmail, strBirthday, strMemo);
                        db.updateContact(contact);
                        nameContent.setText(contact.getName());
                        phonenumberContent.setText(contact.getPhoneNumber());
                        addressContent.setText(contact.getAddress());
                        emailContent.setText(contact.getEmail());
                        birthdayContent.setText(contact.getBirthday());
                        memoContent.setText(contact.getMemo());
                        profileImage.setImageResource(R.drawable.kakao_1);
                        profileImage.setBackground(new ShapeDrawable(new OvalShape()));
                        profileImage.setClipToOutline(true);
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
