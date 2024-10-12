package me.antcode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DataManager {

    private String line;
    private final String csvSplitBy = ",";
    private final List<College> colleges;
    private final List<StudentProfile> studentProfiles;

    public DataManager(){
        colleges = new ArrayList<>();
        studentProfiles = new ArrayList<>();
        loadColleges();
        loadCollegeScattergramData();
        loadOverallCollegeScattergramData();
    }

    private void loadColleges(){
        String collegeInfoCSV = "src/main/java/me/antcode/CollegeData/college_info.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(collegeInfoCSV))) {
            // Skip header if present
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] collegeData = line.split(csvSplitBy);

                if (collegeData.length >= 2) {
                    String uuid = collegeData[0];
                    String name = collegeData[1];

                    String schoolId = collegeData[2];

                    College college = new College(uuid,name, schoolId);
                    colleges.add(college);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Print all colleges to verify
    }

    private void loadCollegeScattergramData(){
        String applicationStatsCSV = "src/main/java/me/antcode/CollegeData/appdata.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(applicationStatsCSV))) {
            // Skip header if present
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] collegeData = line.split(csvSplitBy);

                if (collegeData.length >= 6) {
                    String uuid = collegeData[0];
                    String type = collegeData[1];
                    double gpa = Double.parseDouble(collegeData[2]);
                    int actComp = getInt(collegeData[3]);
                    int convertedSAT = getInt(collegeData[4]);
                    int comboSAT = getInt(collegeData[5]);
                    int compositeSAT = getInt(collegeData[6]);
                    int highestSAT = Math.max(convertedSAT, comboSAT);
                    StudentProfile profile = doesProfileExist(actComp, compositeSAT, highestSAT, gpa);
                    Application application = new Application(type, profile);
                    for (College college : colleges) {
                        if (college.getUuid().equals(uuid)) {
              if (!checkIfDuplicate(profile, college)) {
                college.getAppliedStudents().add(profile);
                application.setCollege(college);
                profile.getApplications().add(application);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getInt(String line){
        try{
            return Integer.parseInt(line);
        } catch (NumberFormatException e){
            return 0;
        }
    }

    private void loadOverallCollegeScattergramData(){
        String overallApplicationStatsCSV = "src/main/java/me/antcode/CollegeData/general_app_stats.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(overallApplicationStatsCSV))) {
            // Skip header if present
            line = br.readLine();

            while ((line = br.readLine()) != null) {
                String[] collegeData = line.split(csvSplitBy);

                if (collegeData.length >= 7) {
                    String uuid = collegeData[0];
                    double act25th = Double.parseDouble(collegeData[1]);
                    double act75th = Double.parseDouble(collegeData[2]);
                    double sat25th = Double.parseDouble(collegeData[3]);
                    double sat75th = Double.parseDouble(collegeData[4]);
                    double gpa25th = Double.parseDouble(collegeData[5]);
                    double gpa75th = Double.parseDouble(collegeData[6]);
                    for (College college : colleges) {
                        if (college.getUuid().equals(uuid)) {
                            college.setPercentiles(act25th, act75th, sat25th, sat75th, gpa25th, gpa75th);
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean checkIfDuplicate(StudentProfile profile, College college){
        for (StudentProfile loopProfile : college.getAppliedStudents()) {
            if (loopProfile.getiD() == profile.getiD()) {
                return true;
            }
        }
        return false;
    }

    private StudentProfile doesProfileExist(int actComp, int satComp, int highestSAT, double gpa){
        StudentProfile profile;
        if (studentProfiles.isEmpty()) {
             profile = new StudentProfile(generateID(), gpa, highestSAT, actComp, satComp);
            studentProfiles.add(profile);
            return profile;
        }
        for (StudentProfile p : studentProfiles) {
            if (p.getActComposite() != actComp ) continue;
            if (p.getSatComposite() != satComp) continue;
            if (p.getwGpa() != gpa) continue;
            if (p.getHighestSAT() != highestSAT) continue;
            return p;
        }
        profile = new StudentProfile(generateID(), gpa, highestSAT, actComp, satComp);
        studentProfiles.add(profile);
        return profile;
    }

    public void testHarvard(){
        for (College college : colleges) {
            if (college.getSchoolId().equals("3434")) {
        System.out.println(college.getName());
        System.out.println(college.getAppliedStudents().size());
        }
            }
        }

    /**
     * Print application statistics for The desired School
     * @param collegeName College name to look up
     */
    public void getCollegeStats(String collegeName){
        for (College college : colleges) {
            if (college.getName().toUpperCase().contains(collegeName.toUpperCase())) {
        System.out.println(college);
                System.out.println("APPLICATIONS: " + college.getAppliedStudents().size());
                System.out.println("APPLICATION STATISTICS: " + "\n" + college.formatApplicationStats());
            }
        }
        }

        private College findMatchingCollege(String uuid){
        for (College college : colleges){
            if (college.getUuid().equals(uuid)) return college;
        }
        return null;
        }

    /**
     * Finds the matching student profile with the same sat and gpa.
     * @param sat sat to look for
     * @param gpa gpa to look for
     */
    public void findMatchingProfile(int sat, double gpa){
        for (StudentProfile studentProfile : studentProfiles){
            if (studentProfile.getwGpa() != gpa) return;
            if (studentProfile.getHighestSAT() != sat) continue;
            for (Application application : studentProfile.getApplications()){
                System.out.println(application);
            }
            break;
        }
        }

        private int generateID(){
        if (studentProfiles.isEmpty()) return 1000;
        return studentProfiles.getLast().getiD() + 1;
        }

    /**
     * Returns the proportion of students who had over the parameterized value.
     */
    public double proportionOfGreaterOrEqualSAT(int sat){
        int overAndEqualSat = 0;
        for (StudentProfile profile : studentProfiles){
            if (profile.getHighestSAT() >= sat) overAndEqualSat++;
        }
        return 100 - ((double) overAndEqualSat /studentProfiles.size()) * 100;
    }

    /**
     * Returns percent of students with a greater or equal gpa.
     * @param gpa Gpa to check
     * @return statistics of greater or equal gpas.
     */
    public double proportionOfGreaterOrEqualGPA(double gpa){
        int overAndEqualGpa = 0;
        for (StudentProfile profile : studentProfiles){
            if (profile.getwGpa() >= gpa) overAndEqualGpa++;
        }
        return  100-  ((double) overAndEqualGpa /studentProfiles.size()) * 100;
    }

    /**
     * Acceptance rate for all applicationst that have an equal or higher sat
     * @param sat sat to check
     * @return Statistics of acceptance.
     */
    public double acceptanceRateForGreaterOrEqualSAT(int sat){
        int acceptances = 0;
        int totalApplications = 0;
        for (StudentProfile profile : studentProfiles){
            for (Application application : profile.getApplications()){
                totalApplications++;
                if (profile.getHighestSAT() >= sat){
                        if (application.isAccepted()) acceptances++;
                }
            }
        }
        return 100- ((double) acceptances /totalApplications) * 100;
    }

    /**
     * Prints statistics for acceptances and total applications with same SAT score
     * @param sat sat to check
     */
    public void printApplicationStatisticsForSAT(int sat){
        int acceptances = 0;
        int totalApplications = 0;
        for (StudentProfile profile : studentProfiles){
            for (Application application : profile.getApplications()){
                if (profile.getHighestSAT() == sat){
                    if (application.isAccepted()) acceptances++;
                    totalApplications++;
                }
            }
        }
        String outputString = "SAT: " + sat + "\n"
                + "Acceptances: " + acceptances + "\n"
                + "Total Applications with matching SAT: " + totalApplications;
    System.out.println(outputString);
    }

    /**
     * Prints statistics for gpas within .3 of gpa inputted, both up and down.
     * @param gpa Gpa to check
     */
    public void printApplicationStatisticsForGPA(double gpa){
        int acceptances = 0;
        int totalApplications = 0;
        for (StudentProfile profile : studentProfiles){
            for (Application application : profile.getApplications()){
                if (isInGPARange(gpa, profile)){
                    if (application.isAccepted()) acceptances++;
                    totalApplications++;
                }
            }
        }
        String outputString = "GPA: " + gpa + "\n"
                + "Acceptances: " + acceptances + "\n"
                + "Total Applications with matching GPA: " + totalApplications;
        System.out.println(outputString);
    }

    public void printSimilarApplicants(int sat, double gpa){
        int similar = 0;
        for (StudentProfile profile : studentProfiles){
            if (isInGPARange(gpa, profile) && profile.getHighestSAT() == sat) similar++;
        }
    System.out.println("There are " + similar + " applicants with a SAT of: " + sat + " and a gpa within .3 (+-) of: " + gpa);
    }

    private boolean isInGPARange(double gpa, StudentProfile profile){
        return profile.getwGpa() == gpa || (profile.getwGpa() >= gpa -.3 && profile.getwGpa() <= gpa + .3);
    }

    /**
     * Find the percentile you fit in based on all previous students statistics
     * @param inputGpa your gpa
     * @param inputSat your sat
     */
    public void getPercentileOfPerson(double inputGpa, int inputSat) {
        ArrayList<Double> allCombinedScores = new ArrayList<>();

        // Normalize and combine the GPA and SAT scores of all profiles
        for (StudentProfile profile : studentProfiles) {
            double combinedScore = normalizeAndCombine(profile.getwGpa(), profile.getHighestSAT());
            allCombinedScores.add(combinedScore);
        }

        // Sort the combined scores
        Collections.sort(allCombinedScores);

        // Normalize and combine the input GPA and SAT
        double inputCombinedScore = normalizeAndCombine(inputGpa, inputSat);

        // Calculate the percentile for the input GPA and SAT
        int belowOrEqualCount = 0;
        for (double score : allCombinedScores) {
            if (score <= inputCombinedScore) {
                belowOrEqualCount++;
            }
        }

        double percentile = (double) belowOrEqualCount / allCombinedScores.size() * 100.0;
        System.out.println("Overall Percentile: " + percentile + "%");
    }

    // Helper method to normalize and combine GPA and SAT
    private double normalizeAndCombine(double gpa, int sat) {
        // Normalize GPA (assuming 5.3 scale)
        double normalizedGpa = (gpa / 5.2) * 100;

        // Normalize SAT (assuming 1600 max score)
        double normalizedSat = (sat / 1600.0) * 100;

        // Combine the normalized GPA and SAT (equal weights here) (weights mean significance in placement, so if gpa is more valued add more weight)
        return (normalizedGpa * 0.5) + (normalizedSat * 0.5);  // Adjust weights if needed
    }

    public void find50thPercentile() {
        ArrayList<Double> allGpas = new ArrayList<>();
        ArrayList<Integer> allSats = new ArrayList<>();

        // Collect GPA and SAT values from all profiles
        for (StudentProfile profile : studentProfiles) {
            allGpas.add(profile.getwGpa());
            allSats.add(profile.getHighestSAT());
        }

        // Sort GPA and SAT values
        Collections.sort(allGpas);
        Collections.sort(allSats);

        // Calculate the 50th percentile (median) for GPA and SAT
        double medianGpa = findMedian(allGpas);
        double medianSat = findMedian(allSats);

        // Output the results
        System.out.println("50th Percentile GPA: " + medianGpa);
        System.out.println("50th Percentile SAT: " + medianSat);
    }

    // Helper method to find the median of a list of numbers
    private double findMedian(ArrayList<? extends Number> values) {
        int size = values.size();
        if (size == 0) return 0;

        // If size is odd, return the middle value
        if (size % 2 == 1) {
            return values.get(size / 2).doubleValue();
        }

        // If size is even, return the average of the two middle values
        double middle1 = values.get((size / 2) - 1).doubleValue();
        double middle2 = values.get(size / 2).doubleValue();
        return (middle1 + middle2) / 2.0;
    }

    public void printAcceptedCollegesForSAT(int sat){
        List<String> collegeNames = new ArrayList<>();
        for (StudentProfile profile : studentProfiles){
            if (profile.getHighestSAT() == sat){
               for (Application application : profile.getApplications()){
                   if (!application.isAccepted()) continue;
                   if (collegeNames.contains(application.getCollege().getName())) continue;
                   collegeNames.add(application.getCollege().getName());
               }
            }
        }
        if (collegeNames.isEmpty()) {
            System.out.println("No accepted colleges :(");
            return;
        }
    System.out.println("Accepted Colleges: {");
        for (String collegeName : collegeNames){
            System.out.println(collegeName);
        }
    System.out.println("}");
    }

    public void printAcceptedCollegesForGpa(double gpa){
        List<String> collegeNames = new ArrayList<>();
        for (StudentProfile profile : studentProfiles){
            if (!isInGPARange(gpa, profile)) continue;{
                for (Application application : profile.getApplications()){
                    if (!application.isAccepted()) continue;
                    if (collegeNames.contains(application.getCollege().getName())) continue;
                    collegeNames.add(application.getCollege().getName());
                }
            }
        }
        if (collegeNames.isEmpty()) {
            System.out.println("No accepted colleges :(");
            return;
        }
        System.out.println("Accepted Colleges: {");
        for (String collegeName : collegeNames){
            System.out.println(collegeName);
        }
        System.out.println("}");
    }




    }



