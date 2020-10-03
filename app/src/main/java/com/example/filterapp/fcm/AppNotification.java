package com.example.filterapp.fcm;

import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AppNotification {
    private boolean staffSignUp;
    private boolean changeBranch;
    private boolean changePosition;
    private boolean customerDeleted;
    private boolean staffStartNavigation;
    private boolean soldNewFilter;
    private boolean serviceDone;
    private boolean companyEmailDetailsChanged;
    private boolean adminVerificationPassChange;
    private boolean identityVerificationPassChange;
    private boolean staffDeleted;

    public AppNotification() {
        this(true, true, true, true, true,
                true, true, true, true,
                true, true);
    }

    public AppNotification(boolean staffSignUp, boolean changeBranch, boolean changePosition, boolean customerDeleted,
                           boolean staffStartNavigation, boolean soldNewFilter, boolean serviceDone,
                           boolean companyEmailDetailsChanged, boolean adminVerificationPassChange,
                           boolean identityVerificationPassChange, boolean staffDeleted) {
        this.staffSignUp = staffSignUp;
        this.changeBranch = changeBranch;
        this.changePosition = changePosition;
        this.customerDeleted = customerDeleted;
        this.staffStartNavigation = staffStartNavigation;
        this.soldNewFilter = soldNewFilter;
        this.serviceDone = serviceDone;
        this.companyEmailDetailsChanged = companyEmailDetailsChanged;
        this.adminVerificationPassChange = adminVerificationPassChange;
        this.identityVerificationPassChange = identityVerificationPassChange;
        this.staffDeleted = staffDeleted;
    }

    public boolean isStaffSignUp() {
        return staffSignUp;
    }

    public void setStaffSignUp(boolean staffSignUp) {
        this.staffSignUp = staffSignUp;
    }

    public boolean isChangeBranch() {
        return changeBranch;
    }

    public void setChangeBranch(boolean changeBranch) {
        this.changeBranch = changeBranch;
    }

    public boolean isChangePosition() {
        return changePosition;
    }

    public void setChangePosition(boolean changePosition) {
        this.changePosition = changePosition;
    }

    public boolean isCustomerDeleted() {
        return customerDeleted;
    }

    public void setCustomerDeleted(boolean customerDeleted) {
        this.customerDeleted = customerDeleted;
    }

    public boolean isStaffStartNavigation() {
        return staffStartNavigation;
    }

    public void setStaffStartNavigation(boolean staffStartNavigation) {
        this.staffStartNavigation = staffStartNavigation;
    }

    public boolean isSoldNewFilter() {
        return soldNewFilter;
    }

    public void setSoldNewFilter(boolean soldNewFilter) {
        this.soldNewFilter = soldNewFilter;
    }

    public boolean isServiceDone() {
        return serviceDone;
    }

    public void setServiceDone(boolean serviceDone) {
        this.serviceDone = serviceDone;
    }

    public boolean isCompanyEmailDetailsChanged() {
        return companyEmailDetailsChanged;
    }

    public void setCompanyEmailDetailsChanged(boolean companyEmailDetailsChanged) {
        this.companyEmailDetailsChanged = companyEmailDetailsChanged;
    }

    public boolean isAdminVerificationPassChange() {
        return adminVerificationPassChange;
    }

    public void setAdminVerificationPassChange(boolean adminVerificationPassChange) {
        this.adminVerificationPassChange = adminVerificationPassChange;
    }

    public boolean isIdentityVerificationPassChange() {
        return identityVerificationPassChange;
    }

    public void setIdentityVerificationPassChange(boolean identityVerificationPassChange) {
        this.identityVerificationPassChange = identityVerificationPassChange;
    }

    public boolean isStaffDeleted() {
        return staffDeleted;
    }

    public void setStaffDeleted(boolean staffDeleted) {
        this.staffDeleted = staffDeleted;
    }

    public JsonObjectRequest sendNotification(String topic, String title, String body) {
        JSONObject mainObj = new JSONObject();
        String URL = "https://fcm.googleapis.com/fcm/send";
        try {
            mainObj.put("to", "/topics/" + topic);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", body);
            notificationObj.put("sound", "default");
            mainObj.put("data", notificationObj);
            JSONObject priority = new JSONObject();
            priority.put("priority", "high");
            mainObj.put("android", priority);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAW3G6BnI:APA91bG8PXt8-aauFEFyh9OYbbVVed-HqZj87nxqkrx2mMt14I4WV4xpWaulQTL0SGfEjEpkV4wkFDpCdF7W_peu7pWuNJefoFvPq-CpavfAEf0mmy-Igwf3brVYkm9K2TeZzrlCsi4Q");
                    return header;
                }
            };

            return request;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public JsonObjectRequest specifUser(String id, String title, String body) {
        JSONObject mainObj = new JSONObject();
        String URL = "https://fcm.googleapis.com/fcm/send";
        try {
            mainObj.put("to", id);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", title);
            notificationObj.put("body", body);
            notificationObj.put("sound", "default");
            mainObj.put("data", notificationObj);
            JSONObject priority = new JSONObject();
            priority.put("priority", "high");
            mainObj.put("android", priority);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }
            ) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAAW3G6BnI:APA91bG8PXt8-aauFEFyh9OYbbVVed-HqZj87nxqkrx2mMt14I4WV4xpWaulQTL0SGfEjEpkV4wkFDpCdF7W_peu7pWuNJefoFvPq-CpavfAEf0mmy-Igwf3brVYkm9K2TeZzrlCsi4Q");
                    return header;
                }
            };

            return request;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
