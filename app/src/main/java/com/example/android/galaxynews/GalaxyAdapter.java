package com.example.android.galaxynews;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class GalaxyAdapter extends ArrayAdapter<Galaxy>{

    public GalaxyAdapter(@NonNull Context context, ArrayList <Galaxy> galaxies) {
        super(context, 0, galaxies);


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }
        // Get the {@link Galaxy} object located at this position in the list
        Galaxy currentGalaxy = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID news_title.
        TextView titleTextView = listItemView.findViewById(R.id.news_title);
        String newsTitle = (currentGalaxy.getDefaultTitle());
        // Set text to currentGalaxy object
        titleTextView.setText(newsTitle);

        // Find the TextView in the list_item.xml layout with the ID section.
        TextView sectionTextView = (TextView) listItemView.findViewById(R.id.section);
        String sectionTitle = (currentGalaxy.getSectionName());
        // Set text to currentGalaxy object
        sectionTextView.setText(sectionTitle);

        // Find the TextView with view ID date
        TextView dateView = (TextView) listItemView.findViewById(R.id.publication_date);

        // Display the category of the current news in that TextView
        SimpleDateFormat dateFormatJSON = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("EE dd MMM yyyy", Locale.ENGLISH);

        try {
            Date dateNews = dateFormatJSON.parse(currentGalaxy.getDefaultDate());

            String date = dateFormat2.format(dateNews);
            dateView.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find the TextView in the list_item.xml layout with the ID section.
        TextView authorTextView = (TextView) listItemView.findViewById(R.id.author);
        String author = (currentGalaxy.getAuthor());
        // Set text to currentGalaxy object
        authorTextView.setText(author);


        return listItemView;

    }


    }




