package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class infos_materiels extends AppCompatActivity {

    // Déclare les composants mis sur la page
    private EditText n_serie;
    private EditText temps;
    private EditText commentaire;
    private Button valider;
    private TextView textPage;
    private String id_intervention;

    private ArrayList<Controler> lesMateriels = new ListLesControles();

    ArrayList<Controler> lesControles = new ArrayList<>();

    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;

    private void init ()
    {
        // Récupère les composants en fonction de leur id
        n_serie = (EditText) findViewById(R.id.n_serie);
        temps = (EditText) findViewById(R.id.temps);
        commentaire = (EditText) findViewById(R.id.commentaire);
        valider = (Button)findViewById(R.id.valider);
        textPage = (TextView)findViewById(R.id.nb_page);

        // Déclare un nouvel objet
        DataBaseManager dataBaseManager = new DataBaseManager(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos_materiels);


        // Appel de la fonction init()
        init();

        final DataBaseManager dataBaseManager1 = new DataBaseManager(this);

        //Récupération de la fenêtre d'avant et des différentes variables envoyé
        Intent intent = getIntent();
        id_intervention = intent.getStringExtra("id_intervention");
        final String nb_materiels = intent.getStringExtra("nb_materiels");
        textPage.setText("Page n°1/" + nb_materiels);


        temps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);


                TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                        infos_materiels.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnTimeSetListener,
                        hour,minute,true);

                mTimePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePickerDialog.show();

            }
        });

        mOnTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String formattedTime = "";
                String sHour = "00";
                String sMinute = "00";
                // converting hour to tow digit if its between 0 to 9. (e.g. 7 to 07)
                if(hourOfDay < 10)
                    sHour = "0"+hourOfDay;
                else
                    sHour = String.valueOf(hourOfDay);

                if(minute < 10)
                    sMinute = "0"+minute;
                else
                    sMinute = String.valueOf(minute);


                temps.setText(sHour + ":" + sMinute);
            }
        };

        // Dès qu'on clique sur le bouton connexion
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupère ce qu'on a mis dans les zones de saisie
                String nb_serie = n_serie.getText().toString();
                String temps_passe = temps.getText().toString();
                String commentaires = commentaire.getText().toString();

                if (!nb_serie.matches("") && !temps_passe.matches("") && !commentaires.matches(""))
                {
                    try
                    {
                        Controler c = new Controler(nb_serie, id_intervention, temps_passe, commentaires);
                        lesMateriels.add(c);
                        System.out.println(lesMateriels.size());
                        n_serie.setText("");
                        temps.setText("");
                        commentaire.setText("");
                        textPage.setText("Page n°" + (lesMateriels.size() + 1) + "/" + nb_materiels);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(infos_materiels.this, "Saisie Incorrecte", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // Si l'utilisateur n'a pas mis toutes les informations cela met un message d'erreur
                    Toast.makeText(infos_materiels.this, "Veuillez saisir toutes les informations nécessaires !", Toast.LENGTH_SHORT).show();
                }


                if (lesMateriels.size() == Integer.parseInt(nb_materiels))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(infos_materiels.this);

                    builder.setMessage("Etes-vous sures de vos saisies ?")
                            .setCancelable(false)
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (Controler c : lesMateriels)
                                    {
                                        dataBaseManager1.insertMateriels(c);
                                        System.out.println(c.getN_serie());
                                    }
                                    // Déclaration d'une nouvelle intent
                                    Intent intent1 = new Intent(infos_materiels.this, infos_Interventions.class);
                                    // On passe à l'autre page
                                    startActivity(intent1);
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent1 = new Intent (infos_materiels.this, verification.class);
                                    intent1.putExtra("lesControles",(Parcelable)lesMateriels);
                                    startActivity(intent1);
                                }
                            });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }
        });
    }
}
