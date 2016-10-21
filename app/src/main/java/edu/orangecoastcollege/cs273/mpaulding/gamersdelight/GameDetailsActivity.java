package edu.orangecoastcollege.cs273.mpaulding.gamersdelight;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;

public class GameDetailsActivity extends AppCompatActivity implements OnRatingBarChangeListener  {//AppCompatActivity

    private TextView gameListNameTextView;
    private TextView gameListDescriptionTextView;
    private RatingBar gameListRatingBar;
    private ImageView gameListImageView;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_details);

        // TODO:  Use the Intent object sent from GameListActivity to populate the Views on
        // TODO:  this layout, including the TextViews, RatingBar and ImageView with the Game details.
        gameListImageView = (ImageView) findViewById(R.id.gameDetailsImageView);
        gameListDescriptionTextView = (TextView) findViewById(R.id.gameDetailsDescriptionTextView);
        gameListNameTextView = (TextView) findViewById(R.id.gameDetailsNameTextView);
        gameListRatingBar = (RatingBar) findViewById(R.id.gameDetailsRatingBar);

        // Get data from Intent
        Intent detailIntent = getIntent();
        gameListNameTextView.setText(detailIntent.getStringExtra("Name"));
        gameListDescriptionTextView.setText(detailIntent.getStringExtra("Description"));
        gameListRatingBar.setRating(Float.parseFloat(detailIntent.getStringExtra("Rating")));

        try{
            id = Integer.parseInt(detailIntent.getStringExtra("IdNumber"));
        }catch (NumberFormatException num){
            Log.e("ID error. " + detailIntent.getStringExtra("IdNumber"),num.getMessage());
        }

        String imageName = detailIntent.getStringExtra("ImageName");
        AssetManager am = this.getAssets();
        try{
            InputStream stream = am.open(imageName);
            Drawable drawable = Drawable.createFromStream(stream, "Gamers Delight");
            gameListImageView.setImageDrawable(drawable);
        }catch (IOException ex){
            Log.e("GamerDelight:", "Cannot load image:" + detailIntent.getStringExtra("ImageName") + ex);
        }

        gameListRatingBar.setOnRatingBarChangeListener(this);

    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating,
                                boolean fromTouch) {
        /*final int numStars = ratingBar.getNumStars();
        Toast.makeText(this, "Rating bar was update." + numStars, Toast.LENGTH_SHORT).show();*/

        Log.e("Rating bar: ", rating + "");
        Game updateGame = new Game(id, gameListNameTextView.getText().toString(),
                gameListDescriptionTextView.getText().toString(), rating, gameListImageView.toString());

        DBHelper db = new DBHelper(this);
        db.updateGame(updateGame);
    }
}
