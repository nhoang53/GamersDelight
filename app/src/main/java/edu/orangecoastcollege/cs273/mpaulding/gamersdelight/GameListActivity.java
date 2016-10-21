package edu.orangecoastcollege.cs273.mpaulding.gamersdelight;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.List;

public class GameListActivity extends AppCompatActivity {

    private DBHelper db;
    private List<Game> gamesList;
    private GameListAdapter gamesListAdapter;
    private ListView gamesListView;

    private EditText gameNameEditText;
    private EditText gameDescriptionEditText;
    private RatingBar gameRatingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_list);

        this.deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);

        db.addGame(new Game("League of Legends", "Multiplayer online battle arena", 4.5f, "lol.png"));
        db.addGame(new Game("Dark Souls III", "Action role-playing", 4.0f, "ds3.png"));
        db.addGame(new Game("The Division", "Single player experience", 3.5f, "division.png"));
        db.addGame(new Game("Doom FLH", "First person shooter", 2.5f, "doomflh.png"));
        db.addGame(new Game("Battlefield 1", "Single player campaign", 5.0f, "battlefield1.png"));

        // TODO:  Populate all games from the database into the list
        gamesList = db.getAllGames();

        // TODO:  Create a new ListAdapter connected to the correct layout file and list
        gamesListAdapter = new GameListAdapter(this, R.layout.game_list_item, gamesList);

        // TODO:  Connect the ListView with the ListAdapter
        // connect ListView with the View Layout
        gamesListView = (ListView) findViewById(R.id.gameListView);
        gamesListView.setAdapter(gamesListAdapter);

        gameDescriptionEditText = (EditText) findViewById(R.id.descriptionEditText);
        gameNameEditText = (EditText) findViewById(R.id.nameEditText);
        gameRatingBar = (RatingBar) findViewById(R.id.gameRatingBar);
    }

    public void viewGameDetails(View view) {

        // TODO: Use an Intent to start the GameDetailsActivity with the data it needs to correctly inflate its views.
        // change View to LinearLayout
       if(view instanceof LinearLayout)
       {
           LinearLayout selectedLinearLayout = (LinearLayout) view;
           /**
            * Tag-property was set in GameListAdapter
            */
           Game selectedGame = (Game) selectedLinearLayout.getTag(); // setTag in GameListAdapter.java

           Log.e("Id is: ", selectedGame.getId() + "");

           Intent detailsIntent = new Intent(this, GameDetailsActivity.class);
           detailsIntent.putExtra("IdNumber", String.valueOf(selectedGame.getId()));
           detailsIntent.putExtra("Name", selectedGame.getName());
           detailsIntent.putExtra("Description", selectedGame.getDescription());
           // Remember to cast data type to String
           detailsIntent.putExtra("Rating", String.valueOf(selectedGame.getRating()));
           detailsIntent.putExtra("ImageName", selectedGame.getImageName());

           startActivity(detailsIntent);
       }
    }

    public void addGame(View view)
    {
        // TODO:  Add a game to the database, list, list adapter
        // TODO:  Make sure the list adapter is updated
        // TODO:  Clear all entries the user made (edit text and rating bar)

        String des = gameDescriptionEditText.getText().toString();
        String name = gameNameEditText.getText().toString();
        float rating = gameRatingBar.getRating();

        if(des.isEmpty()){
            Toast.makeText(this, "Description cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else if(name.isEmpty()){
            Toast.makeText(this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else if(rating == 0){
            Toast.makeText(this, "Rating Bar cannot be empty.", Toast.LENGTH_SHORT).show();
        }
        else{
            Game newGame = new Game(name, des, rating, name);
            gamesListAdapter.add(newGame); // add newGame to the Adapter so it can be displayed

            db.addGame(newGame);

            Toast.makeText(this, name + " was added to the List.", Toast.LENGTH_SHORT).show();

            gameDescriptionEditText.setText("");
            gameNameEditText.setText("");
            gameRatingBar.setRating(0);
        }

    }

    public void clearAllGames(View view)
    {
        // TODO:  Delete all games from the database and lists
        gamesList.clear();
        db.deleteAllGames();
       // gamesListAdapter.notifyDataSetChanged();
        gamesListAdapter.clear(); // either way is fine

        Toast.makeText(this, "All game was deleted.", Toast.LENGTH_SHORT).show();
    }

}
