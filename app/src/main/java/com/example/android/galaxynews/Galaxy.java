package com.example.android.galaxynews;

public class Galaxy {

    //default translation of title of galaxyArticle
    private String mDefaultTitle;
    //default translation of date of galaxyArticle
    private String mDate;
    //default translation of sectionName of galaxyArticle
    private String mSectionName;
    //default translation of webUrl of galaxyArticle
    private String mUrl;
    //Author or no? Display
    private String mAuthor;

    //* Create a new Galaxy object.

    public Galaxy(String defaultTitle, String date, String sectionName, String url, String author) {
        mDefaultTitle= defaultTitle;
        mDate = date;
        mSectionName = sectionName;
        mUrl = url;
        mAuthor = author;
    }

    /**
     * Get the default of the Galaxytitle.
     */
    public String getDefaultTitle() {
        return mDefaultTitle;
    }

    /**
     * Get the default of the date.
     */
    public String getDefaultDate() {
        return mDate;
    }

    /**
     * Get the default of the sectionName.
     */
    public String getSectionName() {
        return mSectionName;
    }

    public String getUrl(){
        return mUrl;
    }

    public String getAuthor(){return mAuthor;}


}
