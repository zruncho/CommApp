package com.cryptophonecall.cv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CustomArrayAdapterCallDB extends ArrayAdapter {
        List<CallDB> ingredientsList;
        Context cnt ;
        public CustomArrayAdapterCallDB(Context context, List<CallDB> list)
        {
            super(context,0,list);
            ingredientsList = list;
            cnt = context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            LayoutInflater inflater = (LayoutInflater) cnt.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.calldb_row, parent,false);
// inflate custom layout called row
                holder = new ViewHolder();
                holder.inout =(ImageView) convertView.findViewById(R.id.callImageView1);
                holder.date =(TextView) convertView.findViewById(R.id.calltextView1);
                holder.number =(TextView) convertView.findViewById(R.id.calltextView2);
// initialize textview
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            CallDB in = (CallDB)ingredientsList.get(position);
            int img;
            if(in.outin)
            {img=R.drawable.outgoing; }else{img=R.drawable.incomming;}
            holder.inout.setImageResource(img);
            holder.date.setText(in.date);
            holder.number.setText(in.number);
            // set the name to the text;

            return convertView;

        }

        static class ViewHolder
        {
            ImageView inout;
            TextView date;
            TextView number;
        }
    }
