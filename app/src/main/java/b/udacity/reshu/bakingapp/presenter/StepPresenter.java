package b.udacity.reshu.bakingapp.presenter;

import java.util.List;

import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.activity.StepActivity;
import b.udacity.reshu.bakingapp.model.Ingredients;
import b.udacity.reshu.bakingapp.model.Steps;

/**
 * Created by lenovo-pc on 8/22/2018.
 */

public class StepPresenter {

    private StepView stepView;
    private List<Steps> stepsList;


    public StepPresenter(StepActivity stepDetailsActivity, List<Steps> stepsList) {
        this.stepView = (StepView) stepDetailsActivity;
        this.stepsList = stepsList;
    }

    public void getSelectedSteps() {
        stepView.displayRecipeSteps(stepsList);
    }
}
