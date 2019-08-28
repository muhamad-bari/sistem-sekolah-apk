package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentLibraryBookAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentLibraryBook extends BaseActivity {

    RecyclerView bookListView;
    ArrayList<String> bookidList = new ArrayList<String>();
    ArrayList<String> bookNameList = new ArrayList<String>();
    ArrayList<String> authorNameList = new ArrayList<String>();
    ArrayList<String> subjectNameList = new ArrayList<String>();
    ArrayList<String> publisherNameList = new ArrayList<String>();
    ArrayList<String> rackNoList = new ArrayList<String>();
    ArrayList<String> quantityList = new ArrayList<String>();
    ArrayList<String> priceList = new ArrayList<String>();
    ArrayList<String> postDate = new ArrayList<String>();
    StudentLibraryBookAdapter adapter;

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_library_book_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.libraryBookIssued));

        bookListView = (RecyclerView) findViewById(R.id.student_libraryBook_listview);

        adapter = new StudentLibraryBookAdapter(StudentLibraryBook.this, bookNameList, authorNameList, subjectNameList,
                publisherNameList, rackNoList, quantityList, priceList, postDate);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        bookListView.setLayoutManager(mLayoutManager);
        bookListView.setItemAnimator(new DefaultItemAnimator());
        bookListView.setAdapter(adapter);

        getDataFromApi();

    }

    private void getDataFromApi () {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getLibraryBookListUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("success");
                        if (success.equals("1")) {

                            JSONArray dataArray = object.getJSONArray("data");

                            for(int i = 0; i < dataArray.length(); i++) {
                                bookidList.add(dataArray.getJSONObject(i).getString("id"));
                                bookNameList.add(dataArray.getJSONObject(i).getString("book_title"));
                                authorNameList.add(dataArray.getJSONObject(i).getString("author"));
                                subjectNameList.add(dataArray.getJSONObject(i).getString("subject"));
                                publisherNameList.add(dataArray.getJSONObject(i).getString("publish"));
                                rackNoList.add(dataArray.getJSONObject(i).getString("rack_no"));
                                quantityList.add(dataArray.getJSONObject(i).getString("qty"));
                                priceList.add(currency + dataArray.getJSONObject(i).getString("perunitcost"));
                                postDate.add(Utility.parseDate("yyyy-MM-dd", defaultDateFormat, dataArray.getJSONObject(i).getString("postdate")));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), object.getString("errorMsg"), Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    pd.dismiss();
                    Toast.makeText(getApplicationContext(), R.string.noInternetMsg, Toast.LENGTH_SHORT).show();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pd.dismiss();
                        Log.e("Volley Error", volleyError.toString());
                        Toast.makeText(StudentLibraryBook.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                headers.put("Client-Service", Constants.clientService);
                headers.put("Auth-Key", Constants.authKey);
                headers.put("Content-Type", Constants.contentType);
                headers.put("User-ID", Utility.getSharedPreferences(getApplicationContext(), "userId"));
                headers.put("Authorization", Utility.getSharedPreferences(getApplicationContext(), "accessToken"));
                Log.e("Headers", headers.toString());
                return headers;
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentLibraryBook.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
