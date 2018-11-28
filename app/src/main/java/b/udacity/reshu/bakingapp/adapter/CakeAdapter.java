package b.udacity.reshu.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.activity.StepActivity;
import b.udacity.reshu.bakingapp.model.Cake;

/**
 * Created by Reshu-Agarwal on 8/9/2018.
 */


public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.MyViewHolder> {

    private Context context;
    private List<Cake> cardModelList;
    private final ItemClickListener mItemClickListener;


    public CakeAdapter(Context context, List<Cake> CardModels, ItemClickListener mItemClickListener) {
        this.context = context;
        this.cardModelList = CardModels;
        this.mItemClickListener = mItemClickListener;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new MyViewHolder(view);
    }


    public interface ItemClickListener {
        void onItemClickListener(int itemId, Cake recipe);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Cake cake = cardModelList.get(position);


        if(!cardModelList.get(position).getImage().trim().equals(""))
        Glide.with(context)
                .load(cardModelList.get(position).getImage())
                .into(holder.movieImg);
        else{
            Glide.with(context)
                    .load(R.drawable.cake).into(holder.movieImg);
        }

        holder.release.setText(cake.getName());

        Log.e("NAME","::::::"+cake.getName());

        holder.ingredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent ingredients = new Intent(context, IngredientsDetailsActivity.class);
                ingredients.putExtra("name",cake.getName());
                ingredients.putParcelableArrayListExtra("ingredients", (ArrayList<? extends Parcelable>) cake.getIngredients());
                ingredients.putParcelableArrayListExtra("select", (ArrayList<? extends Parcelable>) cake.getSteps());
                ingredients.putExtra("recipe", cardModelList.get(position));
                ingredients.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(ingredients);

            }
        });

        holder.step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent detail = new Intent(context, StepActivity.class);
                detail.putParcelableArrayListExtra("select", (ArrayList<? extends Parcelable>) cake.getSteps());
                detail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(detail);
            }
        });

    }

    @Override
    public int getItemCount() {

        return cardModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, release;
        public ImageView movieImg;
        public CardView cardView;
        public Button ingredient, step;


        public MyViewHolder(View itemView) {
            super(itemView);

            movieImg = (ImageView) itemView.findViewById(R.id.thumbnail);
            release = (TextView) itemView.findViewById(R.id.title);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
            ingredient = (Button)itemView.findViewById(R.id.ingredient);
            step = (Button)itemView.findViewById(R.id.steps);
        }

        @Override
        public void onClick(View view) {
            int elementId = cardModelList.get(getAdapterPosition()).getmId();
            Cake cake = cardModelList.get(getAdapterPosition());
            mItemClickListener.onItemClickListener(elementId, cake);
        }
    }
}