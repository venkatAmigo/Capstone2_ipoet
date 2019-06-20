package com.example.ipoet.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavRepository {

    private FavDao mFavDao;
    private LiveData<List<FavouritePoems>> mAllPoems;
    FavRepository(Application application) {
        FavRoomDatabase db = FavRoomDatabase.getDatabase(application);
        mFavDao = db.movDao();
       mAllPoems = mFavDao.getAllFavData();
    }

    public LiveData<List<FavouritePoems>>getAllFavData() {
        return mAllPoems;
    }
    public void insert (FavouritePoems favouritePoems) {
        new insertAsyncTask(mFavDao).execute(favouritePoems);
    }
    public void delete(FavouritePoems favouritePoems)
    {
        new deleteAsyncTask(mFavDao).execute(favouritePoems);
    }
    FavouritePoems isItInDatabase(String title)
    {
        return mFavDao.isItInDatabase(title);
    }
    private static class deleteAsyncTask extends AsyncTask<FavouritePoems, Void, Void> {

        private FavDao mAsyncTaskDao;

        deleteAsyncTask(FavDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavouritePoems... params) {
            mAsyncTaskDao.deleteData(params[0]);
            return null;
        }
    }

    private static class insertAsyncTask extends AsyncTask<FavouritePoems, Void, Void> {

        private FavDao mAsyncTaskDao;

        insertAsyncTask(FavDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final FavouritePoems... params) {
            mAsyncTaskDao.insertFav(params[0]);
            return null;
        }
    }

}
