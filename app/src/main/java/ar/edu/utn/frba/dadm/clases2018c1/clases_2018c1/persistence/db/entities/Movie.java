package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.persistence.db.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Movie {
    @PrimaryKey
    @NonNull
    public String title;

    public String year;

    public String imageUri;
}
