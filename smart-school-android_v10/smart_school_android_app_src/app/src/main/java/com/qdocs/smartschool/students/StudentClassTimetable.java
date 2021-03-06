package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
import com.qdocs.smartschool.adapters.StudentClassTimetableAdapter;
import com.qdocs.smartschool.utils.Constants;
import com.qdocs.smartschool.utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class StudentClassTimetable extends BaseActivity {


    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    RecyclerView mondayList, tuesdayList, wednesdayList, thursdayList, fridayList, saturdayList, sundayList;
    TextView mondayHeader, tuesdayHeader, wednesdayHeader, thursdayHeader, fridayHeader, saturdayHeader, sundayHeader;

    ArrayList<String> mondaySubject = new ArrayList<>();
    ArrayList<String> mondayTime = new ArrayList<>();
    ArrayList<String> mondayRoomNo = new ArrayList<>();

    ArrayList<String> tuesdaySubject = new ArrayList<>();
    ArrayList<String> tuesdayTime = new ArrayList<>();
    ArrayList<String> tuesdayRoomNo = new ArrayList<>();

    ArrayList<String> wednesdaySubject = new ArrayList<>();
    ArrayList<String> wednesdayTime = new ArrayList<>();
    ArrayList<String> wednesdayRoomNo = new ArrayList<>();

    ArrayList<String> thursdaySubject = new ArrayList<>();
    ArrayList<String> thursdayTime = new ArrayList<>();
    ArrayList<String> thursdayRoomNo = new ArrayList<>();

    ArrayList<String> fridaySubject = new ArrayList<>();
    ArrayList<String> fridayTime = new ArrayList<>();
    ArrayList<String> fridayRoomNo = new ArrayList<>();

    ArrayList<String> saturdaySubject = new ArrayList<>();
    ArrayList<String> saturdayTime = new ArrayList<>();
    ArrayList<String> saturdayRoomNo = new ArrayList<>();

    ArrayList<String> sundaySubject = new ArrayList<>();
    ArrayList<String> sundayTime = new ArrayList<>();
    ArrayList<String> sundayRoomNo = new ArrayList<>();

    StudentClassTimetableAdapter  mondayAdapter, tuesdayAdapter, wednesdayAdapter, thursdayAdapter, fridayAdapter, saturdayAdapter, sundayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.student_class_timetable_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.timeTable));


        mondayList = findViewById(R.id.classTimetable_mondayList);
        tuesdayList = findViewById(R.id.classTimetable_tuesdayList);
        wednesdayList = findViewById(R.id.classTimetable_wednesdayList);
        thursdayList = findViewById(R.id.classTimetable_thursdayList);
        fridayList = findViewById(R.id.classTimetable_fridayList);
        saturdayList = findViewById(R.id.classTimetable_saturdayList);
        sundayList = findViewById(R.id.classTimetable_sundayList);

        mondayHeader = findViewById(R.id.classTimetable_mondayHeader);
        tuesdayHeader = findViewById(R.id.classTimetable_tuesdayHeader);
        wednesdayHeader = findViewById(R.id.classTimetable_wednesdayHeader);
        thursdayHeader = findViewById(R.id.classTimetable_thursdayHeader);
        fridayHeader = findViewById(R.id.classTimetable_fridayHeader);
        saturdayHeader = findViewById(R.id.classTimetable_saturdayHeader);
        sundayHeader = findViewById(R.id.classTimetable_sundayHeader);

        decorate();

        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
        JSONObject obj=new JSONObject(params);
        Log.e("params ", obj.toString());

        getDataFromApi(obj.toString());

        mondayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, mondaySubject, mondayTime, mondayRoomNo);
        tuesdayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, tuesdaySubject, tuesdayTime, tuesdayRoomNo);
        wednesdayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, wednesdaySubject, wednesdayTime, wednesdayRoomNo);
        thursdayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, thursdaySubject, thursdayTime, thursdayRoomNo);
        fridayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this,fridaySubject,fridayTime, fridayRoomNo);
        saturdayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, saturdaySubject, saturdayTime, saturdayRoomNo);
        sundayAdapter = new StudentClassTimetableAdapter(StudentClassTimetable.this, sundaySubject, sundayTime, sundayRoomNo);

        RecyclerView.LayoutManager mondayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager tuesdayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager wednesdayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager thursdayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager fridayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager saturdayLayoutManager = new LinearLayoutManager(getApplicationContext());
        RecyclerView.LayoutManager sundayLayoutManager = new LinearLayoutManager(getApplicationContext());

        mondayList.setLayoutManager(mondayLayoutManager);
        mondayList.setItemAnimator(new DefaultItemAnimator());
        mondayList.setAdapter(mondayAdapter);

        tuesdayList.setLayoutManager(tuesdayLayoutManager);
        tuesdayList.setItemAnimator(new DefaultItemAnimator());
        tuesdayList.setAdapter(tuesdayAdapter);

        wednesdayList.setLayoutManager(wednesdayLayoutManager);
        wednesdayList.setItemAnimator(new DefaultItemAnimator());
        wednesdayList.setAdapter(wednesdayAdapter);

        thursdayList.setLayoutManager(thursdayLayoutManager);
        thursdayList.setItemAnimator(new DefaultItemAnimator());
        thursdayList.setAdapter(thursdayAdapter);

        fridayList.setLayoutManager(fridayLayoutManager);
        fridayList.setItemAnimator(new DefaultItemAnimator());
        fridayList.setAdapter(fridayAdapter);

        saturdayList.setLayoutManager(saturdayLayoutManager);
        saturdayList.setItemAnimator(new DefaultItemAnimator());
        saturdayList.setAdapter(saturdayAdapter);

        sundayList.setLayoutManager(sundayLayoutManager);
        sundayList.setItemAnimator(new DefaultItemAnimator());
        sundayList.setAdapter(sundayAdapter);

    }

    private void decorate() {
        mondayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        tuesdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        wednesdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        thursdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        fridayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        saturdayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
        sundayHeader.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));
    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+Constants.getClassScheduleUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = object.getString("status");
                        if (success.equals("200")) {

                            JSONObject dataObject = object.getJSONObject("result_array");

                            JSONArray mondayArray = dataObject.getJSONArray("Monday");
                            JSONArray tuesdayArray = dataObject.getJSONArray("Tuesday");
                            JSONArray wednesdayArray = dataObject.getJSONArray("Wednesday");
                            JSONArray thursdayArray = dataObject.getJSONArray("Thursday");
                            JSONArray fridayArray = dataObject.getJSONArray("Friday");
                            JSONArray saturdayArray = dataObject.getJSONArray("Saturday");
                            JSONArray sundayArray = dataObject.getJSONArray("Sunday");

                            for(int i = 0; i < mondayArray.length(); i++) {
                                mondaySubject.add(mondayArray.getJSONObject(i).getString("subject"));
                                if(!mondayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    mondayTime.add(mondayArray.getJSONObject(i).getString("start_time") + " - " + mondayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    mondayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                mondayRoomNo.add(mondayArray.getJSONObject(i).getString("room_no"));
                            }
                            mondayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < tuesdayArray.length(); i++) {
                                tuesdaySubject.add(tuesdayArray.getJSONObject(i).getString("subject"));
                                if(!tuesdayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    tuesdayTime.add(tuesdayArray.getJSONObject(i).getString("start_time") + " - " + tuesdayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    tuesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                tuesdayRoomNo.add(tuesdayArray.getJSONObject(i).getString("room_no"));
                            }
                            tuesdayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < wednesdayArray.length(); i++) {
                                wednesdaySubject.add(wednesdayArray.getJSONObject(i).getString("subject"));
                                if(!wednesdayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    wednesdayTime.add(wednesdayArray.getJSONObject(i).getString("start_time") + " - " + wednesdayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    wednesdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                wednesdayRoomNo.add(wednesdayArray.getJSONObject(i).getString("room_no"));
                            }
                            wednesdayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < thursdayArray.length(); i++) {
                                thursdaySubject.add(thursdayArray.getJSONObject(i).getString("subject"));
                                if(!thursdayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    thursdayTime.add(thursdayArray.getJSONObject(i).getString("start_time") + " - " + thursdayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    thursdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                thursdayRoomNo.add(thursdayArray.getJSONObject(i).getString("room_no"));
                            }
                            thursdayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < fridayArray.length(); i++) {
                                fridaySubject.add(fridayArray.getJSONObject(i).getString("subject"));
                                if(!fridayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    fridayTime.add(fridayArray.getJSONObject(i).getString("start_time") + " - " + fridayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    fridayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                fridayRoomNo.add(fridayArray.getJSONObject(i).getString("room_no"));
                            }
                            fridayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < saturdayArray.length(); i++) {
                                saturdaySubject.add(saturdayArray.getJSONObject(i).getString("subject"));
                                if(!saturdayArray.getJSONObject(i).getString("start_time").equals("")) {
                                    saturdayTime.add(saturdayArray.getJSONObject(i).getString("start_time") + " - " + saturdayArray.getJSONObject(i).getString("end_time"));
                                } else {
                                    saturdayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                }
                                saturdayRoomNo.add(saturdayArray.getJSONObject(i).getString("room_no"));
                            }
                            saturdayAdapter.notifyDataSetChanged();

                            for(int i = 0; i < sundayArray.length(); i++) {

                                    sundaySubject.add(sundayArray.getJSONObject(i).getString("subject"));
                                    if(!sundayArray.getJSONObject(i).getString("start_time").equals("")) {
                                        sundayTime.add(sundayArray.getJSONObject(i).getString("start_time") + " - " + sundayArray.getJSONObject(i).getString("end_time"));
                                    } else {
                                        sundayTime.add(getApplicationContext().getString(R.string.notScheduled));
                                    }
                                    sundayRoomNo.add(sundayArray.getJSONObject(i).getString("room_no"));

                            }
                            sundayAdapter.notifyDataSetChanged();




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
                        Toast.makeText(StudentClassTimetable.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
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

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }
        };
        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(StudentClassTimetable.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
