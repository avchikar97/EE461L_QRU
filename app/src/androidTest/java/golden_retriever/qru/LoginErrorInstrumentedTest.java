package golden_retriever.qru;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginErrorInstrumentedTest {

    @Rule
    public ActivityTestRule<LoginActivity> loginactivity = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void useLogin() throws Exception {
        onView(withId(R.id.email))
                .perform(typeText("kevinsux@bignuts.com"));
        onView(withId(R.id.password))
                .perform(typeText("sux"));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.password))
                .check(matches(hasErrorText("This password is too short")));
    }
}
