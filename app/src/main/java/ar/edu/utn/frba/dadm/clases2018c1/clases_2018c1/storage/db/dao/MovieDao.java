package ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import ar.edu.utn.frba.dadm.clases2018c1.clases_2018c1.storage.db.entities.Movie;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM Movie")
    List<Movie> getAll();

    @Query("SELECT * FROM Movie WHERE title = :title  LIMIT 1")
    Movie getByTitle(@NotNull String title);

    @Insert
    void insert(Movie movie);

    @Delete
    void delete(Movie movie);
}