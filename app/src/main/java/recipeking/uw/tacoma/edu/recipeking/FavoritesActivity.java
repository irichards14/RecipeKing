package recipeking.uw.tacoma.edu.recipeking;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import recipeking.uw.tacoma.edu.recipeking.recipes.list.RecipeDetailFragment;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.RecipeFragment;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;

public class FavoritesActivity extends AppCompatActivity implements
        RecipeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if ((savedInstanceState == null
                && findViewById(R.id.activity_favorites_fragment_container) != null)
                || getSupportFragmentManager().findFragmentById(R.id.list) == null) {

            ImageView img = (ImageView) findViewById(R.id.activity_favorites_nofav_image);
            img.setVisibility(View.GONE);

            RecipeFragment recipeFragment = RecipeFragment.newInstance("", true);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_favorites_fragment_container, recipeFragment)
                    .commit();

        }

    }

    @Override
    public void onListFragmentInteraction(Recipe item) {
        RecipeDetailFragment recipeDetailFragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RecipeDetailFragment.RECIPE_ITEM_SELECTED, item);
        recipeDetailFragment.setArguments(args);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_favorites_fragment_container, recipeDetailFragment)
                .addToBackStack(null)
                .commit();
    }

//    public void openInstructionsUrl(View view) {
//        String url = view.getContentDescription().toString();
//        Uri webpage = Uri.parse(url);
//        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
//        String title = getResources().getString(R.string.chooser_title);
//        Intent chooser = Intent.createChooser(intent, title);
//
//        if(intent.resolveActivity(getPackageManager()) != null) {
//            startActivity(chooser);
//        }
//    }

}
