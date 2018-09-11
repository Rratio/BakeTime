package b.udacity.reshu.bakingapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.StepFragment;

/**
 * Created by Reshu-Agarwal on 8/28/2018.
 */

public class StepListContainer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.step_container);

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            arguments.putParcelable(StepFragment.ITEM_ID,
                    getIntent().getExtras().getParcelable(StepFragment.ITEM_ID));
            StepFragment fragment = new StepFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_detail_container, fragment)
                    .commit();
        }
    }

}
