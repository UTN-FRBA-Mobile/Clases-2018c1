package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.dao.MovieDao;
import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie;

@Database(entities = {Movie.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase db;

    public abstract MovieDao movieDao();

    public static AppDatabase getInstance(Context context){
        if(db == null){
            db = Room.databaseBuilder(context, AppDatabase.class, "database-cache").build();
        }

        return db;
    }
}