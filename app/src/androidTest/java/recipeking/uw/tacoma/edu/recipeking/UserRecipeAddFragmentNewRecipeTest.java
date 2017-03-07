package recipeking.uw.tacoma.edu.recipeking;


import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import recipeking.uw.tacoma.edu.recipeking.login.LoginActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class UserRecipeAddFragmentNewRecipeTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule = new ActivityTestRule<>(
            LoginActivity.class);


    @Test
    public void testSignUpFragmentOnEmptyUsername() {
        onView(withId(R.id.sign_up_tv))
                .perform(click());

        onView(withId(R.id.username_signup))
                .perform(typeText(""));
        onView(withId(R.id.password_signup))
                .perform(typeText("123456"));
        onView(withId(R.id.button_signup))
                .perform(click());

        onView(withText("Username field is required"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpFragmentOnEmptyPassword() {
        onView(withId(R.id.sign_up_tv))
                .perform(click());

        onView(withId(R.id.username_signup))
                .perform(typeText("SimpleUserName"));
        onView(withId(R.id.password_signup))
                .perform(typeText(""));
        onView(withId(R.id.button_signup))
                .perform(click());

        onView(withText("Password field is required"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpFragmentOnInvalidPassword() {
        onView(withId(R.id.sign_up_tv))
                .perform(click());

        onView(withId(R.id.username_signup))
                .perform(typeText("SimpleUserName"));
        onView(withId(R.id.password_signup))
                .perform(typeText("123"));
        onView(withId(R.id.button_signup))
                .perform(click());

        onView(withText("This password is too short. Enter at least 6 characters"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpFragmentOnInvalidUsernameWithSpaces() {
        onView(withId(R.id.sign_up_tv))
                .perform(click());

        onView(withId(R.id.username_signup))
                .perform(typeText("Simpl Username"));
        onView(withId(R.id.password_signup))
                .perform(typeText("123456"));
        onView(withId(R.id.button_signup))
                .perform(click());

        onView(withText("Username cannot have space characters and must be less than 20 characters."))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testSignUpFragmentOnValidInfo() {
        onView(withId(R.id.sign_up_tv))
                .perform(click());

        Random random = new Random();
        String username = "Usr" + (random.nextInt(100));

        onView(withId(R.id.username_signup))
                .perform(typeText(username));
        onView(withId(R.id.password_signup))
                .perform(typeText("123456"));
        onView(withId(R.id.button_signup))
                .perform(click());

        onView(withText("You have been registered successfully"))
                .inRoot(withDecorView(not(is(
                        mActivityRule.getActivity()
                                .getWindow()
                                .getDecorView()))))
                .check(matches(isDisplayed()));
    }

}
