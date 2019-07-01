package com.example.myapplication2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Locale;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ViewHolder> {

    private ArrayList<Contact> arrayListOfcontactinfo;
    final private Context mcontext;
    private Activity activity;
    private ArrayList<Contact> searchList = new ArrayList<>();
    private Fragment fragment;




    //Adapter 초기화 및 생성자로 받은 데이터기반으로 Adapter 내 데이터 세팅
    public RecyclerViewDataAdapter(Activity activity, Context context, Fragment fragment, ArrayList<Contact> arrayListOfcontactinfo){
        this.mcontext = context;
        this.arrayListOfcontactinfo = arrayListOfcontactinfo;
        this.activity = activity;
        this.fragment = fragment;
    }

    //ViewHolder가 초기화 될 때 혹은 ViewHolder를 초기화 할 때 실행되는 메서드
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Context를 부모로 부터 받아와서
        Context context = parent.getContext() ;

        //받은 Context를 기반으로 LayoutInflater를 생성
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        //생성된 LayoutInflater로 어떤 Layout을 가져와서 어떻게 View를 그릴지 결정
        View contactinfoView = layoutInflater.inflate(R.layout.recyclerview_item, parent, false);

        //View 생성 후, 이 View를 관리하기위한 ViewHolder를 생성
        ViewHolder viewHolder = new ViewHolder(contactinfoView);

        //생성된 ViewHolder를 OnBindViewHolder로 넘겨줌
        return viewHolder;
    }

    //RecyclerView의 Row 하나하나를 구현하기위해 Bind(묶이다) 될 때
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {


        //RecyclerView에 들어갈 Data(Student로 이루어진 ArrayList 배열인 arrayListOfStudent)를 기반으로 Row를 생성할 때
        //해당 row의 위치에 해당하는 Student를 가져와서
        final Contact contact = arrayListOfcontactinfo.get(position);

        //넘겨받은 ViewHolder의 Layout에 있는 View들을 어떻게 다룰지 설정
        //ex. TextView의 text를 어떻게 설정할지, Button을 어떻게 설정할지 등등...
        TextView txtName = holder.txtName;
        txtName.setText(contact.getName());

        TextView txtAddress = holder.txtPhoneNumber;
        txtAddress.setText(contact.getPhoneNumber());

        ImageView imageProfile = holder.imageProfile;
        imageProfile.setImageResource(R.drawable.kakao_1);
        imageProfile.setBackgroundResource(R.drawable.background_rounding);
        imageProfile.setClipToOutline(true);


        imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                activity.startActivityForResult(intent,1);
            }
        });


        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(holder.itemView.getContext(), ProfileActivity.class);
                intent.putExtra("id",contact.getID());
                holder.itemView.getContext().startActivity(intent);
                ((Activity) mcontext).finish();

            }
        });

    }



    //요 메서드가 arrayListOfStudent에 들어있는 Student 개수만큼 카운트해줌
    //요녀석이 있어야 arrayListOfStudent에 넣어준 Student의 데이터를 모두 그릴수 있음
    @Override
    public int getItemCount() {
        return arrayListOfcontactinfo.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        public TextView txtName;
        public TextView txtPhoneNumber;
        public ImageView imageProfile;

        //ViewHolder 생성
        public ViewHolder(View itemView) {
            super(itemView);

            //View를 넘겨받아서 ViewHolder를 완성
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtPhoneNumber = (TextView) itemView.findViewById(R.id.txtPhoneNumber);
            imageProfile = (ImageView) itemView.findViewById(R.id.profileImage);

            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
            MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "Edit");
            MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "Delete");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);

        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                final DBcontactHelper db = new DBcontactHelper(mcontext);

                switch (item.getItemId()) {

                    case 1001:
                        AlertDialog.Builder builder = new AlertDialog.Builder(mcontext);
                        View view = LayoutInflater.from(mcontext)
                                .inflate(R.layout.edit_box, null, false);
                        builder.setView(view);
                        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_update);
                        final EditText editTextName = (EditText) view.findViewById(R.id.edittext_dialog_name);
                        final EditText editTextPN = (EditText) view.findViewById(R.id.edittext_dialog_pn);
                        final EditText editTextAddress = (EditText) view.findViewById(R.id.edittext_dialog_address);
                        final EditText editTextEmail = (EditText) view.findViewById(R.id.edittext_dialog_email);
                        final EditText editTextBirthday = (EditText) view.findViewById(R.id.edittext_dialog_birthday);
                        final EditText editTextMemo = (EditText) view.findViewById(R.id.edittext_dialog_memo);

                        editTextName.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getName());
                        editTextPN.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getPhoneNumber());
                        editTextAddress.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getAddress());
                        editTextEmail.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getEmail());
                        editTextBirthday.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getBirthday());
                        editTextMemo.setText(arrayListOfcontactinfo.get(getAdapterPosition()).getMemo());


                        final AlertDialog dialog = builder.create();
                        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                String strName = editTextName.getText().toString();
                                String strPN = editTextPN.getText().toString();
                                String strAddress = editTextAddress.getText().toString();
                                String strEmail = editTextEmail.getText().toString();
                                String strBirthday = editTextBirthday.getText().toString();
                                String strMemo = editTextMemo.getText().toString();

                                Contact contact = new Contact(arrayListOfcontactinfo.get(getAdapterPosition()).getID(), strName,strPN,strAddress,strEmail,strBirthday,strMemo);
                                db.updateContact(contact);
                                arrayListOfcontactinfo=db.getAllContacts();
                                dialog.dismiss();

                                FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                                ft.detach(fragment).attach(fragment).commit();
                            }
                        });

                        dialog.show();

                        break;


                    case 1002:

                        db.deleteContact(arrayListOfcontactinfo.get(getAdapterPosition()));
                        arrayListOfcontactinfo=db.getAllContacts();
                        FragmentTransaction ft = fragment.getFragmentManager().beginTransaction();
                        ft.detach(fragment).attach(fragment).commit();

                        break;
                }


                return true;
            }
        };
    }

    public void filter(String charText) {
        DBcontactHelper db = new DBcontactHelper(mcontext);
        charText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();
        searchList.addAll(db.getAllContacts());
        arrayListOfcontactinfo.clear();
        if (charText.length() == 0){
            arrayListOfcontactinfo.addAll(db.getAllContacts());
        } else{
            for (Contact contact : searchList){
                String name = contact.getName();
                    if (name.toLowerCase().contains(charText)){
                        arrayListOfcontactinfo.add(contact);
                }
            }
        }
        notifyDataSetChanged();
    }





}

