package golden_retriever.qru;

/**
 * Created by daniel on 4/2/18.
 */

public class ID implements java.io.Serializable{
    private static final long serialVersionUID = 1L;

    String value;

    public ID(String value){
        super();
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString(){
        return this.value;
    }

}
