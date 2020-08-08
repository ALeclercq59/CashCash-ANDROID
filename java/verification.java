package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class verification extends AppCompatActivity {

    // Déclare les composants mis sur la page
    private TextView textView3;
    private TextView textView4;
    private TextView textView6;
    private TextView textView9;
    private TextView textView8;
    private EditText id_intervention1;
    private EditText n_serie1;
    private EditText temps1;
    private EditText commentaire1;
    private Button valider1;


    TimePickerDialog.OnTimeSetListener mOnTimeSetListener;


    private void init ()
    {
        // Récupère les composants en fonction de leur id
        id_intervention1 = (EditText)findViewById(R.id.numero_intervention);
        n_serie1 = (EditText) findViewById(R.id.numero_serie);
        temps1 = (EditText) findViewById(R.id.temps_passer);
        commentaire1 = (EditText) findViewById(R.id.commentaires);
        valider1 = (Button) findViewById(R.id.suivant_verif);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);

        // Appel de la fonction init()
        init();

        final DataBaseManager dataBaseManager1 = new DataBaseManager(this);

        Intent intent = getIntent();
        Bundle b 	= getIntent().getExtras();
        final ListLesControles lesControles = b.getParcelable("lesControles");

        ListLesControles lesControlesFinaux;

        final int nb_pages = 0;

        final int[] i = {0};

        final Controler c = lesControles.get(i[0]);



        id_intervention1.setText(c.getId_intervention());
        n_serie1.setText(c.getN_serie());
        temps1.setText(c.getTemps());
        commentaire1.setText(c.getCommentaire());


        temps1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mCalendar =  Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = mCalendar.get(Calendar.MINUTE);


                TimePickerDialog mTimePickerDialog = new TimePickerDialog(
                        verification.this,
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


                temps1.setText(sHour + ":" + sMinute);
            }
        };

        valider1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num_inter = id_intervention1.getText().toString();
                String num_serie = n_serie1.getText().toString();
                String temps = temps1.getText().toString();
                String commentaires = commentaire1.getText().toString();

                Controler c2 = new Controler(num_serie, num_inter, temps, commentaires);
                lesControles.set(i[0], c2);

                i[0]++;

                if (i[0] < lesControles.size())
                {
                    Controler c = lesControles.get(i[0]);
                    id_intervention1.setText(c.getId_intervention());
                    n_serie1.setText(c.getN_serie());
                    temps1.setText(c.getTemps());
                    commentaire1.setText(c.getCommentaire());
                }
                else
                {
                    for (Controler c1 : lesControles)
                    {
                        dataBaseManager1.insertMateriels(c1);
                    }
                    // Déclaration d'une nouvelle intent
                    Intent intent1 = new Intent(verification.this, infos_Interventions.class);
                    // On passe à l'autre page
                    startActivity(intent1);
                }
            }
        });

    }
}
