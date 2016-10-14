package com.li.videoapplication.data.danmuku;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.List;

/**
 * 接口：解析弹幕XML数据
 */
public class DanmukuListXmlParser implements AbsXmlParser<DanmukuListEntity> {

    private String tag = this.getClass().getSimpleName();

    @Override
    public DanmukuListEntity parse(String xml) throws Exception {
        if (xml == null)
            return null;
        InputStream is = new ByteArrayInputStream(xml.getBytes("UTF-8"));

        DanmukuListEntity entity = new DanmukuListEntity();
        List<DanmukuEntity> entities = entity.getData();
        DanmukuEntity danmukuEntity = null;

        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(is, "UTF-8");
        int eventType = parser.getEventType();
        Log.i(tag, "parse/eventType=" + eventType);

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;

                case XmlPullParser.END_DOCUMENT:
                    break;

                case XmlPullParser.START_TAG:
                    Log.i(tag, "START_TAG/name=" + parser.getName());
                    Log.i(tag, "START_TAG/text=" + parser.getText());
                    danmukuEntity = new DanmukuEntity();
                    if (parser.getName().equals(DanmukuEntity.D)) {// d
                        if (parser.getAttributeName(0).equals(DanmukuEntity.P)) {// p
                            danmukuEntity = new DanmukuEntity();
                            danmukuEntity.setP(parser.getAttributeName(0));
                        }
                        danmukuEntity.setText(parser.getText());
                    } else if (parser.getName().equals(DanmukuListEntity.CHATSERVER)) {
                        entity.chatserver = parser.getText();
                    } else if (parser.getName().equals(DanmukuListEntity.CHATID)) {
                        entity.chatid = parser.getText();
                    } else if (parser.getName().equals(DanmukuListEntity.MISSION)) {
                        entity.mission = parser.getText();
                    } else if (parser.getName().equals(DanmukuListEntity.MAXLIMIT)) {
                        entity.maxlimit = parser.getText();
                    } else if (parser.getName().equals(DanmukuListEntity.SOURCE)) {
                        entity.source = parser.getText();
                    }
                    eventType = parser.next();
                    break;

                case XmlPullParser.END_TAG:
                    Log.i(tag, "END_TAG/name=" + parser.getName());
                    Log.i(tag, "END_TAG/text=" + parser.getText());

                    if (parser.getName().equals(DanmukuEntity.P)) {
                        entities.add(danmukuEntity);
                        danmukuEntity = null;
                    }
                    break;
            }
            eventType = parser.next();
        }
        Log.i(tag, "entity=" + entity);
        return entity;
    }

    @Override
    public String serialize(DanmukuListEntity entity) throws Exception {
        Log.i(tag, "serialize/entity=" + entity);
        XmlSerializer serializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        serializer.setOutput(writer);

        /**
         * i
         */
        serializer.startDocument("UTF-8", true);
        serializer.startTag("", DanmukuListEntity.I);

        /**
         * chatserver
         */
        serializer.startTag("", DanmukuListEntity.CHATSERVER);
        serializer.text(entity.chatserver);
        serializer.endTag("", DanmukuListEntity.CHATSERVER);

        /**
         * chatid
         */
        serializer.startTag("", DanmukuListEntity.CHATID);
        serializer.text(entity.chatid);
        serializer.endTag("", DanmukuListEntity.CHATID);

        /**
         * mission
         */
        serializer.startTag("", DanmukuListEntity.MISSION);
        serializer.text(entity.mission);
        serializer.endTag("", DanmukuListEntity.MISSION);

        /**
         * maxlimit
         */
        serializer.startTag("", DanmukuListEntity.MAXLIMIT);
        serializer.text(entity.maxlimit);
        serializer.endTag("", DanmukuListEntity.MAXLIMIT);

        /**
         * source
         */
        serializer.startTag("", DanmukuListEntity.SOURCE);
        serializer.text(entity.source);
        serializer.endTag("", DanmukuListEntity.SOURCE);

        for (DanmukuEntity e : entity.getData()) {

            /**
             * d
             */
            serializer.startTag("", DanmukuEntity.D);
            serializer.attribute("", DanmukuEntity.P, e.getP());
            serializer.text(e.getText());
            serializer.endTag("", DanmukuEntity.D);
        }

        /**
         * i
         */
        serializer.endTag("", DanmukuListEntity.I);
        serializer.endDocument();
        Log.i(tag, "serialize/writer=" + writer.toString());
        return writer.toString();
    }
}
