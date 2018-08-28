package b.udacity.reshu.bakingapp.presenter;

import java.util.List;

import b.udacity.reshu.bakingapp.model.Steps;

/**
 * Created by lenovo-pc on 8/22/2018.
 */


public  interface StepView {

        void displayRecipeSteps(List<Steps> stepsList);
    }

