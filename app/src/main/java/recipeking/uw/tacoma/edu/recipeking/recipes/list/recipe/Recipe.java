package recipeking.uw.tacoma.edu.recipeking.recipes.list.recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

/**
 *
 *
 */
public class Recipe implements Serializable {

    private String mTitle;
    private String mImageUrl;
    private String[] mIngredients;
    private String mDirectionsUrl;

    public static final String HITS = "hits";
    public static final String RECIPE = "recipe";
    public static final String LABEL = "label";
    public static final String IMAGE = "image";
    public static final String INGREDIENT_LINES = "ingredientLines";
    public static final String URL = "url";

    public Recipe(String mTitle, String mImageUrl, String[] mIngredients, String mDirectionsUrl) {
        this.mTitle = mTitle;
        this.mImageUrl = mImageUrl;
        this.mIngredients = mIngredients;
        this.mDirectionsUrl = mDirectionsUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public String[] getmIngredients() {
        return mIngredients;
    }

    public String getmDirectionsUrl() {
        return mDirectionsUrl;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmDirectionsUrl(String mDirectionsUrl) {
        this.mDirectionsUrl = mDirectionsUrl;
    }

    public void setmIngredients(String[] mIngredients) {
        this.mIngredients = mIngredients;
    }

    public static String parseRecipesJSON(String recipesJSON, List<Recipe> recipeList) {
        String reason = null;
        if (recipesJSON != null) {
            try {
                JSONObject obj = new JSONObject(recipesJSON);
                JSONArray arr = obj.getJSONArray(Recipe.HITS);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject rObj = arr.getJSONObject(i);
                    JSONObject recipeJson = rObj.getJSONObject(Recipe.RECIPE);

                    String label = recipeJson.getString(Recipe.LABEL);
                    //System.out.println(label);

                    String image = recipeJson.getString(Recipe.IMAGE);
                    //System.out.println(image);

                    JSONArray JSONingredients = recipeJson.getJSONArray(Recipe.INGREDIENT_LINES);
                    String[] ingred = new String[JSONingredients.length()];
                    for (int j = 0; j < ingred.length; j++) {
                        ingred[j] = JSONingredients.getString(j);
                    }

                    //                for(String s : ingred) {
                    //                    System.out.println(s);
                    //                }

                    String url = recipeJson.getString(Recipe.URL);
                    //System.out.println(url);

                    Recipe recipe = new Recipe(label, image, ingred, url);
                    recipeList.add(recipe);
                }

                //System.out.println(arr.length());

            } catch (JSONException e) {
                reason = "Unable to parse data, Reason: " + e.getMessage();
            }
        }
        return reason;
    }

}
