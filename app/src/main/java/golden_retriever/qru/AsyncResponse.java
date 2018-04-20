package golden_retriever.qru;

import org.json.JSONObject;

/**
 * Created by daniel on 4/20/18.
 */

public interface AsyncResponse {
    void processFinish(JSONObject output);
}
