package recipeking.uw.tacoma.edu.recipeking;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import recipeking.uw.tacoma.edu.recipeking.login.LoginActivity;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String BASE_FAV_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam7/favorites_list.php?";

    private static final String ADD_FAV_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam7/addFavorite.php?";

    private static final String REMOVE_FAV_URL
            = "http://cssgate.insttech.washington.edu/~_450bteam7/removeFavorite.php?";

    public static String currentUser;

    public static List<Recipe> favoriteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            DownloadFavoritesTask task = new DownloadFavoritesTask();
            task.execute(new String[]{buildFavUlr()});
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showCategoriesActivity(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        startActivity(intent);
    }

    public void showFavoritesActivity(View view) {
        Intent intent = new Intent(this, FavoritesActivity.class);
        startActivity(intent);
    }

    public void showMyRecipiesActivity(View view) {
        Intent intent = new Intent(this, MyRecipesActivity.class);
        startActivity(intent);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browse_categories) {
            // Handle the camera action
            showCategoriesActivity(null);
        } else if (id == R.id.nav_favorites) {
            showFavoritesActivity(null);
        } else if (id == R.id.nav_my_recipes) {
            showMyRecipiesActivity(null);
        } else if (id == R.id.nav_menu4) {
            currentUser = null;
            favoriteList = null;
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void addToFavorites(Recipe recipe) {
        // Add to local list
        MainActivity.favoriteList.add(recipe);

        // Add to the Server
        AddToFavoritesList task = new AddToFavoritesList();
        task.execute(buildAddFavUlr(recipe));
    }


    public static void removeFromFavorites(Recipe recipe) {
        // Remove from local list
        if (!MainActivity.favoriteList.isEmpty()) {
            MainActivity.favoriteList.remove(recipe);
        }

        // Remove from Server
        AddToFavoritesList task = new AddToFavoritesList();
        task.execute(buildRemoveFavUrl(recipe));

    }

    private String buildFavUlr() {
        StringBuilder sb = new StringBuilder(BASE_FAV_URL);

        try {
            String user = MainActivity.currentUser;
            sb.append("usr=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("Recipe(FavList)Fragment", sb.toString());

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),
                    "Something wrong with the favlist url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    private static String buildAddFavUlr(Recipe recipe) {
        StringBuilder sb = new StringBuilder(ADD_FAV_URL);

        try {
            String title = recipe.getmTitle();
            sb.append("title=");
            sb.append(URLEncoder.encode(title, "UTF-8"));

            String user = MainActivity.currentUser;
            sb.append("&user=");
            sb.append((URLEncoder.encode(user, "UTF-8")));

            String url = recipe.getmImageUrl();
            sb.append("&image_url=");
            sb.append(URLEncoder.encode(url, "UTF-8"));

            String joinIngredients = "";
            for(String s : recipe.getmIngredients()) {
                joinIngredients += s + "\n";
            }
            sb.append("&ingredients=");
            sb.append(URLEncoder.encode(joinIngredients, "UTF-8"));

            String instructions_url = recipe.getmInstructionsUrl();
            sb.append("&instructions_url=");
            sb.append(URLEncoder.encode(instructions_url, "UTF-8"));

            Log.i("MainActivity AddFavURL", sb.toString());


        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    "Something wrong with the AddFavlist url" + e.getMessage(),
//                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    private static String buildRemoveFavUrl(Recipe recipe) {
        StringBuilder sb = new StringBuilder(REMOVE_FAV_URL);

        try {
            String user = MainActivity.currentUser;
            sb.append("user=");
            sb.append((URLEncoder.encode(user, "UTF-8")));

            String title = recipe.getmTitle();
            sb.append("&title=");
            sb.append((URLEncoder.encode(title, "UTF-8")));

        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(),
//                    "Something wrong with the RemoveFavlist url" + e.getMessage(),
//                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    private class DownloadFavoritesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        result.append(s);
                    }
                } catch (Exception e) {
                    result = new StringBuilder("Unable to download the list of recipes, Reason: " + e
                            .getMessage());
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return result.toString();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            favoriteList = new ArrayList<>();
            result = Recipe.parseFavRecipesJSON(result, favoriteList);

            if(result != null) {
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

        }
    }

    private static class AddToFavoritesList extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            StringBuilder result = new StringBuilder();
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        result.append(s);
                    }
                } catch (Exception e) {
                    result = new StringBuilder("Unable to add recipe to favorites list, Reason: "
                            + e
                            .getMessage());
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return result.toString();
        }


        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
//            try {
//
//                JSONObject jsonObject = new JSONObject(result);
//                String status = (String) jsonObject.get("result");
//                if (status.equals("success")) {
//                    Toast.makeText(getApplicationContext(), "Added to favorites"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                } else if (status.equals("success2")) {
//                    Toast.makeText(getApplicationContext(), "Removed to favorites"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    Toast.makeText(getApplicationContext(), "Failed to add(remove): "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getApplicationContext(), "Something wrong with the data" +
//                        e.getMessage(), Toast.LENGTH_LONG).show();
//            }
        }


    }

}
