package b.udacity.reshu.bakingapp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.adapter.CakeAdapter;
import b.udacity.reshu.bakingapp.adapter.IngredientsAdapter;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Ingredients;
import b.udacity.reshu.bakingapp.presenter.IngredientsPresenter;
import b.udacity.reshu.bakingapp.presenter.IngredientsView;
import b.udacity.reshu.bakingapp.presenter.MainPresenter;
import butterknife.BindView;
import butterknife.ButterKnife;

public class IngredientsDetailsActivity extends AppCompatActivity implements IngredientsView{


    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.ingredients_list)
    TextView recipe_name;

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
        ingredientsPresenter = new IngredientsPresenter(IngredientsDetailsActivity.this,getIngredientList());
        getList();

    }

    public Cake getIngredientList(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        ingredientsList = null;
        if(bundle != null) {
            ingredientsList  =  intent.getParcelableArrayListExtra("ingredients");
        }
        return new Cake();

    }

    private void getList() {
        ingredientsPresenter.getRecipeIngredients();
    }


    @Override
    public void displayIngredientslist(Cake cake) {

        cake = new Cake();

        if(cake.getIngredients()!= null) {

            IngredientsAdapter adapter = new IngredientsAdapter(this, cake.getIngredients());
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

}
