package com.example.mynewsapplication;

public class MyNews {

    private String mSectionName;
    private String mNewsTitle;
    private String mPublicationDate;
    private String mUrl;

    /**
     * Constructs a new MyNews object
     */

    public MyNews (String sectionName, String newsTitle, String publicationDate, String url){
        mSectionName = sectionName;
        mNewsTitle = newsTitle;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    /**
     * Returns section name
     *
     */
    public String getSectionName(){
        return mSectionName;
    }

    /**
     * Returns the news title
     *
     */
    public String getNewsTitle(){
        return mNewsTitle;
    }

    /**
     * Returns publication date
     *
     */
    public String getPublicationDate(){
        return mPublicationDate;
    }

    /**
     * Returns url
     *
     */
    public String getUrl(){
        return mUrl;
    }

}
