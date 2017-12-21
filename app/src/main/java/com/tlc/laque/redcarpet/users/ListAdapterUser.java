package com.tlc.laque.redcarpet.users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tlc.laque.redcarpet.R;


import java.util.List;

/**
 * Created by User on 07/12/2017.
 */
public class ListAdapterUser extends ArrayAdapter<User> {
    private ImageView image;


public ListAdapterUser(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        }

public ListAdapterUser(Context context, int resource, List<User> items) {
        super(context, resource, items);
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_list_user, null);
        }

        User u = getItem(position);

        if (u != null) {
            TextView txtManeParty = v.findViewById(R.id.textViewNamaUserList);
            TextView txtNumberUsers =  v.findViewById(R.id.textViewNumFriends);
            TextView txtLocationUsers =  v.findViewById(R.id.textViewLocationUser);
            image = v.findViewById(R.id.imageViewUserAdapter);

            if (txtManeParty != null) {
                txtManeParty.setText(u.getNickname());
            }
            if (txtNumberUsers != null) {
                    txtNumberUsers.setText(u.getNumberFriends()+"");
                }

            if(txtLocationUsers != null){
                if(u.getPrivacy().equalsIgnoreCase("No one")){}
                else if(u.getPrivacy() == null){
                    txtLocationUsers.setText(u.getLocation()+"");}
                else if(u.getPrivacy().equalsIgnoreCase("Only Friends") && u.isFriendOrNot()){
                    txtLocationUsers.setText(u.getLocation()+"");}
                else if(u.getPrivacy().equalsIgnoreCase("Everyone"))
                    txtLocationUsers.setText(u.getLocation()+"");
            }
        }

        return v;
        }
}