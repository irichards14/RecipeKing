package recipeking.uw.tacoma.edu.recipeking;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import recipeking.uw.tacoma.edu.recipeking.recipes.list.RecipeFragment;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;

public class RecipesListActivity extends AppCompatActivity implements
        RecipeFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_list);
        Intent intent = getIntent();
        //Message from the ImageView that called this activity.
        String message = intent.getStringExtra(CategoriesActivity.EXTRA_MESSAGE);


        if ((savedInstanceState == null
            && findViewById(R.id.activity_recipes_list_fragment_container) != null)
                || getSupportFragmentManager().findFragmentById(R.id.list) == null) {

            RecipeFragment recipeFragment = RecipeFragment.newInstance(message);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_recipes_list_fragment_container, recipeFragment)
                    .commit();
        }

    }


    @Override
    public void onListFragmentInteraction(Recipe item) {

    }
}
