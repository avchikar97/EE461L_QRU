package golden_retriever.qru;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Akaash Chikarmane on 4/20/2018.
 */

public class myJSONWrapper implements Serializable {
    public JSONObject getJSONObject() {
        return mJSONObject;
    }

    myJSONWrapper(JSONObject mJSON){
        super();
        this.mJSONObject = mJSON;
    }

    public void setJSONObject(JSONObject JSONObject) {
        mJSONObject = JSONObject;
    }

    JSONObject mJSONObject;

}
