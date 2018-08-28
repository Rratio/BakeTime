package b.udacity.reshu.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.StepFragment;
import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.activity.StepActivity;
import b.udacity.reshu.bakingapp.activity.StepListContainer;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Steps;

/**
 * Created by Reshu-Agarwal on 8/9/2018.
 */


public class StepAdapter extends RecyclerView.Adapter<StepAdapter.MyViewHolder> {

    private Context context;
    private List<Steps> stepsList;
    private  boolean twoPane;


    public StepAdapter(Context context, List<Steps> stepsList, boolean twoPane) {
        this.context = context;
        this.stepsList = stepsList;
        this.twoPane = twoPane;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.step_items, parent, false);

        return new MyViewHolder(view);
    }



    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Steps step = (Steps) view.getTag();
            if (twoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(StepFragment.ITEM_ID, step);
                StepFragment fragment = new StepFragment();
                fragment.setArguments(arguments);
                ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_detail_container, fragment)
                        .commit();
            } else {
                Intent intent = new Intent(context, StepListContainer.class);
                intent.putExtra(StepFragment.ITEM_ID, step);
                context.startActivity(intent);
            }
        }
    };

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Steps steps = stepsList.get(position);

        holder.mShortDescription.setText(steps.getsDescription());
        holder.mLongDescription.setText(steps.getDescription());
        holder.itemView.setTag(stepsList.get(position));
        if (!stepsList.get(position).getThumbnailURL().trim().equals("")) {
            Picasso.with(context).load(stepsList.get(position)
                    .getThumbnailURL()).placeholder(R.drawable.cake)
                    .error(R.drawable.cake)
                    .into(holder.cake_image);
        }
        else{
            Picasso.with(context).load(R.drawable.cake)
                    .into(holder.cake_image);
        }

    }


    @Override
    public int getItemCount() {

        return stepsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mShortDescription, mLongDescription;
        public ImageView cake_image;


        public MyViewHolder(View itemView) {
            super(itemView);

            cake_image = (ImageView) itemView.findViewById(R.id.recipe_video_thumbnail);
            mLongDescription = (TextView) itemView.findViewById(R.id.step_long_description);
            mShortDescription = (TextView) itemView.findViewById(R.id.step_short_description);
            itemView.setOnClickListener(mOnClickListener);

        }


    }
}