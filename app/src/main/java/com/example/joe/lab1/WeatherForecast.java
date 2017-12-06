package com.example.joe.lab1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends Activity {

    //declare a ForecastQuery event to execute the API URL
    ForecastQuery fq;

    //identify variables that will be handles for GUI objects
    ProgressBar pb = null;

    TextView temp_current;
    TextView temp_max;
    TextView temp_min;

    ImageView weather_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        //set instance variables to specific GUI instances
        temp_current = (TextView) findViewById(R.id.temp_current);
        temp_max = (TextView) findViewById(R.id.temp_max);
        temp_min = (TextView) findViewById(R.id.temp_min);
        weather_image = (ImageView) findViewById(R.id.weather_image);

        pb = (ProgressBar) findViewById(R.id.progressBar);
        pb.setVisibility(View.VISIBLE);

        //instantiate and execute the API URL download
        fq = new ForecastQuery();
        //fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric");  //course API
        fq.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=ebd7ebaccf00b1de15296a90a21824f0&mode=xml&units=metric");  //my API

    }

    @SuppressLint("StaticFieldLeak")
    private class ForecastQuery extends AsyncTask<String, Integer, String> {

        //declare variables that will be modified by the ASyncTask, then set to the GUI handles after execution
        String current;
        String min;
        String max;
        String icon;

        Bitmap image;

        @Override
        protected String doInBackground(String... args) {

            InputStream iStream;

            try {
                //create connection to API
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                iStream = urlConnection.getInputStream();

                //create XMLPullParser from API Connection
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(iStream, "UTF-8");

                //iterate through each XML tag in the API response
                while (xpp.getEventType() != XmlPullParser.END_DOCUMENT) {
                    switch (xpp.getEventType()) {

                        //if the current tag is an opening tag
                        case XmlPullParser.START_TAG: {

                            //if it is a temperature tag, grab the current, max, and min temp attributes and publish the progress
                            if (xpp.getName().equals("temperature")) {
                                current = xpp.getAttributeValue(null, "value");
                                publishProgress(25);
                                min = xpp.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = xpp.getAttributeValue(null, "max");
                                publishProgress(75);
                                //Log.i("temps", current + min + max);
                            }

                            //if it is a weather tag, grab the icon name attribute
                            if (xpp.getName().equals("weather")) {
                                icon = xpp.getAttributeValue(null, "icon");
                                //Log.i("icon", icon);
                            }
                            break;
                        }
                    }

                    //after each complete tag has been read, move to the next.
                    xpp.next();
                }
            } catch (XmlPullParserException xppe) {
                Log.e("ERROR", "ParserException");
            } catch (MalformedURLException mue) {
                Log.e("ERROR", "MalformedURLException");
            } catch (IOException ioe) {
                Log.e("ERROR", "IOException");
            }

            //check that the icon is NOT already downloaded to local storage
            if (!imageDownloaded(icon)) {
                //note that the image is not yet downloaded, then download it to a temporary bitmap object
                Log.i("Bitmap downloader", "downloading image for the first time");
                Bitmap image = getImage("http://openweathermap.org/img/w/" + icon + ".png");

                //write the temporary bitmap object to local storage file, compressed to png
                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(icon + ".png", Context.MODE_PRIVATE);
                    image.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                    outputStream.flush();
                    outputStream.close();
                } catch (IOException e) {   //failure to open/flush/close the stream
                    e.printStackTrace();
                }

            } else {
                //image is already downloaded - make a note in the log
                Log.i("bitmap downloader", "image already downloaded locally");
            }

            //now that the image is 100% for sure downloaded, open it and set it to the image variable
            FileInputStream fis = null;
            try {
                fis = openFileInput(icon + ".png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            image = BitmapFactory.decodeStream(fis);

            //update the progress bar to show that all network activity is complete
            publishProgress(100);

            return "Go to OnPostExecute";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //set the ProgressBar progress to the parameter value
            pb.setProgress(values[0]);
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(String result) {

            //write all instance variables to the GUI
            temp_current.setText("Current: " + fq.current + (char) 0x00B0);
            temp_max.setText("Max: " + fq.max + (char) 0x00B0);
            temp_min.setText("Min: " + fq.min + (char) 0x00B0);
            weather_image.setImageBitmap(image);

            //when complete, hide the progressbar
            pb.setVisibility(View.INVISIBLE);
        }

        //Overload: getImage from a URL string
        Bitmap getImage(String urlString) {
            try {
                URL url = new URL(urlString);
                return getImage(url);
            } catch (MalformedURLException e) {
                return null;
            }
        }

        //download the image at parameter URL object
        Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        //returns true IFF the file is already in local storage
        boolean imageDownloaded(String iconName) {
            File file = getBaseContext().getFileStreamPath(iconName + ".png");
            return file.exists();
        }
    }
}
