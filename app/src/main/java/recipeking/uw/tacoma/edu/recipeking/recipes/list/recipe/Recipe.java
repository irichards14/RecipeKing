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
    private String mInstructionsUrl;

    public static final String HITS = "hits";
    public static final String RECIPE = "recipe";
    public static final String LABEL = "label";
    public static final String IMAGE = "image";
    public static final String INGREDIENT_LINES = "ingredientLines";
    public static final String URL = "url";

    public static final String TITLE = "title";
    public static final String USER = "user";
    public static final String IMAGE_URL = "image_url";
    public static final String INGREDIENTS = "ingredients";
    public static final String INSTRUCTIONS_URL = "instructions_url";

    public Recipe(String mTitle, String mImageUrl, String[] mIngredients, String mInstructionsUrl) {
        this.mTitle = mTitle;
        this.mImageUrl = mImageUrl;
        this.mIngredients = mIngredients;
        this.mInstructionsUrl = mInstructionsUrl;
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

    public String getmInstructionsUrl() {
        return mInstructionsUrl;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public void setmInstructionsUrl(String mInstructionsUrl) {
        this.mInstructionsUrl = mInstructionsUrl;
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

    public static String parseFavRecipesJSON(String recipesJSON, List<Recipe> recipeList) {
        String reason = null;
        if(recipesJSON != null) {

            try {
                JSONArray arr = new JSONArray(recipesJSON);

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    String title = obj.getString(Recipe.TITLE);
                    //String user = obj.getString(Recipe.USER);
                    String img_url = obj.getString(Recipe.IMAGE_URL);
                    String ingredients = obj.getString(Recipe.INGREDIENTS);

                    String[] ingred = ingredients.split("\\n");

                    String instructions_url = obj.getString(Recipe.INSTRUCTIONS_URL);

                    Recipe recipe = new Recipe(title, img_url, ingred, instructions_url);
                    recipeList.add(recipe);

                }
            } catch (JSONException e) {
                reason = "Unable to parse data, Reason: " + e.getMessage();
            }

        }
        return reason;
    }

    /**
     * Compares two Recipe objects for equality.
     * @param theOther represents the other Recipe object.
     * @return the result of the comparison.
     */
    @Override
    public boolean equals(Object theOther) {
        boolean result = false;
        if(theOther instanceof Recipe) {
            Recipe other = (Recipe) theOther;
            if (this.mTitle.equals(other.mTitle)) {
                result = true;
            }
        }
        return result;
    }

}
