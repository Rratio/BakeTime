package b.udacity.reshu.bakingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import b.udacity.reshu.bakingapp.Database.AppExecutors;
import b.udacity.reshu.bakingapp.Database.IngredientsEntry;
import b.udacity.reshu.bakingapp.Database.RecipeDatabase;
import b.udacity.reshu.bakingapp.Database.RecipeEntry;
import b.udacity.reshu.bakingapp.Database.StepsEntry;
import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.adapter.IngredientsAdapter;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Ingredients;
import b.udacity.reshu.bakingapp.model.Steps;
import b.udacity.reshu.bakingapp.presenter.IngredientsPresenter;
import b.udacity.reshu.bakingapp.presenter.IngredientsView;
import b.udacity.reshu.bakingapp.widgets.RecipeIngredientsService;
import b.udacity.reshu.bakingapp.widgets.RecipesGridViewService;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsDetailsActivity extends AppCompatActivity implements IngredientsView {


    private static final String TAG = "";
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ingredients_list)
    TextView recipe_name;

    @BindView(R.id.cook)
    Button cook_detail;

    @BindView(R.id.widget)
    ImageButton ingredients_widgets;

    @BindView(R.id.cake_image)
    ImageView mCakeImage;

    @BindView(R.id.fab_fav)
    FloatingActionButton fab;

    // Database
    private RecipeDatabase recipeDatabase;
    private RecipeEntry recipeEntry;

    String recipeName;
    Cake recipe;

    IngredientsPresenter ingredientsPresenter;

    //Key used for Intent
    public static String EXTRA_RECIPE_POSITION = "position";
    public static String EXTRA_IS_FROM_WIDGET = "isFromWidget";
    public static final String EXTRA_RECIPE_ID = "recipeID";
    public static final String EXTRA_RECIPE = "recipe";
    private boolean isFav = false;

//    public IngredientsDetailsActivity(Cake recipe) {
//        this.recipe = recipe;
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);
        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);

        recipeName = getIntent().getStringExtra("name");
        recipe_name.setText(recipeName);

        Glide.with(this)
                .load(R.drawable.cake).into(mCakeImage);
        ingredientsPresenter = new IngredientsPresenter(IngredientsDetailsActivity.this, getIngredientList());
        ingredientsPresenter.getRecipeIngredients();

        cook_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(IngredientsDetailsActivity.this, StepActivity.class);
                intent.putParcelableArrayListExtra("select", (ArrayList<? extends Parcelable>) getSelectedSteps());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

        ingredients_widgets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addToFavourites();
            }
        });
    }

    public List<Ingredients> getIngredientList() {
        Bundle bundle = getIntent().getExtras();
        List<Ingredients> ingredients = null;
        if (bundle != null) {
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
        }
        return ingredients;

    }

    public void addToFavourites() {
        SharedPreferences.Editor editor = getSharedPreferences("FAVOURITES", MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(getSelectedRecipe());
        editor.putString("favourite_recipe", json);
        editor.commit();
        Toast.makeText(getApplicationContext(), "You can now add widget in your Home Screen", Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.fab_fav)
    public void onClickFAB(View view) {

        //Write into Database

        if (isFav) {
            //Delete the contents from Database
            if (recipe != null) {
                AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        recipeDatabase.recipesDao().deleteRecipe(recipeEntry);
                        recipeDatabase.recipesDao().deleteIngredients(recipe.getmId());
                        recipeDatabase.recipesDao().deleteSteps(recipe.getmId());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                fab.setImageResource(R.drawable.favorite_border_primary_24dp);
                                //Update the Widgets
                            }
                        });
                    }
                });

                isFav = false;
                RecipeIngredientsService.startQueryingNumberOfRecipes(getApplicationContext());
                RecipesGridViewService.startQueryingRecipes(getApplicationContext());
            }
        } else {

            Bitmap bitmapRecipeImage = mCakeImage.getDrawingCache();
            //Convert Recipe POJO into RecipeEntry POJO
            final RecipeEntry recipeEntry = new RecipeEntry(recipe.getmId(),
                    recipe.getName(),
                    recipe.getServing(),
                    recipe.getImage(),
                    getBitmapAsByteArray(bitmapRecipeImage));
            this.recipeEntry = recipeEntry;

            //Convert List of Ingredient POJO into List of IngredientsEntry POJO
            final List<IngredientsEntry> ingredientsEntries = new ArrayList<>();
            Log.d(TAG, "Size - Ingredients - " + recipe.getIngredients().size());
            for (Ingredients ingredient : recipe.getIngredients()) {
                ingredientsEntries.add(new IngredientsEntry(recipe.getmId()
                        , ingredient.getQuantity()
                        , recipe.getName()
                        , ingredient.getMeasure()
                        , ingredient.getIngredient()));
            }

            //Convert List of Step POJO into List of StepsEntry POJO
            final List<StepsEntry> stepsEntries = new ArrayList<>();
            for (int i = 0; i < recipe.getSteps().size(); i++) {
                Steps step = recipe.getSteps().get(i);
                stepsEntries.add(new StepsEntry(step.getmId(),
                        step.getsDescription(),
                        step.getDescription(),
                        step.getVideoURL(),
                        step.getThumbnailURL()));
                Log.d(TAG, "Step - RecipeId" + recipe.getmId());
            }

            // After converting all the POJOs write into Database
            AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
                @Override
                public void run() {
                    recipeDatabase.recipesDao().insertRecipe(recipeEntry);
                    recipeDatabase.recipesDao().insertIngredients(ingredientsEntries);
                    recipeDatabase.recipesDao().insertSteps(stepsEntries);
                    Log.d(TAG, "Size - stepsEntries" + stepsEntries.size());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            fab.setImageResource(R.drawable.favorite_primary_24dp);
                            Toast.makeText(IngredientsDetailsActivity.this, R.string.toast_msg_added_as_favorite, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            isFav = true;

            //Update the Widgets
            RecipeIngredientsService.startQueryingNumberOfRecipes(getApplicationContext());
            RecipesGridViewService.startQueryingRecipes(getApplicationContext());
        }
    }

    //Helper Method to convert Bitmap into ByteArray
    public static byte[] getBitmapAsByteArray(Bitmap bitmap){
        if (bitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            return outputStream.toByteArray();
        }
        return null;
    }

    private Cake getSelectedRecipe() {
        Bundle data = getIntent().getExtras();
        Cake cake = null;
        if (data != null) {
            cake = data.getParcelable("recipe");
        }
        return cake;
    }

    public List<Steps> getSelectedSteps() {
        Bundle bundle = getIntent().getExtras();
        List<Steps> steps = null;
        if (bundle != null) {
            steps = getIntent().getParcelableArrayListExtra("select");
        }
        return steps;

    }

    @Override
    public void displayIngredientslist(List<Ingredients> ingredientsList) {

        if (ingredientsList != null) {

            IngredientsAdapter adapter = new IngredientsAdapter(this, ingredientsList);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}