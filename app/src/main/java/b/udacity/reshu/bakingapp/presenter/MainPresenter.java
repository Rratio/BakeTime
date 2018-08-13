package b.udacity.reshu.bakingapp.presenter;

import android.util.Log;

import java.util.List;

import b.udacity.reshu.bakingapp.activity.MainBakingActivity;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.rest.ApiClient;
import b.udacity.reshu.bakingapp.rest.ApiInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * Created by lenovo-pc on 8/9/2018.
 */

public class MainPresenter {

    private Mainview mainView;

    public MainPresenter(MainBakingActivity mainViewInterface) {
        this.mainView = (Mainview) mainViewInterface;
    }

    public void getRecipes() {
        getObservable().subscribe(getObserver());
    }

    private io.reactivex.Observable<List<Cake>> getObservable() {
        return ApiClient.getClient().create(ApiInterface.class)
                .getRecipes()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private io.reactivex.Observer<List<Cake>> getObserver() {
        return new Observer<List<Cake>>() {
            @Override
            public void onSubscribe(Disposable d) {

            }


            @Override
            public void onNext(List<Cake> recipes) {
                mainView.displayRecipes(recipes);
            }

            @Override
            public void onError(Throwable e) {
                Log.i(TAG, "onError: " + e.getMessage());
                Log.i(TAG, "onError: " + e.getLocalizedMessage());
                e.printStackTrace();
                mainView.displayError("Error fetching Cake details");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: Completed");
            }
        };
    }
}