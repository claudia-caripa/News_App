package com.example.mynewsapplication;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link MyNewsAdapter} knows how to create a list item layout for each news
 * in the data source (a list of {@link MyNews} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class MyNewsAdapter extends ArrayAdapter<MyNews> {

    private static final String DATE_SEPARATOR = "T";

    /**
     * Constructs a new {@link MyNewsAdapter}
     */
    public MyNewsAdapter(Context context, List<MyNews> myNews) {
        super(context, 0, myNews);
    }

    /**
     * Returns a list item view that displays information about the news at the given position
     * in the list of news.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (convertView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the news at the given position in the list of news
        MyNews currentNews = getItem(position);

        // Find the TextView with view ID sectionName
        TextView sectionNameView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionNameView.setText(currentNews.getSectionName());

        // Find the TextView with view ID newsTitle
        TextView webTitleView = (TextView) listItemView.findViewById(R.id.newsTitle);
        webTitleView.setText(currentNews.getNewsTitle());

        /**
         * OJO
          */
        TextView authorNameView = (TextView) listItemView.findViewById(R.id.authorInformation);
        if (currentNews.getAuthorName() != null){
            authorNameView.setText(currentNews.getAuthorName());
            authorNameView.setVisibility(View.VISIBLE);
        } else {
            authorNameView.setVisibility(View.GONE);
        }


        // Create a new Date object from the publication date of the news
        //Date dateObject= new Date(currentNews.getPublicationDate());
        String dateString = currentNews.getPublicationDate();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        Date date = null;
        try {
            date = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Find the TextView with view ID date
        TextView publicationDateView = (TextView) listItemView.findViewById(R.id.date);
        // Format the date string (i.e. "Mar 3, 1984")
        String formattedDate = formatDate(date);
        // Display the date of the current news in that TextView
        publicationDateView.setText(formattedDate);

        // Find the TextView with view ID time
        TextView publicationTimeView = (TextView) listItemView.findViewById(R.id.time);
        // Format the date string (i.e. "1:32PM")
        String formattedTime = formatTime(date);
        // Display the date of the current news in that TextView
        publicationTimeView.setText(formattedTime);

        return listItemView;
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

}
