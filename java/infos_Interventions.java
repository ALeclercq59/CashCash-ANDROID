package com.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class infos_Interventions extends AppCompatActivity {

    private EditText id_intervention;
    private EditText nb_materiels;
    private Button valider;

    private void init ()
    {
        // Récupère les composants en fonction de leur id
        id_intervention = (EditText) findViewById(R.id.id_intervention);
        nb_materiels = (EditText) findViewById(R.id.nb_materiels);
        valider = (Button)findViewById(R.id.btn_valider);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infos__interventions);

        // Appel de la fonction init()
        init();

        //Récupération de la fenêtre d'avant et des différentes variables envoyé
        Intent intent = getIntent();

        // Dès qu'on clique sur le bouton connexion
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupère ce qu'on a mis dans les zones de saisie
                String id_inter = id_intervention.getText().toString();
                String nombre = nb_materiels.getText().toString();

                if (!id_inter.matches("") && !nombre.matches(""))
                {
                    // Déclaration d'une nouvelle intent
                    Intent intent1 = new Intent(infos_Interventions.this, infos_materiels.class);

                    // Ajoute en extra les différentes informations pour après les envoyer dans l'autre page
                    intent1.putExtra("id_intervention", id_inter);
                    intent1.putExtra("nb_materiels", nombre);
                    
                    // On passe à l'autre page
                    startActivity(intent1);
                }
                else
                {
                    // Si l'utilisateur n'a pas mis toutes les informations cela met un message d'erreur
                    Toast.makeText(infos_Interventions.this, "Veuillez saisir toutes les informations nécessaires !", Toast.LENGTH_SHORT).show();
                }
            }
        });
        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }
}
