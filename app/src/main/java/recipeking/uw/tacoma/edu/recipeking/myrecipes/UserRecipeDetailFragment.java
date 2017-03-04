package recipeking.uw.tacoma.edu.recipeking.myrecipes;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;

import recipeking.uw.tacoma.edu.recipeking.MainActivity;
import recipeking.uw.tacoma.edu.recipeking.R;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserRecipeDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserRecipeDetailFragment extends Fragment {

    public static final String RECIPE_ITEM_SELECTED = "recipe_selected";

    private static final String RECIPE_DELETE_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam7/removeRecipe.php?";

    private TextView mRecipeTitleTextView;
    private TextView mRecipeDetailsTextView;

    private RecipeDeleteListener mListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface RecipeDeleteListener {
        void deleteRecipe(String url);
    }

    public UserRecipeDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment UserRecipeDetailFragment.
     */
    public static UserRecipeDetailFragment newInstance(Recipe recipe) {
        UserRecipeDetailFragment fragment = new UserRecipeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(RECIPE_ITEM_SELECTED, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_recipe_detail, container, false);

        mRecipeTitleTextView = (TextView) view.findViewById(R.id.user_recipe_title);
        mRecipeDetailsTextView = (TextView) view.findViewById(R.id.user_recipe_details);

        FloatingActionButton floatingActionButton = (FloatingActionButton)
                getActivity().findViewById(R.id.fab);

        Button deleteBtn = (Button) view.findViewById(R.id.recipe_delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = buildRecipeDeleteUrl(v);
                mListener.deleteRecipe(url);

            }
        });

        if (floatingActionButton != null) {
            floatingActionButton.hide();
        }

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
            // Set article based on arguments passed in
            updateView((Recipe) args.getSerializable(RECIPE_ITEM_SELECTED));
        }

    }

    public void updateView(Recipe recipe) {
        if (recipe != null) {
            mRecipeTitleTextView.setText(recipe.getmTitle());
            mRecipeDetailsTextView.setText(recipe.getmRecipeDetails());


        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RecipeDeleteListener) {
            mListener = (RecipeDeleteListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String buildRecipeDeleteUrl(View v) {
        StringBuilder sb = new StringBuilder(RECIPE_DELETE_URL);
        try {
            String recipeTitle = mRecipeTitleTextView.getText().toString();
            sb.append("title=");
            sb.append(URLEncoder.encode(recipeTitle, "UTF-8"));

            String currUser = MainActivity.currentUser;
            sb.append("&user=");
            sb.append(URLEncoder.encode(currUser, "UTF-8"));

            Log.i("UserRecipeDetailFrag", sb.toString());

        } catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

}
