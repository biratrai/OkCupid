package com.gooner10.okcupid.ui.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.gooner10.okcupid.Model.DataModel;
import com.gooner10.okcupid.Model.LocationModel;
import com.gooner10.okcupid.Network.VolleySingleton;
import com.gooner10.okcupid.R;
import com.gooner10.okcupid.ui.Adapter.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {
    private String LOG_TAG = SearchFragment.class.getSimpleName();
    ArrayList<DataModel> dataModelArrayList = new ArrayList<>();
    SearchAdapter mSearchAdapter;

    @Bind(R.id.recyclerViewMovie)
    RecyclerView mSearchRecyclerView;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        JsonParser();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ButterKnife.bind(this, view);

        // Setting the Grid Layout
        mSearchRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        // Setting the Adapter
        mSearchAdapter = new SearchAdapter(getActivity(), dataModelArrayList);
        mSearchRecyclerView.setAdapter(mSearchAdapter);
        return view;
    }

    public void JsonParser() {
        final String url = "https://www.okcupid.com/matchSample.json";
        RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(LOG_TAG, "response" + response);
                parseJSONresponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(LOG_TAG, "Error Message" + error.getMessage());
            }
        });
        Log.d(LOG_TAG, "Error Message" + jsObjRequest);
        requestQueue.add(jsObjRequest);
    }

    private void parseJSONresponse(JSONObject response) {
        try {
            String jsonString = response.getString("data");

            JSONArray jsonArray = new JSONArray(jsonString);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                String photo = jsonObject.getString("photo");
                Log.i(LOG_TAG, "photo" + photo);

                JSONObject photoJsonObject = new JSONObject(photo);
                Log.i(LOG_TAG, "photoJsonObject" + photoJsonObject);

                String full_path = photoJsonObject.getString("full_paths");
                Log.i(LOG_TAG, "full_path" + full_path);

                JSONObject fullPathJsonObject = new JSONObject(full_path);
                Log.i(LOG_TAG, "photoJsonObject" + photoJsonObject);

                String photo_large = fullPathJsonObject.getString("large");
                String photo_medium = fullPathJsonObject.getString("medium");

                String user_name = jsonObject.getString("username");
                String match = jsonObject.getString("match");
                int age = Integer.parseInt(jsonObject.getString("age"));

                String location = jsonObject.getString("location");
                JSONObject locationJsonObject = new JSONObject(location);
                String mCountryCode = locationJsonObject.getString("country_code");
                String mCountryName = locationJsonObject.getString("country_name");
                String mCityName = locationJsonObject.getString("city_name");
                String mStateName = locationJsonObject.getString("state_name");
                String mStateCode = locationJsonObject.getString("state_code");

                LocationModel locationModel = new LocationModel(mCountryCode, mCountryName, mCityName, mStateName, mStateCode);
                DataModel dataModel = new DataModel(photo_medium,
                        photo_large,
                        user_name,
                        match,
                        age,
                        locationModel);
                dataModelArrayList.add(dataModel);
                mSearchRecyclerView.getAdapter().notifyDataSetChanged();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "dataModelArrayList jsonparser" + dataModelArrayList);
//      EventBus.getDefault().postSticky(mMovieDataArrayList);

    }

}
