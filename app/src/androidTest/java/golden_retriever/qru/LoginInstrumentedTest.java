package golden_retriever.qru;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class LoginInstrumentedTest {

    @Rule
    public IntentsTestRule<LoginActivity> loginactivity = new IntentsTestRule<LoginActivity>(LoginActivity.class);

    @Test
    public void wrongPassword() throws Exception {
        onView(withId(R.id.email))
                .perform(typeText("calladokike@gmail.com"));
        onView(withId(R.id.password))
                .perform(typeText("wrong"));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.password))
                .check(matches(hasErrorText("Password is incorrect")));

    }

    @Test
    public void emptyEmail() throws Exception {

        onView(withId(R.id.password))
                .perform(typeText("testing"));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        onView(withId(R.id.email))
                .check(matches(hasErrorText("This field is required")));
    }
    @Test
    public void validLogin() throws Exception{
        onView(withId(R.id.email))
                .perform(typeText("calladokike@gmail.com"));
        onView(withId(R.id.password))
                .perform(typeText("testing"));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), StudentMain.class)));
        //intended(hasComponent(StudentMain.class.getName()));
    }

}
