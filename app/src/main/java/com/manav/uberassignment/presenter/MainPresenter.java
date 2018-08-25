package com.manav.uberassignment.presenter;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.manav.uberassignment.Interfaces.MainInterface;
import com.manav.uberassignment.model.AddToAdapter;
import com.manav.uberassignment.model.Model;
import com.manav.uberassignment.model.OuterModel;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainPresenter implements MainInterface.Actions {

    ArrayList<AddToAdapter> mainDataList;
    MainInterface.view view;
    boolean adapterInitializationStatus;

    public MainPresenter(MainInterface.view view) {
        this.view = view;
    }


    @Override
    public void callNextPages(int pageno, String title) {
        view.showPrgress();
        String url = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=3e7cc266ae2b0e0d78e279ce8e361736&format=json&nojsoncallback=1&per_page=9&page=" + pageno + "&text=" + title;
        try {
            new HttpGetRequest().execute(url).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    class HttpGetRequest extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected String doInBackground(String... params) {
            String stringUrl = params[0];
            String result;
            String inputLine;
            try {
                //Create a URL object holding our url
                URL myUrl = new URL(stringUrl);
                //Create a connection
                HttpURLConnection connection = (HttpURLConnection)
                        myUrl.openConnection();
                //Set methods and timeouts
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);

                //Connect to our url
                connection.connect();
                //Create a new InputStreamReader
                InputStreamReader streamReader = new
                        InputStreamReader(connection.getInputStream());
                //Create a new buffered reader and String Builder
                BufferedReader reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                //Check if the line we are reading is not null
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                //Close our InputStream and Buffered reader
                reader.close();
                streamReader.close();
                //Set our result equal to our stringBuilder
                result = stringBuilder.toString();
            } catch (IOException e) {
                e.printStackTrace();
                result = null;
            }
            return result;
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            view.hideProgress();
            try {
                JSONObject response = new JSONObject(result);
                parse(response);
            } catch (Throwable tx) {
                view.ToastMessage("error parsing data");
            }
        }
    }

    private void parse(JSONObject response) {
        OuterModel outerModel = new Gson().fromJson(response.toString(), OuterModel.class);
        List<Model> photolist;
        mainDataList = new ArrayList<>();
        if (outerModel != null && outerModel.getPhotos() != null) {
            photolist = outerModel.getPhotos().getPhoto();
            if (photolist != null && photolist.size() == 0)
                view.ToastMessage("no data available");
            generateUrlList(photolist);
            if (!adapterInitializationStatus) {
                adapterInitializationStatus = true;
                view.initializeAdapter(mainDataList);
            } else {
                view.notifydataChanges(mainDataList);
            }
        } else {
            view.ToastMessage("no data available");
        }

    }

    private void generateUrlList(List<Model> photolist) {
        if (photolist != null && photolist.size() > 0) {
            for (int i = 0; i < photolist.size(); i++) {
                String url = "https://farm" + photolist.get(i).getFarm() + ".staticflickr.com/" + photolist.get(i).getServer() + "/" + photolist.get(i).getId() + "_" + photolist.get(i).getSecret() + "_c.jpg";
                AddToAdapter data = new AddToAdapter();
                data.setUrl(url);
                data.setName(photolist.get(i).getTitle());
                mainDataList.add(data);
            }
        }
    }
}
