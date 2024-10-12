package me.antcode;


import java.util.ArrayList;

public class College {
    private final String uuid;
    private final String name;
    private final String schoolId;

    private double act25thPercentile;

    private double act75thPercentile;

    private double sat25thPercentile;

    private double sat75thPercentile;

    private double weightedGPA25thPercentile;
    private double weightedGPA75thPercentile;


    private final ArrayList<StudentProfile> appliedStudents;

    public College(String uuid, String name, String schoolId) {
        this.uuid = uuid;
        this.name = name;
        this.schoolId = schoolId;
        appliedStudents = new ArrayList<>();
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public double getAct25thPercentile() {
        return act25thPercentile;
    }

    public double getAct75thPercentile() {
        return act75thPercentile;
    }

    public double getSat25thPercentile() {
        return sat25thPercentile;
    }

    public double getSat75thPercentile() {
        return sat75thPercentile;
    }

    public double getWeightedGPA25thPercentile() {
        return weightedGPA25thPercentile;
    }

    public double getWeightedGPA75thPercentile() {
        return weightedGPA75thPercentile;
    }

    public ArrayList<StudentProfile> getAppliedStudents() {
        return appliedStudents;
    }

    public void setPercentiles(
      double act25thPercentile,
      double act75thPercentile,
      double sat25thPercentile,
      double sat75thPercentile,
      double weightedGPA25thPercentile,
      double weightedGPA75thPercentile) {
    this.act25thPercentile = act25thPercentile;
    this.act75thPercentile = act75thPercentile;
    this.sat25thPercentile = sat25thPercentile;
    this.sat75thPercentile = sat75thPercentile;
    this.weightedGPA25thPercentile = weightedGPA25thPercentile;
    this.weightedGPA75thPercentile = weightedGPA75thPercentile;
        }

    @Override
    public String toString() {
        return "College { " + "\n"
                + "NAME: " + name + "\n"
                + "SCHOOL_ID: " + schoolId + "\n"
                + "UUID: " + uuid + "\n"
                + " }";
    }

    public String formatApplicationStats() {
        StringBuilder formattedApps = new StringBuilder();
        int acceptedRD = 0;
        int acceptedED = 0;
        int acceptedEA = 0;
        int deniedRD = 0;
        int deniedED = 0;
        int deniedEA = 0;
        int waitListedAcceptedED = 0;
        int waitListedAcceptedRD = 0;
        int waitListedAcceptedEA = 0;
        int waitListedDeniedED = 0;
        int waitListedDeniedEA = 0;
        int waitListedDeniedRD = 0;
        int waitListedUnknownED = 0;
        int waitListedUnknownRD = 0;
        int waitListedUnknownEA = 0;

    for (StudentProfile profile : appliedStudents) {
      for (Application application : profile.getApplications()) {
        if (application.getCollege() != null && application.getCollege().getUuid().equals(uuid)) {
          if (application.isWaitlisted()) {
            if (application.isAccepted()) {
              if (application.isEarlyDecision()) {
                waitListedAcceptedED++;
              } else if (application.isEarlyAction()) {
                waitListedAcceptedEA++;
              } else if (application.isRegularDecision()) {
                waitListedAcceptedRD++;
              }
            } else if (application.isRejected()) {
              if (application.isEarlyDecision()) {
                waitListedDeniedED++;
              } else if (application.isEarlyAction()) {
                waitListedDeniedEA++;
              } else if (application.isRegularDecision()) {
                waitListedDeniedRD++;
              }
            } else if (application.isUnknown()) {
              if (application.isEarlyDecision()) {
                waitListedUnknownED++;
              } else if (application.isEarlyAction()) {
                waitListedUnknownEA++;
              } else if (application.isRegularDecision()) {
                waitListedUnknownRD++;
              }
            }
          } else if (application.isRejected()) {
            if (application.isEarlyDecision()) {
              deniedED++;
            } else if (application.isEarlyAction()) {
              deniedEA++;
            } else if (application.isRegularDecision()) {
              deniedRD++;
            }
          } else if (application.isAccepted()) {
            if (application.isEarlyDecision()) {
              acceptedED++;
            } else if (application.isEarlyAction()) {
              acceptedEA++;
            } else if (application.isRegularDecision()) {
              acceptedRD++;
            }
          }
        }
      }
            }

        // Formatting the output
        formattedApps.append("Accepted Early Decision: ").append(acceptedED).append("\n");
        formattedApps.append("Accepted Early Action: ").append(acceptedEA).append("\n");
        formattedApps.append("Accepted Regular Decision: ").append(acceptedRD).append("\n");
        formattedApps.append("Denied Early Decision: ").append(deniedED).append("\n");
        formattedApps.append("Denied Early Action: ").append(deniedEA).append("\n");
        formattedApps.append("Denied Regular Decision: ").append(deniedRD).append("\n");
        formattedApps.append("Waitlisted & Accepted Early Decision: ").append(waitListedAcceptedED).append("\n");
        formattedApps.append("Waitlisted & Accepted Early Action: ").append(waitListedAcceptedEA).append("\n");
        formattedApps.append("Waitlisted & Accepted Regular Decision: ").append(waitListedAcceptedRD).append("\n");
        formattedApps.append("Waitlisted & Denied Early Decision: ").append(waitListedDeniedED).append("\n");
        formattedApps.append("Waitlisted & Denied Early Action: ").append(waitListedDeniedEA).append("\n");
        formattedApps.append("Waitlisted & Denied Regular Decision: ").append(waitListedDeniedRD).append("\n");
        formattedApps.append("Waitlisted & Unknown Early Decision: ").append(waitListedUnknownED).append("\n");
        formattedApps.append("Waitlisted & Unknown Early Action: ").append(waitListedUnknownEA).append("\n");
        formattedApps.append("Waitlisted & Unknown Regular Decision: ").append(waitListedUnknownRD).append("\n");

        return formattedApps.toString();
    }



}
