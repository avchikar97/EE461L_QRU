package golden_retriever.qru;

import java.util.ArrayList;

/**
 * Created by mattjohnson on 4/29/18.
 */

public class Company_Profile {
    private String name;
    private ArrayList<Recruiter_Profile> recruiters;

    public Company_Profile(String name) {
        this.name = name;
    }

    public void addRecruiter(Recruiter_Profile recruiter_profile){
        recruiters.add(recruiter_profile);
    }

    public ArrayList<Recruiter_Profile> getRecruiters(){
        return recruiters;
    }

}
