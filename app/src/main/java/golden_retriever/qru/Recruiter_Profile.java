package golden_retriever.qru;

/**
 * Created by Matt on 3/26/18.
 */

public class Recruiter_Profile {
    String _id;
    String salt;
    String password;
    String fName;
    String lName;
    String email;
    String companyName;
    String companyAbout;
    String companyPositions;

    Recruiter_Profile(String id, String salt, String password, String fName, String lName, String email, String companyName, String companyAbout,
                      String comapnyPositions){
        this._id = id;
        this.salt = salt;
        this.password = password;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.companyName = companyName;
        this.companyAbout = companyAbout;
        this.companyPositions = companyPositions;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAbout() {
        return companyAbout;
    }

    public void setCompanyAbout(String companyAbout) {
        this.companyAbout = companyAbout;
    }

    public String getCompanyPositions() {
        return companyPositions;
    }

    public void setCompanyPositions(String companyPositions) {
        this.companyPositions = companyPositions;
    }


}
