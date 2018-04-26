package golden_retriever.qru;

import android.content.ComponentName;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.intent.rule.IntentsTestRule;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ExecutionException;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.hasErrorText;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ProfileUpdateTest implements AsyncResponse{
    private static final String email = "calladokike@gmail.com";

   /* @Rule
    public ActivityTestRule<UpdateStudentProfile> tActivityRule = new ActivityTestRule<>(
            UpdateStudentProfile.class, true, );*/
    @Rule
    public IntentsTestRule<LoginActivity> mActivityRule = new IntentsTestRule<>(
            LoginActivity.class, true, true);

    @Test
    public void updateProfile() throws Exception {
        //Create Rest Client and sets operation to GET
        RestAsync rest  = new RestAsync(this);
        rest.setType("GET");
        JSONObject query = new JSONObject();
        JSONObject result = null;

        onView(withId(R.id.email))
                .perform(typeText(email));
        onView(withId(R.id.password))
                .perform(typeText("testing"));
        onView(withId(R.id.email_sign_in_button))
                .perform(click());
        intended(hasComponent(new ComponentName(getTargetContext(), StudentMain.class)));
        onView(withId(R.id.submit_student_profile_btn))
                .perform(click());
   //     tActivityRule.launchActivity(null);
        //New name
        onView(withId(R.id.student_update_profile_firstname))
                .perform(typeText("Test"));
        //New Last name
        onView(withId(R.id.student_update_profile_lastname))
                .perform(typeText("Name"));
        //New GPA
        onView(withId(R.id.student_update_profile_GPA))
                .perform(typeText("5.0"))
                .perform(closeSoftKeyboard());
        //Aerospace major
        onView(withId(R.id.student_update_profile_major))
                .perform(click());
                onData(allOf(is(instanceOf(String.class)))).atPosition(0).perform(click());
        onView(withId(R.id.student_update_profile_major))
                .check(matches(withSpinnerText(containsString("Aerospace Engineering"))));
        //Senior classification
        onView(withId(R.id.student_update_profile_classification))
                .perform(click());
                onData(allOf(is(instanceOf(String.class)))).atPosition(3).perform(click());
        onView(withId(R.id.student_update_profile_classification))
                .check(matches(withSpinnerText(containsString("Senior"))));
        //note
        onView(withId(R.id.student_update_profile_special_notes))
                .perform(typeText("Please Hire Me"));
        onView(withId(R.id.submit_student_profile_btn))
                .perform(click());

        onView(withId(R.id.student_scan_btn))
                .check(matches(withText("Scan New Company")));
        //check that the Database got updated with the new profile: New name, last name, gpa
        //assertEquals(Candidate_Profile.class.getName(), "Test");

        //Create JSON query and sends it to RestClient and returns the query result to "result"
        try {
            query.put("email", email);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        rest.execute(query);

        try{
            result = rest.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e ) {
            e.printStackTrace();
        }

        //Checks if update was successful
        if(result.has("firstName")) {
            assertEquals(result.getString("firstName"), "Test");
        } else {
            fail("Update was not successful");
        }
    }

    @Override
    public void processFinish(JSONObject output){

    }
}
