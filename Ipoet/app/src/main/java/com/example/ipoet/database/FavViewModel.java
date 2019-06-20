package com.example.ipoet.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class FavViewModel extends AndroidViewModel {
    private FavRepository mRepository;

    private LiveData<List<FavouritePoems>> mAllPoems;
    FavouritePoems favouritePoems;
    public FavViewModel (Application application) {
        super(application);
        mRepository = new FavRepository(application);
        mAllPoems = mRepository.getAllFavData();
    }
    public LiveData<List<FavouritePoems>> getAllWords()
    {
        return mAllPoems;
    }
    public FavouritePoems isItInDatabase(String title)
    {
        favouritePoems = mRepository.isItInDatabase(title);
        return favouritePoems;
    }
    public void insert(FavouritePoems favouritePoems) {
        mRepository.insert(favouritePoems);
    }
    public  void delete(FavouritePoems favouritePoems)
    {
        mRepository.delete(favouritePoems);
    }

}
