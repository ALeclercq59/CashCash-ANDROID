package com.example.myfirstapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ListLesControles extends ArrayList<Controler> implements Parcelable {
    public ListLesControles() {

    }

    public ListLesControles(Parcel in) {
        this.getFromParcel(in);
    }

    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ListLesControles createFromParcel(Parcel in) {
            return new ListLesControles(in);
        }

        @Override
        public Object[] newArray(int size) {
            return null;
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //Taille de la liste
        int size = this.size();
        dest.writeInt(size);
        for (int i = 0; i < size; i++) {
            Controler c = this.get(i); //On vient lire chaque objet personne
            dest.writeString(c.getN_serie());
            dest.writeString(c.getId_intervention());
            dest.writeString(c.getTemps());
            dest.writeString(c.getCommentaire());
        }
    }

    public void getFromParcel(Parcel in) {
        // On vide la liste avant tout remplissage
        this.clear();

        //Récupération du nombre d'objet
        int size = in.readInt();

        //On repeuple la liste avec de nouveau objet
        for (int i = 0; i < size; i++) {
            Controler c = new Controler();
            c.setN_serie(in.readString());
            c.setId_intervention(in.readString());
            c.setTemps(in.readString());
            c.setCommentaire(in.readString());
            this.add(c);
        }

    }
}
