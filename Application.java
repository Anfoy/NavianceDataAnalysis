package me.antcode;

public class Application {


    private final String type;

    private  College college;

    private boolean isAccepted = false;

    private boolean isRejected = false;

    private boolean isWaitlisted = false;

    private boolean isEarlyAction = false;

    private boolean isEarlyDecision = false;

    private  boolean isRegularDecision = false;

    private boolean isUnknown = false;

    private final StudentProfile associatedProfile;


    public Application(String type, StudentProfile studentProfile) {
        this.type = type;
        this.college = null;
        setApplicationBooleanStats();
        this.associatedProfile = studentProfile;
    }

    private void setApplicationBooleanStats(){
        if (type.contains("EA")){
            isEarlyAction = true;
        }
        if (type.contains("RD")){
            isRegularDecision = true;
        }
        if (type.contains("ED")){
            isEarlyDecision = true;
        }
        if (type.contains("accepted") || type.contains("Accepted")){
            isAccepted = true;
        }
        if (type.contains("denied") || type.contains("Denied")){
            isRejected = true;
        }
        if (type.contains("waitlisted")){
            isWaitlisted = true;
        }
        if (type.contains("Unknown")){
            isUnknown = true;
        }
    }

    public void setCollege(College college){
        this.college = college;
    }


    public String getType() {
        return type;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public boolean isWaitlisted() {
        return isWaitlisted;
    }

    public boolean isEarlyAction() {
        return isEarlyAction;
    }

    public boolean isEarlyDecision() {
        return isEarlyDecision;
    }

    public boolean isRegularDecision() {
        return isRegularDecision;
    }

    public boolean isUnknown() {
        return isUnknown;
    }

    public College getCollege() {
        return college;
    }

    public StudentProfile getAssociatedProfile() {
        return associatedProfile;
    }

    @Override
    public String toString() {
        return "Application { " + "\n"
                + "COLLEGE_UUID: " + college.getUuid() + "\n"
                + "TYPE: " + type + "\n"
                + "ACCEPTED: " + isAccepted + "\n"
                + "REJECTED: " + isRejected + "\n"
                + "WAITLISTED: " + isWaitlisted + "\n"
                + "EARLY_ACTION: " + isEarlyAction + "\n"
                + "EARLY_DECISION: " + isEarlyDecision + "\n"
                + "REGULAR_DECISION: " + isRegularDecision + "\n"
                + "UNKNOWN: " + isUnknown + "\n"
                + "}";
    }

}
