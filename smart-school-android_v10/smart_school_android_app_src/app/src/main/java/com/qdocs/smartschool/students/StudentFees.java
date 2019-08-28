package com.qdocs.smartschool.students;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import com.qdocs.smartschool.adapters.StudentFeesAdapter;
import com.qdocs.smartschool.BaseActivity;
import com.qdocs.smartschool.R;
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

public class StudentFees extends BaseActivity {

    RecyclerView feesList;
    StudentFeesAdapter adapter;

    TextView gtAmtTV, gtDiscountTV, gtFineTV, gtPaidTV, gtBalanceTV;
    CardView grandTotalLay;

    ArrayList <String> feesIdList = new ArrayList<String>();
    ArrayList <String> feesCodeList = new ArrayList<String>();
    ArrayList <String> dueDateList = new ArrayList<String>();
    ArrayList <String> amtList = new ArrayList<String>();
    ArrayList <String> paidAmtList = new ArrayList<String>();
    ArrayList <String> balanceAmtList = new ArrayList<String>();
    ArrayList <String> statusList = new ArrayList<String>();
    ArrayList <String> feesDepositIdList = new ArrayList<String>();
    ArrayList <String> feesDetails = new ArrayList<String>();
    ArrayList <String> feesTypeId = new ArrayList<String>();
    ArrayList <String> feesCat = new ArrayList<String>();

    ArrayList <String> discountNameList = new ArrayList<String>();
    ArrayList <String> discountAmtList = new ArrayList<String>();
    ArrayList <String> discountStatusList = new ArrayList<String>();

    public Map<String, String> params = new Hashtable<String, String>();
    public Map<String, String>  headers = new HashMap<String, String>();

    TextView headerTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.students_fees_activity, null, false);
        mDrawerLayout.addView(contentView, 0);

        titleTV.setText(getApplicationContext().getString(R.string.fees));

        feesList = (RecyclerView) findViewById(R.id.studentFees_listview);

        gtAmtTV = findViewById(R.id.fees_amtTV);
        gtDiscountTV = findViewById(R.id.fees_discountTV);
        gtFineTV = findViewById(R.id.fees_fineTV);
        gtPaidTV = findViewById(R.id.fees_paidTV);
        gtBalanceTV = findViewById(R.id.fees_balance);
        grandTotalLay = findViewById(R.id.feesAdapter_containerCV);

        headerTV = findViewById(R.id.fees_headTV);

        adapter = new StudentFeesAdapter(StudentFees.this, feesIdList, feesCodeList, dueDateList, amtList,
                paidAmtList, balanceAmtList, feesDepositIdList, statusList, feesDetails, feesTypeId, feesCat,
                discountNameList, discountAmtList, discountStatusList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        feesList.setLayoutManager(mLayoutManager);
        feesList.setItemAnimator(new DefaultItemAnimator());
        feesList.setAdapter(adapter);


        params.put("student_id", Utility.getSharedPreferences(getApplicationContext(), "studentId"));
        JSONObject obj=new JSONObject(params);
        Log.e("params ", obj.toString());

        getDataFromApi(obj.toString());

        headerTV.setBackgroundColor(Color.parseColor(Utility.getSharedPreferences(getApplicationContext(), Constants.secondaryColour)));

    }

    private void getDataFromApi (String bodyParams) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();

        final String requestBody = bodyParams;

        String url = Utility.getSharedPreferences(getApplicationContext(), "apiUrl")+ Constants.getFeesUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                if (result != null) {
                    pd.dismiss();
                    try {
                        Log.e("Result", result);
                        JSONObject object = new JSONObject(result);

                        String success = "1"; //object.getString("success");
                        if (success.equals("1")) {

                            if(object.getString("pay_method").equals("0")) {
                                Log.e("test", "testing");
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, false);
                            } else {
                                Utility.setSharedPreferenceBoolean(getApplicationContext(), Constants.showPaymentBtn, true);
                            }


                            String currency = Utility.getSharedPreferences(getApplicationContext(), Constants.currency);

                            JSONObject grandTotalDetails = object.getJSONObject("grand_fee");
                            gtAmtTV.setText(currency + grandTotalDetails.getString("amount"));
                            gtDiscountTV.setText(currency + grandTotalDetails.getString("amount_discount"));
                            gtFineTV.setText(currency + grandTotalDetails.getString("amount_fine"));
                            gtPaidTV.setText(currency + grandTotalDetails.getString("amount_paid"));
                            gtBalanceTV.setText(currency + grandTotalDetails.getString("amount_remaining"));

                            JSONArray dataArray = object.getJSONArray("student_due_fee");

                            if(dataArray.length() != 0) {
                                grandTotalLay.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getApplicationContext(), R.string.noData, Toast.LENGTH_LONG).show();
                            }

                            for(int i = 0; i < dataArray.length(); i++) {

                                JSONArray feesArray = dataArray.getJSONObject(i).getJSONArray("fees");

                                for(int j = 0; j<feesArray.length(); j++) {
                                    feesIdList.add(feesArray.getJSONObject(j).getString("id"));
                                    feesCodeList.add(feesArray.getJSONObject(j).getString("name") + "-" + feesArray.getJSONObject(j).getString("type") );

                                    dueDateList.add(feesArray.getJSONObject(j).getString("due_date"));
                                    amtList.add( currency + feesArray.getJSONObject(j).getString("amount"));
                                    paidAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_paid"));
                                    balanceAmtList.add(currency + feesArray.getJSONObject(j).getString("total_amount_remaining"));
                                    feesDepositIdList.add(feesArray.getJSONObject(j).getString("student_fees_deposite_id"));
                                    feesTypeId.add(feesArray.getJSONObject(j).getString("fee_groups_feetype_id"));
                                    feesCat.add("fees");

                                    discountNameList.add("");
                                    discountAmtList.add("");
                                    discountStatusList.add("");

                                    statusList.add(feesArray.getJSONObject(j).getString("status").substring(0, 1).toUpperCase() + feesArray.getJSONObject(j).getString("status").substring(1));

                                    JSONObject feesDetailsJson;
                                    try {
                                        feesDetailsJson = new JSONObject(feesArray.getJSONObject(j).getString("amount_detail"));
                                    } catch (JSONException e) {
                                        feesDetailsJson = new JSONObject();
                                    }

                                    feesDetails.add(feesDetailsJson.toString());
                                }

                            }

                            JSONArray discountArray = object.getJSONArray("student_discount_fee");

                            for (int i = 0; i < discountArray.length(); i++) {
                                feesIdList.add(discountArray.getJSONObject(i).getString("id")+"discount");
                                discountNameList.add(discountArray.getJSONObject(i).getString("name"));
                                discountAmtList.add(discountArray.getJSONObject(i).getString("amount"));
                                discountStatusList.add(discountArray.getJSONObject(i).getString("status"));
                                feesCat.add("discount");
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
                        Toast.makeText(StudentFees.this, R.string.slowInternetMsg, Toast.LENGTH_LONG).show();
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
        RequestQueue requestQueue = Volley.newRequestQueue(StudentFees.this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

}
