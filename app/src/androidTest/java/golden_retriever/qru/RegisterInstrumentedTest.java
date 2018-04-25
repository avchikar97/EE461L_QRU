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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
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
public class RegisterInstrumentedTest {

    @Rule
    public ActivityTestRule<RegisterActivity> registeractivity = new ActivityTestRule<RegisterActivity>(RegisterActivity.class);

    @Test
    public void emailTaken() throws Exception {
        onView(withId(R.id.email))
                .perform(typeText("calladokike@gmail.com"));
        onView(withId(R.id.first_name))
                .perform(typeText("Jon"));
        onView(withId(R.id.last_name))
                .perform(typeText("Deer"));
        onView(withId(R.id.password1))
                .perform(typeText("testing"));
        onView(withId(R.id.password2))
                .perform(typeText("string"))
                .perform(closeSoftKeyboard());
        onView(withId(R.id.register_button))
                .perform(click());
        onView(withId(R.id.email))
                .check(matches(hasErrorText("This email is already registered")));

    }
}
