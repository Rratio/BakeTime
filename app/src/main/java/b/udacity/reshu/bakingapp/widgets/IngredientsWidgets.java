package b.udacity.reshu.bakingapp.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import b.udacity.reshu.bakingapp.R;
import com.google.gson.Gson;

import java.util.List;

import b.udacity.reshu.bakingapp.activity.MainBakingActivity;
import b.udacity.reshu.bakingapp.model.Cake;
import b.udacity.reshu.bakingapp.model.Ingredients;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgets extends AppWidgetProvider {

    private RemoteViews views;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // There may be multiple widgets active, so update all of them

        for (int appWidgetId : appWidgetIds) {
            views = new RemoteViews(context.getPackageName(), R.layout.activity_ingredients_widgets);
            appWidgetManager.updateAppWidget(appWidgetId, views);

            Intent configIntent = new Intent(context, MainBakingActivity.class);
            PendingIntent configPendingIntent = PendingIntent.getActivity(context, 0, configIntent, 0);
            views.setOnClickPendingIntent(R.id.start_cooking_btn_wgt, configPendingIntent);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        Log.i("IngredientWidget", "onReceive: called");
        String action = intent.getAction();
        views = new RemoteViews(context.getPackageName(), R.layout.activity_ingredients_widgets);
        Intent mainActivityIntent = new Intent(context, MainBakingActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainActivityIntent, 0);
        views.setOnClickPendingIntent(R.id.start_cooking_btn_wgt, pendingIntent);

        views.setTextViewText(R.id.recipe_selected_wgt, getRecipe(context).getName());
        List<Ingredients> ingredientList = getRecipe(context).getIngredients();
        StringBuilder builder = new StringBuilder();
        int pos = 1;
        for (Ingredients currentIngredients : ingredientList) {
            builder.append(pos).append(".").append(currentIngredients.getIngredient()).append(" ").append(currentIngredients.getQuantity()).append(" ").append(currentIngredients.getMeasure()).append("\n");
            pos++;
        }

        views.setTextViewText(R.id.ingredients_text_wgt, builder.toString());

        Log.i("IngredientWidget", "onReceive: " + builder.toString());
        //Now update all widgets
        AppWidgetManager.getInstance(context).updateAppWidget(
                new ComponentName(context, IngredientsWidgets.class), views);


    }

    private Cake getRecipe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("FAVOURITES", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString("favourite_recipe", "");
        return gson.fromJson(json, Cake.class);
    }
}

