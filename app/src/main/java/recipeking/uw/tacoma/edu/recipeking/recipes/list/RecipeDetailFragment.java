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
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeDetailFragment extends Fragment {

    public final static String RECIPE_ITEM_SELECTED = "recipe_selected";

    private TextView mRecipeTitle;
    private ImageView mRecipeImage;
    private TextView mRecipeIngredients;
    private TextView mRecipeInstructionsUrl;
    private Button mFavoriteButton;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment RecipeDetailFragment.
     */
    public static RecipeDetailFragment newInstance() {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

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

    @Override
    public void onStart() {
        super.onStart();
        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }

    }

    public void updateView(final Recipe recipe) {
        if (recipe != null) {
            mRecipeTitle.setText(recipe.getmTitle());

            MyRecipeRecyclerViewAdapter.DownloadImageTask task = new MyRecipeRecyclerViewAdapter
                    .DownloadImageTask(mRecipeImage);

            task.execute(recipe.getmImageUrl());

            String joinIngredients = "";
            for(String s : recipe.getmIngredients()) {
                joinIngredients += s + "\n";
            }

            mRecipeIngredients.setText(joinIngredients);

            //mRecipeInstructionsUrl.setContentDescription(recipe.getmInstructionsUrl());

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

            if (MainActivity.favoriteList.contains(recipe)) {
                mFavoriteButton.setText("Remove from Favorites");
            }

            mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mFavoriteButton.getText().toString().equals("Remove from Favorites")) {
                        MainActivity.removeFromFavorites(recipe);
                        mFavoriteButton.setText("Add to Favorites");
                    } else {
                        MainActivity.addToFavorites(recipe);
                        mFavoriteButton.setText("Remove from Favorites");
                    }

                }
            });

        }
    }


}
