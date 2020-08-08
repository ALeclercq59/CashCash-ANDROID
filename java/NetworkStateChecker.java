package com.example.myfirstapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver{
    //context and database helper object
    private Context context;
    private DataBaseManager db;
    private List<Controler> lesControles;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new DataBaseManager(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                //getting all the unsynced names
                lesControles = db.readLesControles();
                for(Controler c : lesControles)
                {
                    saveName(c);
                }
            }
        }
    }

    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void saveName(final Controler unControle) {
        //"http://10.0.2.2/syncAndroid/insertSync.php"
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://androidasj.mtxserv.com/syncAndroid/insertSync.php" ,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject obj = new JSONObject(response);
                            if (!obj.getBoolean("error")) {
                                //updating the status in sqlite
                                db.deleteControle(unControle);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent("net.simplifiedcoding.datasaved"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("numero_serie", unControle.getN_serie());
                params.put("numero_intervention", unControle.getId_intervention());
                params.put("temps_passer", unControle.getTemps());
                params.put("commentaire", unControle.getCommentaire());
                return params;
            }
        };

        Singleton.getInstance(context).addToRequestQue(stringRequest);
    }
}
