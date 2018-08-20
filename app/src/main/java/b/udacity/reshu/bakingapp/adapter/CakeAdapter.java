package b.udacity.reshu.bakingapp.adapter;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Reshu-Agarwal on 8/9/2018.
 */


import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import b.udacity.reshu.bakingapp.R;
import b.udacity.reshu.bakingapp.activity.IngredientsDetailsActivity;
import b.udacity.reshu.bakingapp.model.Cake;


public class CakeAdapter extends RecyclerView.Adapter<CakeAdapter.MyViewHolder> {

    private Context context;
    private List<Cake> cardModelList;


    public CakeAdapter(Context context, List<Cake> CardModels) {
        this.context = context;
        this.cardModelList = CardModels;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new MyViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final Cake cake = cardModelList.get(position);


        Picasso.with(context).load(R.drawable.default_image).into(holder.movieImg);

        holder.release.setText(cake.getName());

        Log.e("NAME","::::::"+cake.getName());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent detail = new Intent(context, IngredientsDetailsActivity.class);
                detail.putExtra("name",cake.getName());
                detail.putExtra("ingredients", String.valueOf(cake.getIngredients()));
                context.startActivity(detail);

            }
        });

    }

    @Override
    public int getItemCount() {

        return cardModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, release;
        public ImageView movieImg;
        public CardView cardView;


        public MyViewHolder(View itemView) {
            super(itemView);

            movieImg = (ImageView) itemView.findViewById(R.id.thumbnail);
            release = (TextView) itemView.findViewById(R.id.title);
            cardView = (CardView) itemView.findViewById(R.id.card_view);

        }


    }
}

