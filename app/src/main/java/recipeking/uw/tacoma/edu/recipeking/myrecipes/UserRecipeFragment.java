package recipeking.uw.tacoma.edu.recipeking.myrecipes;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import recipeking.uw.tacoma.edu.recipeking.MainActivity;
import recipeking.uw.tacoma.edu.recipeking.R;
import recipeking.uw.tacoma.edu.recipeking.data.RecipeDB;
import recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe.Recipe;
import recipeking.uw.tacoma.edu.recipeking.utils.NetworkUtils;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class UserRecipeFragment extends Fragment {


    private static final String ARG_COLUMN_COUNT = "column-count";

    private static final String MY_RECIPE_LIST_URL =
            "http://cssgate.insttech.washington.edu/~_450bteam7/myrecipes_list.php?";

    private int mColumnCount = 1;

    private OnListFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;


    private List<Recipe> mRecipeList;

    /** Local recipe database storing info when network connection is unavailable. */
    private RecipeDB mRecipeDB;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserRecipeFragment() {
    }


    public static UserRecipeFragment newInstance(int columnCount) {
        UserRecipeFragment fragment = new UserRecipeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_userrecipe_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            if (NetworkUtils.networkConnectionAvailable(getActivity())) {
                DownloadRecipeTask task = new DownloadRecipeTask();
                String url = buildMyRecipeListUrl();
                task.execute(new String[]{url});
            } else {
                Toast.makeText(getActivity(), "No network connection available. " +
                                "Displaying locally stored data",
                        Toast.LENGTH_SHORT) .show();
                //Fill the local list of recipe with data from the local Recipe database
                if(mRecipeDB == null) {
                       mRecipeDB = new RecipeDB(getActivity());
                }
                if (mRecipeList == null) {
                       mRecipeList = mRecipeDB.getRecipes();
                }
                mRecyclerView.setAdapter(new MyUserRecipeRecyclerViewAdapter(mRecipeList, mListener));

            }

        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        if (fab != null) {
            fab.show();
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Recipe item);
    }

    private String buildMyRecipeListUrl() {
        StringBuilder sb = new StringBuilder(MY_RECIPE_LIST_URL);

        try {
            String user = MainActivity.currentUser;
            sb.append("user=");
            sb.append(URLEncoder.encode(user, "UTF-8"));

            Log.i("UserRecipeFragment", sb.toString());

        } catch (Exception e) {
            Toast.makeText(getActivity(),
                    "Something wrong with the myrecipelist url" + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        return sb.toString();
    }

    private class DownloadRecipeTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of recipes, Reason: " + e.getMessage();
                }
                finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            mRecipeList = new ArrayList<Recipe>();
            result = Recipe.parseMyRecipesJSON(result, mRecipeList);

            if (result != null) {

                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!mRecipeList.isEmpty()) {

                //IMPLEMENT SQLITE DATABASE
                if (mRecipeDB == null) {
                    mRecipeDB = new RecipeDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mRecipeDB.deleteRecipes();

                // Fetch the network data and update the local database
                for (int i = 0; i < mRecipeList.size(); i++) {
                    Recipe recipe = mRecipeList.get(i);
                    mRecipeDB.insertRecipe(recipe.getmTitle(), recipe.getmRecipeDetails());
                }

                mRecyclerView.setAdapter(new MyUserRecipeRecyclerViewAdapter(mRecipeList, mListener));
            }
        }


    }

}
