package b.udacity.reshu.bakingapp.activity;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;
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

    private CakeAdapter adapter;
    private List<Cake> modelList = new ArrayList<>();

    ApiInterface apiInterface;
    MainPresenter mainPresenter;
    private String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_baking);
        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);

        mainPresenter = new MainPresenter(this);

        apiInterface = ApiClient.getClient().create(ApiInterface.class);

        getRecipeList();

    }

    private void getRecipeList() {
        mainPresenter.getRecipes();
    }

    @Override
    public void displayRecipes(List<Cake> recipes) {

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        CakeAdapter adapter = new CakeAdapter(this, recipes, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();


        List<String> strings = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            strings.add(recipes.get(i).getName());
        }

    }


    @Override
    public void displayError(String e) {

        Toast.makeText(MainBakingActivity.this, e, Toast.LENGTH_LONG).show();

    }


    @Override
    public void onItemClickListener(int itemId, Cake recipe) {

    }


    private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int count, space;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int count, int space, boolean includeEdge) {
            this.count = count;
            this.space = space;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % count;

            if (includeEdge) {
                outRect.left = space - column * space / count;
                outRect.right = (column + 1) * space / count;

                if (position < count) {
                    outRect.top = space;
                }
                outRect.bottom = space;
            } else {
                outRect.left = column * space / count;
                outRect.right = space - (column + 1) * space / count;

                if (position > count) {
                    outRect.top = space;
                }
            }

        }
    }

    private int dpToPx(int i) {
        Resources res = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, res.getDisplayMetrics()));
    }


}
