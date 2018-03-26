package golden_retriever.qru;

/**
 * Created by daniel on 3/6/18.
 */

public class Candidate_Profile {
    String _id;
    String fName;
    String lName;
    String email;
    String password;
    String salt;
    String major;
    float GPA;

    public Candidate_Profile(String id, String fname, String lname, String email, String password, String salt, String major, float GPA){
        this._id = id;
        this.fName = fname;
        this.lName = lname;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.major = major;
        this.GPA = GPA;

    }

    public String getID() {
        return _id;
    }

    public void setID(String _id) {
        this._id = _id;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public float getGPA() {
        return GPA;
    }

    public void setGPA(float GPA) {
        this.GPA = GPA;
    }
}
