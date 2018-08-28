package b.udacity.reshu.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.TextView;

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

    String recipeName;


    List<Ingredients> ingredientsList;
    IngredientsPresenter ingredientsPresenter;
    Ingredients ingredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_details);
        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);

        recipeName = getIntent().getStringExtra("name");
        recipe_name.setText(recipeName);
        ingredientsPresenter = new IngredientsPresenter(IngredientsDetailsActivity.this, getIngredientList());
        getList();
        openDetails();

    }

    public List<Ingredients> getIngredientList() {
        Bundle bundle = getIntent().getExtras();
        List<Ingredients> ingredients = null;
        if (bundle != null) {
            ingredients = getIntent().getParcelableArrayListExtra("ingredients");
        }
        return ingredients;

    }


    @OnClick(R.id.cook)
    void openDetails() {
        Cake cake = new Cake();
        Intent intent = new Intent(this, StepActivity.class);
        intent.putParcelableArrayListExtra("select", (ArrayList<? extends Parcelable>) cake.getSteps());
        startActivity(intent);
    }


    private void getList() {
        ingredientsPresenter.getRecipeIngredients();
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
