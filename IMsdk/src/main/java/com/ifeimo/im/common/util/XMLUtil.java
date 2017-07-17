package com.ifeimo.im.common.util;

import com.ifeimo.im.common.bean.xml.PresenceList;

import org.jivesoftware.smack.packet.Presence;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by lpds on 2017/6/16.
 */
public class XMLUtil {

    public static String simpleXML(String xml,String key){
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            //设置输入的内容
            xmlPullParser.setInput(new StringReader(xml));

            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagNameString = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (key.equals(tagNameString)) {//Book标签
                            return xmlPullParser.nextText();
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();//重新赋值，不然会死循环
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return "";
    }

    @Deprecated
    public static <T> T xmlDissect(String xml,Class<T> tClass) throws IllegalAccessException, InstantiationException {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            //设置输入的内容
            xmlPullParser.setInput(new StringReader(xml));
            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagNameString = xmlPullParser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();//重新赋值，不然会死循环
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return tClass.newInstance();

    }

    /**
     * 教练列表
     * @param xml
     * @return
     */
    public static PresenceList convertCoachByXMl(String xml) {
        PresenceList presenceList = new PresenceList();
        try {


            PresenceList.Presence presence = null;

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            //设置输入的内容
            xmlPullParser.setInput(new StringReader(xml));

            int eventType = xmlPullParser.getEventType();

            String tagNameString = "";
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagNameString = xmlPullParser.getName();
                        switch (tagNameString) {
                            case "presence":
                                presence = new PresenceList.Presence();
                                for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
//                                    System.out.println("*********"+xmlPullParser.getAttributeName(i));
                                    switch (xmlPullParser.getAttributeName(i)) {
                                        case "from":
                                            presence.setFrom(xmlPullParser.getAttributeValue(i));
                                            break;
                                        case "id":
                                            presence.setId(xmlPullParser.getAttributeValue(i));
                                            break;
                                        case "to":
                                            presence.setTo(xmlPullParser.getAttributeValue(i));
                                            break;
                                        case "type":
                                            presence.setType(Presence.Type.fromString(xmlPullParser.getAttributeValue(i)));
                                    }
                                }
                                if (presence.getType() == null) {
                                    presence.setType(Presence.Type.available);
                                }
                        }
                        break;
                    case XmlPullParser.TEXT:
                        if (!StringUtil.isNull(tagNameString)) {
                            switch (tagNameString) {
                                case "show":
                                    presence.setShow(Presence.Mode.fromString(xmlPullParser.getText()));
                                    break;
                                case "priority":
                                    presence.setPriority(xmlPullParser.getText());
                                    break;
                                case "status":
                                    presence.setStatus(xmlPullParser.getText());
                                    break;
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        tagNameString = xmlPullParser.getName();
                        switch (tagNameString) {
                            case "presence":
                                presenceList.getPresences().add(presence);
                                presence = null;
                                break;
                        }
                        tagNameString = "";
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();//重新赋值，不然会死循环
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return presenceList;
    }

}
