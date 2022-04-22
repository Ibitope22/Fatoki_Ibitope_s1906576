package com.example.name_jhondoe_1234;

import android.util.Log;

import com.example.name_jhondoe_1234.Model.DataItem;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class XmlPullParserHandler {
    private List<DataItem> DataItemsList = new ArrayList<DataItem>();
    private DataItem item;
    private String text;

    public List<DataItem> getEmployees() {
        return DataItemsList;
    }

    public List<DataItem> parse(InputStream is) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser parser = factory.newPullParser();

            parser.setInput(is, null);
            int eventType = parser.getEventType();
            boolean isStarted=true;
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagname = parser.getName();
//                Log.d("tagnameTesting",tagname+"nul");
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (tagname.equalsIgnoreCase("item")) {
                            // create a new instance of employee
                            item = new DataItem();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                    {
                        Log.d("tagnameTesting","nul");
                        if (tagname.equalsIgnoreCase("item")) {
                            // add employee object to list
                            Log.d("goingToreutrn",item.toString());
                            DataItemsList.add(item);
                        } else if (tagname.equalsIgnoreCase("author")) {
                            item.setAuthor(text);
                        } else if (tagname.equalsIgnoreCase("category")) {
                            item.setCategory1(text);
                        } else if (tagname.equalsIgnoreCase("description")) {
                            if(isStarted){
                                item = new DataItem();
                                isStarted=false;
                            }
                            item.setDescription(text);
                        } else if (tagname.equalsIgnoreCase("guid")) {
                            item.setGuid(text);
                        }else if (tagname.equalsIgnoreCase("link")) {
                            item.setLink(text);
                        }else if (tagname.equalsIgnoreCase("pubDate")) {
                            item.setPubDate(text);
                        }else if (tagname.equalsIgnoreCase("title")) {
                            item.setTitle(text);
                        }else if (tagname.equalsIgnoreCase("reference")) {
                            item.setReference(text);
                        }else if (tagname.equalsIgnoreCase("road")) {
                            item.setRoad(text);
                        }else if (tagname.equalsIgnoreCase("region")) {
                            item.setRegion(text);
                        }else if (tagname.equalsIgnoreCase("county")) {
                            item.setCounty(text);
                        }else if (tagname.equalsIgnoreCase("latitude")) {
                            item.setLatitude(text);
                        }else if (tagname.equalsIgnoreCase("longitude")) {
                            item.setLongitude(text);
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = parser.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("dataITem",e.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("dataITem",e.toString());
        }

        Log.d("goingToreutrn",DataItemsList.size()+"");
        return DataItemsList;
    }
}