package b.udacity.reshu.bakingapp.presenter;

import java.util.List;

import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Ingredients;

/**
 * Created by lenovo-pc on 8/17/2018.
 */

public class IngredientsPresenter {

    private IngredientsView ingredientsView;
    private List<Ingredients> ingredientsList;


    public IngredientsPresenter(IngredientsDetailsActivity recipeDetailsActivity, List<Ingredients> ingredientsList) {
        this.ingredientsView = (IngredientsView) recipeDetailsActivity;
        this.ingredientsList = ingredientsList;
    }

    public void getRecipeIngredients() {
        ingredientsView.displayIngredientslist(ingredientsList);
    }
}
