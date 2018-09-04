package b.udacity.reshu.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.StepFragment;
import b.udacity.reshu.bakingapp.adapter.StepAdapter;
import b.udacity.reshu.bakingapp.model.Steps;
import b.udacity.reshu.bakingapp.presenter.StepPresenter;
import b.udacity.reshu.bakingapp.presenter.StepView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class StepActivity extends AppCompatActivity implements StepView {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private boolean mTwoPane;

    StepPresenter stepPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ButterKnife.bind(this);
        toolbar.setTitle(" Baking App");
        setSupportActionBar(toolbar);

        if (findViewById(R.id.step_detail_container) != null) {
            mTwoPane = true;
            //Show default fragment in tablet
            Bundle arguments = new Bundle();
            Steps step = new Steps();
            arguments.putParcelable(StepFragment.ITEM_ID, step);
            StepFragment fragment = new StepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }

        stepPresenter = new StepPresenter(StepActivity.this,  getSelectedSteps());
        getSteps();
    }


    public List<Steps> getSelectedSteps() {
        Bundle bundle = getIntent().getExtras();
        List<Steps> steps = null;
        if (bundle != null) {
            steps = getIntent().getParcelableArrayListExtra("select");
        }
        return steps;
    }

    private void getSteps() {
        stepPresenter.getSelectedSteps();
    }

    @Override
    public void displayRecipeSteps(List<Steps> stepsList) {

        if (stepsList != null) {

            StepAdapter adapter = new StepAdapter(this, stepsList, mTwoPane);
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
