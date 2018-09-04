package b.udacity.reshu.bakingapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.model.Ingredients;

/**
 * Created by Reshu-Agarwal on 8/9/2018.
 */


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.MyViewHolder> {

    private Context context;
    private List<Ingredients> cardModelList;

    public IngredientsAdapter(Context context, List<Ingredients> CardModels) {
        this.context = context;
        this.cardModelList = CardModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredients_list_item, parent, false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Ingredients ingredients = cardModelList.get(position);

        StringBuilder stringBuilder = new StringBuilder();




         holder.details.setText(stringBuilder.append(position + 1).append(".").append(ingredients.getIngredient()).append(" ").append(ingredients.getQuantity()).append(" ").append(ingredients.getMeasure())
        );

    }

    @Override
    public int getItemCount() {

        return cardModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mCakeImage;
        public TextView details;

        public MyViewHolder(View itemView) {
            super(itemView);

            mCakeImage = (ImageView) itemView.findViewById(R.id.cake_image);
            details = (TextView) itemView.findViewById(R.id.all_content);


        }


    }
}

