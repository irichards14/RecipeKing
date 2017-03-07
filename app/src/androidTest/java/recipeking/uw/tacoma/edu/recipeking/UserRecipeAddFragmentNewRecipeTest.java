package recipeking.uw.tacoma.edu.recipeking;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;

import recipeking.uw.tacoma.edu.recipeking.myrecipes.MyRecipesActivity;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRecipeAddFragmentNewRecipeTest {

    @Rule
    public ActivityTestRule<MyRecipesActivity> mActivityRule = new ActivityTestRule<>(
            MyRecipesActivity.class);



}
