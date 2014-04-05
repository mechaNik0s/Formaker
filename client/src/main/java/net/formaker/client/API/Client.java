package net.formaker.client.API;

import android.content.Context;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;

/**
 * Created by NikOS on 05.04.2014.
 */
public class Client {

    public String token;
    public boolean isAuthenticate;
    public String password;
    public String id;
    public String message;
    private Context context;

    public Client(Context context, String id, String password) {
        this.message = "";
        this.context = context;
        this.id = id;
        this.password = password;
    }

    public void login() {
        httpGetter getter = new httpGetter("http://formaker.net/public/api/form?method=login&id=" +
                id + "&password=" + password);
        if (getter.send()) {

            String xml = getter.response;

            if (this.getToken(xml)) {
                this.isAuthenticate = true;
            } else {
                this.isAuthenticate = false;
            }
        } else {
            this.isAuthenticate = false;
            message = getter.message;
        }
    }

    public boolean getForm() {
        httpGetter getter = new httpGetter("http://formaker.net/public/api/form?method=get&token=" + this.token);
        if (getter.send()) {
            String xml = getter.response;
            XMLFormParser parser = new XMLFormParser(this.context);
            if (parser.getFormModel(xml)) {
                return true;
            } else {
                message = parser.message;
                return false;
            }
        } else {
            message = getter.message;
            return false;
        }
    }
    /*public boolean sendFormResults(String formId) {
        return true;
	}*/

    private boolean getToken(String xml) {
        try {
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp;
            xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));

            int eventType = 0;
            eventType = xpp.getEventType();
            String currentTag = "";
            boolean result = false;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG) {
                    currentTag = xpp.getName();
                } else if (eventType == XmlPullParser.END_TAG) {

                } else if (eventType == XmlPullParser.TEXT) {
                    if (currentTag.equals("result") && xpp.getText().equals("false")) {
                        return false;
                    }
                    if (currentTag.equals("result") && xpp.getText().equals("true")) {
                        result = true;
                    }
                    if (currentTag.equals("token") && result == true) {
                        this.token = xpp.getText();
                    }
                }
                eventType = xpp.next();
            }
            return true;
        } catch (Exception e) {
            message = e.getMessage();
            return false;
        }
    }
}
