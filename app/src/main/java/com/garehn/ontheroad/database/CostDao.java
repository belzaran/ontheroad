package com.garehn.ontheroad.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.garehn.ontheroad.trip.Cost;

import java.util.List;

@Dao
public interface CostDao {

    @Query("SELECT * FROM Cost WHERE tripId = :tripId")
    List<Cost> getCosts(long tripId);

    @Insert
    long insertCost(Cost cost);

    @Update
    int updateCost(Cost cost);

    @Query("DELETE FROM Cost WHERE id = :expenseId")
    int deleteCost(long expenseId);

    @Query("SELECT * FROM Cost WHERE categoryId = :categoryId AND tripId = :tripId")
    List<Cost> getCategoryCost(int categoryId, long tripId);

    @Query("DELETE FROM Cost WHERE price = :price")
    int clean(float price);

}
