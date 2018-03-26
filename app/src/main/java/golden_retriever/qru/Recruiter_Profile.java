package golden_retriever.qru;

/**
 * Created by Matt on 3/26/18.
 */

public class Recruiter_Profile {
    String _id;
    String fName;
    String lName;
    String email;
    String password;
    String salt;
    String companyName;

    public Recruiter_Profile(String id, String fname, String lname, String email, String password, String salt, String companyName){
        this._id = id;
        this.fName = fname;
        this.lName = lname;
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.companyName = companyName;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }


}
