package recipeking.uw.tacoma.edu.recipeking.recipes.list;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import recipeking.uw.tacoma.edu.recipeking.MainActivity;
import recipeking.uw.tacoma.edu.recipeking.R;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;

/**
 * Class for RecipeDetailFragment. This class represents the details about a recipe.
 *
 */
public class RecipeDetailFragment extends Fragment {

    /** Constant for which recipe has been selected. */
    public final static String RECIPE_ITEM_SELECTED = "recipe_selected";

    /** TextView element for the recipe's title. */
    private TextView mRecipeTitle;

    /** ImageView element for the recipe's image. */
    private ImageView mRecipeImage;

    /** TextView element for the recipe's ingredients. */
    private TextView mRecipeIngredients;

    /** TextView element for the recipe's instructions URL. */
    private TextView mRecipeInstructionsUrl;

    /** Button element for adding or removing the recipe to the favorite list. */
    private Button mFavoriteButton;


    /**
     *  Required empty public constructor
     */
    public RecipeDetailFragment() {

    }

    /**
     * Method for constructing a new instance of this Fragment.
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance() {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate() method for this Fragment.
     *
     * @param savedInstanceState - the savedInstanceState for this Fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    /**
     *
     * Inflates the view and the view elements for this Fragment.
     *
     * @param inflater - the inflater for this Fragment.
     * @param container - the container for this Fragment.
     * @param savedInstanceState - the savedInstanceState for this Fragment.
     *
     * @return - a view object of this Fragment.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        mRecipeTitle = (TextView) view.findViewById(R.id.recipe_title_tv);
        mRecipeImage = (ImageView) view.findViewById(R.id.recipe_image_iv);
        mRecipeIngredients = (TextView) view.findViewById(R.id.recipe_ingredients);
        mRecipeInstructionsUrl = (TextView) view.findViewById(R.id.recipe_instructions_url);
        mFavoriteButton = (Button) view.findViewById(R.id.favorite_button);

        return view;
    }

    /**
     * onStart() method for this Fragment. Sets article based on argument passed in.
     */
    @Override
    public void onStart() {
        super.onStart();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }

    }

    /**
     * Updates the view for this Fragment based on which recipe has been selected.
     *
     * @param recipe - The recipe that has been selected.
     */
    public void updateView(final Recipe recipe) {
        if (recipe != null) {
            mRecipeTitle.setText(recipe.getmTitle());

            // Downloads the image for the recipe.
            MyRecipeRecyclerViewAdapter.DownloadImageTask task = new MyRecipeRecyclerViewAdapter
                    .DownloadImageTask(mRecipeImage);
            task.execute(recipe.getmImageUrl());


            // Parsing the ingredients from array to a single string with new line character.
            String joinIngredients = "";
            for(String s : recipe.getmIngredients()) {
                joinIngredients += s + "\n";
            }

            mRecipeIngredients.setText(joinIngredients);

            // Opens the recipe's website for instructions based on the URL.
            // Implicit intent for opening a webpage.
            mRecipeInstructionsUrl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = recipe.getmInstructionsUrl();
                    Uri webpage = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    String title = getResources().getString(R.string.chooser_title);
                    Intent chooser = Intent.createChooser(intent, title);

                    if(intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivity(chooser);
                    }
                }
            });

            // If the recipe is already in the favorite list, set the text accordingly.
            if (MainActivity.favoriteList.contains(recipe)) {
                mFavoriteButton.setText(R.string.remove_from_favorites);
            }

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mFavoriteButton.getText().toString().equals("Remove from Favorites")) {
                        // Removes the recipe from the static list.
                        MainActivity.removeFromFavorites(recipe, getContext());
                        mFavoriteButton.setText(R.string.add_to_favorites);
                    } else {
                        // Adds the recipe to the static list.
                        MainActivity.addToFavorites(recipe, getContext());
                        mFavoriteButton.setText(R.string.remove_from_favorites);
                    }

                }
            });

        }
    }

}
