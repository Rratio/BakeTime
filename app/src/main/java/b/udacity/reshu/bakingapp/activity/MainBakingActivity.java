package b.udacity.reshu.bakingapp.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.adapter.CakeAdapter;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.presenter.MainPresenter;
import b.udacity.reshu.bakingapp.presenter.Mainview;
import b.udacity.reshu.bakingapp.rest.ApiClient;
import b.udacity.reshu.bakingapp.rest.ApiInterface;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainBakingActivity extends AppCompatActivity implements Mainview, CakeAdapter.ItemClickListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    ApiInterface apiInterface;
    public int state;
    MainPresenter mainPresenter;
    @Nullable
    private IdlingResource mIdlingResource;
    private FirebaseAnalytics mFirebaseAnalytics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_baking);
        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mainPresenter = new MainPresenter(this);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "name");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        if (isTablet(this) || getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            state = 1;
        } else {
            state = 0;
        }

        getRecipeList();

    }

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new IdlingResource() {
                @Override
                public String getName() {
                    return null;
                }

                @Override
                public boolean isIdleNow() {
                    return false;
                }

                @Override
                public void registerIdleTransitionCallback(ResourceCallback callback) {

                }
            };
        }
        return mIdlingResource;
    }

    private void getRecipeList() {
        mainPresenter.getRecipes();
    }

    @Override
    public void displayRecipes(List<Cake> recipes) {

        if (recipes != null) {
            CakeAdapter adapter = new CakeAdapter(this, recipes, this);

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();


            List<String> strings = new ArrayList<>();
            for (int i = 0; i < adapter.getItemCount(); i++) {
                strings.add(recipes.get(i).getName());
            }
        } else {
            Toast.makeText(MainBakingActivity.this, "No recipes available", Toast.LENGTH_LONG).show();
        }

    }


    private boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }


    private void setupViews() {
        if (state == 0) {
            recyclerView.setLayoutManager(new LinearLayoutManager(MainBakingActivity.this));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(MainBakingActivity.this, 2));
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            state = 1;
            setupViews();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            state = 0;
            setupViews();
        }
    }

    @Override
    public void displayError(String e) {

        Toast.makeText(MainBakingActivity.this, e, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onItemClickListener(int itemId, Cake recipe) {

    }
}
