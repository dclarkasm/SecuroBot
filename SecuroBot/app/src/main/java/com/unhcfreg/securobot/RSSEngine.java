package com.unhcfreg.securobot;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

/**
 * Created by Devon on 7/6/2015.
 * source code adapted from: http://www.tutorialspoint.com/android/android_rss_reader.htm
 */
public class RSSEngine {
    private String author = null;   //RSS feed channel name
    private String title = "title";     //feed title (title of article
    private String link = "link";       //associated link to article
    private String description = "description"; //article preface (String HTML format)
    private String urlString = null;
    private XmlPullParserFactory xmlFactoryObject;
    public volatile boolean processing = false;
    private boolean processingFailure = false;
    private HashMap<String, String> finalURL = new HashMap<>();
    private String keys[];
    Random r = new Random();

    public RSSEngine(String url) {
        finalURL.put("Naked Security", "https://nakedsecurity.sophos.com/feed/");
        finalURL.put("SANS Institute", "https://www.sans.org/webcasts/rss/");
        finalURL.put("The Register", "http://www.theregister.co.uk/headlines.rss");
        keys = finalURL.keySet().toArray(new String[finalURL.keySet().size()]); //convert the set of keys to an array of strings

        if(url==null) { //set a random url for the search if no url specified
            int rn = r.nextInt(keys.length-0);
            urlString = finalURL.get(keys[rn]);
            author = keys[rn];
        }
        else urlString = url;
    }

    public String getTitle(){
        return title;
    }

    public String getLink(){
        return link;
    }

    public String getAuthor(){
        return author;
    }

    public String getDescription(){
        return description;
    }

    public boolean isProcessingFailure() {
        return processingFailure;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text=null;
        boolean foundChannel=false, foundAuthor=false, foundItem=false,
                foundTitle=false, foundDescription=false;

        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG: {
                        if(name.equals("channel")) {
                            foundChannel=true;
                            Log.d("RSS", "found channel");
                        }
                        else if(name.equals("item") && foundChannel && foundAuthor && !foundItem) {
                            foundItem = true;
                            Log.d("RSS", "found item");
                        }
                        break;
                    }
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                    {
                        if(name.equals("title") && foundChannel && !foundAuthor) {
                            if(author==null) author = text; //if the author was not set on initialization
                            foundAuthor = true;
                            Log.d("RSS", "found Author");
                        }
                        else if(name.equals("title") && foundChannel && foundAuthor &&
                                foundItem && !foundTitle) {
                            title = text;
                            foundTitle = true;
                            Log.d("RSS", "found title" );
                        }
                        else if(name.equals("description") && foundChannel && foundAuthor &&
                                foundItem && foundTitle && !foundDescription) {
                            description = text;
                            foundDescription = true;
                            //TODO: add code here to process the string description
                        }
                        break;
                    }
                }
                if(foundAuthor && foundChannel && foundDescription && foundItem && foundTitle) break;   //break if we found all the components to the latest RSS feed
                event = myParser.next();
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchXML(){
        processing = true;
        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {

                try {
                    URL url = new URL(urlString);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setReadTimeout(10000 /* milliseconds */);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);

                    // Starts the query
                    conn.connect();
                    InputStream stream = conn.getInputStream();

                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(stream, null);

                    parseXMLAndStoreIt(myparser);
                    stream.close();
                    processing = false;
                }

                catch (Exception e) {
                    processing = false;
                    processingFailure = false;
                }
                processing = false;
            }
        });
        thread.start();
    }

}
