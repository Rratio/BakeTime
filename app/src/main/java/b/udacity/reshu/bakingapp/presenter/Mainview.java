package b.udacity.reshu.bakingapp.presenter;

import java.util.List;

import b.udacity.reshu.bakingapp.model.Cake;

/**
 * Created by lenovo-pc on 8/9/2018.
 */

public interface Mainview {
    void displayRecipes(List<Cake> recipes);

    void displayError(String e);

}
