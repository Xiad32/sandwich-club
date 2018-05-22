package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        Sandwich sandwich = null;
        try {
            sandwich = JsonUtils.parseSandwichJson(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI(sandwich);
        Picasso.with(this)
                .load(sandwich.getImage())
                .into(ingredientsIv);

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI(Sandwich sandwich) {
        TextView AKA = (TextView) findViewById(R.id.aka_tv);
        TextView AKALabel = (TextView) findViewById(R.id.aka_label_tv);
        TextView origin = (TextView) findViewById(R.id.origin_tv);
        TextView originLabel = (TextView) findViewById(R.id.place_of_origin_label_tv);
        TextView description = (TextView) findViewById(R.id.description_tv);
        TextView descriptionLabel = (TextView) findViewById(R.id.description_label_tv);
        TextView ingredients = (TextView) findViewById(R.id.ingredients_tv);
        TextView ingredientsLabel = (TextView) findViewById(R.id.ingedients_label_tv);

        //Populate Single Text Entries:
        if (!sandwich.getPlaceOfOrigin().isEmpty())
            origin.setText(sandwich.getPlaceOfOrigin());
        else
            origin.setVisibility(View.GONE);
            originLabel.setVisibility(View.GONE);

        if (!sandwich.getPlaceOfOrigin().isEmpty())
            description.setText(sandwich.getDescription());
        else {
            description.setVisibility(View.GONE);
            descriptionLabel.setVisibility(View.GONE);
        }

        List list = sandwich.getAlsoKnownAs();
        populateTVwithList(AKA, list, AKALabel);

        list = sandwich.getIngredients();
        populateTVwithList(ingredients, list, ingredientsLabel);

    }

    private void populateTVwithList(TextView tv, List list, TextView tvLabel){
        //Populate list Entries:
        if(list.isEmpty())
        {
            tv.setVisibility(View.GONE);
            tvLabel.setVisibility(View.GONE);
        }
        String listString = "";
        for (int i =0; i< list.size(); i++)
            listString += list.get(i) + "\n";
        tv.setText(listString);
    }
}
