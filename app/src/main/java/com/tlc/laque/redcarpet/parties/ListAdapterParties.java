package com.tlc.laque.redcarpet.parties;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tlc.laque.redcarpet.R;

import java.io.File;
import java.util.List;

/**
 * Created by User on 06/12/2017.
 * Adapter for the ListView Parties
 */
public class ListAdapterParties extends ArrayAdapter<Party> {
    private ImageView image;
    private File tmpFile;
    public ListAdapterParties(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapterParties(Context context, int resource, List<Party> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.adapter_party_list, null);
        }

        Party p = getItem(position);

        if (p != null) {
            TextView txtManeParty = v.findViewById(R.id.textViewNamePartyList);
            TextView txtLocationParty = v.findViewById(R.id.textViewLocationPartyList);
            TextView txtNumberUsers =  v.findViewById(R.id.textViewNumAttending);
            image = v.findViewById(R.id.imageViewUserAdapter);
            setImage(p);
            ///Populate the TextViews
            if (txtManeParty != null) {
                if(p.isPartyStarted()){
                    txtManeParty.setText(p.getName() + "\n (NOW)");
                }else
                txtManeParty.setText(p.getName());
            }
            if (txtLocationParty != null) {
                txtLocationParty.setText(p.getLocation());
            }
            if (txtNumberUsers != null) {
                txtNumberUsers.setText(p.getNumUserAttending()+"");
            }



        }

        return v;
    }

        //Get Image from FireBase Storage and put in the ImageView
        private void setImage (Party p){
                    Picasso.with(getContext()).load( p.getUrl()).fit().centerCrop()
                            .placeholder(R.drawable.progress_animation)
                            .error(R.drawable.error_download)
                            .into(image);
        }


}
