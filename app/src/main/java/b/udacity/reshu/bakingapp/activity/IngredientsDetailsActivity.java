package b.udacity.reshu.bakingapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.adapter.IngredientsAdapter;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Ingredients;
import b.udacity.reshu.bakingapp.model.Steps;
import b.udacity.reshu.bakingapp.presenter.IngredientsPresenter;
import b.udacity.reshu.bakingapp.presenter.IngredientsView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IngredientsDetailsActivity extends AppCompatActivity implements IngredientsView {


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

    String recipeName;

    IngredientsPresenter ingredientsPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);
        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);

        recipeName = getIntent().getStringExtra("name");
        recipe_name.setText(recipeName);


        Picasso.with(this).load(R.drawable.cake).into(mCakeImage);
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
        Toast.makeText(getApplicationContext(),"You can now add widget in your Home Screen", Toast.LENGTH_LONG).show();
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