package golden_retriever.qru;

import org.junit.Test;

import java.util.regex.Matcher;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void login_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        assertThat(LoginActivity.isEmailValid("kevin@gmail.com"), is(true));
        assertThat(LoginActivity.isEmailValid("bob@utexas.edu"), is(true));
        assertThat(LoginActivity.isEmailValid("enrique@govboi.org"), is(true));
        assertThat(LoginActivity.isEmailValid("4head"), is(false));
        assertThat(LoginActivity.isEmailValid("@@@"), is(false));
        assertThat(LoginActivity.isEmailValid("a@b"), is(false));
        assertThat(LoginActivity.isEmailValid("a@b.com"), is(true));
    }
}