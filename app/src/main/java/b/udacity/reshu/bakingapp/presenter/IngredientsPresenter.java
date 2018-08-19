package b.udacity.reshu.bakingapp.presenter;

import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.model.Cake;

/**
 * Created by lenovo-pc on 8/17/2018.
 */

public class IngredientsPresenter {

    private IngredientsView ingredientsView;
    private Cake recipe;


    public IngredientsPresenter(IngredientsDetailsActivity recipeDetailsActivity, Cake selectedRecipe) {
        this.ingredientsView = (IngredientsView) recipeDetailsActivity;
        this.recipe = selectedRecipe;
    }

    public void getRecipeIngredients() {
        ingredientsView.displayIngredientslist(recipe);
    }
}
