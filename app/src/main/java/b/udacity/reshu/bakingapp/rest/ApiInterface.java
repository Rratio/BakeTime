package b.udacity.reshu.bakingapp.rest;

import java.util.List;

import b.udacity.reshu.bakingapp.model.Cake;
import retrofit2.http.GET;

/**
 * Created by lenovo-pc on 8/8/2018.
 */

public interface ApiInterface {

    @GET("topher/2017/May/59121517_baking/baking.json")
    io.reactivex.Observable<List<Cake>> getRecipes();
}
