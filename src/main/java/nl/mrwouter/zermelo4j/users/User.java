package nl.mrwouter.zermelo4j.users;

/**
 * User object
 */
public class User {

    private final String user;
    private final String firstName;
    private final String lastName;
    private final String prefix;
    private final boolean isArchived;
    private final boolean hasPassword;
    private final boolean isApplicationManager;
    private final boolean isStudent;
    private final boolean isEmployee;
    private final boolean isFamilyMember;
    private final boolean isSchoolScheduler;
    private final boolean isSchoolLeader;
    private final boolean isStudentAdministrator;
    private final boolean isTeamLeader;
    private final boolean isSectionLeader;
    private final boolean isMentor;
    private final boolean isDean;

    /**
     * Create a user object
     * @param user (username or ~me)
     * @param firstName first name
     * @param lastName last name
     * @param prefix prefix
     * @param isArchived true if user is archived
     * @param hasPassword true if user has a password
     * @param isApplicationManager true if user is an application manager
     * @param isStudent true if user is a student
     * @param isEmployee true if user is an employee
     * @param isFamilyMember true if user is a family member
     * @param isSchoolScheduler true if user is a school scheduler
     * @param isSchoolLeader true if user is a school leader
     * @param isStudentAdministrator true if user is a student administrator
     * @param isTeamLeader true if user is a team leader
     * @param isSectionLeader true if user is a section leader
     * @param isMentor true if user is a mentor
     * @param isDean true if user is a dean
     */
    public User(String user, String firstName, String lastName, String prefix, boolean isArchived, boolean hasPassword,
                boolean isApplicationManager, boolean isStudent, boolean isEmployee, boolean isFamilyMember,
                boolean isSchoolScheduler, boolean isSchoolLeader, boolean isStudentAdministrator, boolean isTeamLeader,
                boolean isSectionLeader, boolean isMentor, boolean isDean) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.prefix = prefix;

        this.isArchived = isArchived;
        this.hasPassword = hasPassword;
        this.isApplicationManager = isApplicationManager;
        this.isStudent = isStudent;
        this.isEmployee = isEmployee;
        this.isFamilyMember = isFamilyMember;
        this.isSchoolScheduler = isSchoolScheduler;
        this.isSchoolLeader = isSchoolLeader;
        this.isStudentAdministrator = isStudentAdministrator;
        this.isTeamLeader = isTeamLeader;
        this.isSectionLeader = isSectionLeader;
        this.isMentor = isMentor;
        this.isDean = isDean;
    }

    /**
     * Get the user identifier
     *
     * @return identifier of user
     */
    public String getUser() {
        return user;
    }

    /**
     * Get the first name of the user
     *
     * @return first name of user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Get the last name of the user
     *
     * @return last name of user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Get the prefix of the user
     *
     * @return prefix of user
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Get whether the user is archived
     *
     * @return whether the user is archived
     */
    public boolean isArchived() {
        return isArchived;
    }

    /**
     * Get whether the user has a password to login
     *
     * @return whether the user has a password
     */
    public boolean hasPassword() {
        return hasPassword;
    }

    /**
     * Get whether the user is an application manager
     *
     * @return whether the user is an application manager
     */
    public boolean isApplicationManager() {
        return isApplicationManager;
    }

    /**
     * Get whether the user is a student
     *
     * @return whether the user is a student
     */
    public boolean isStudent() {
        return isStudent;
    }

    /**
     * Get whether the user is an employee
     *
     * @return whether the user is an employee
     */
    public boolean isEmployee() {
        return isEmployee;
    }

    /**
     * Get whether the user is a family member
     *
     * @return whether the user is a family member
     */
    public boolean isFamilyMember() {
        return isFamilyMember;
    }

    /**
     * Get whether the user is a school scheduler
     *
     * @return whether the user is a school scheduler
     */
    public boolean isSchoolScheduler() {
        return isSchoolScheduler;
    }

    /**
     * Get whether the user is a school leader
     *
     * @return whether the user is a school leader
     */
    public boolean isSchoolLeader() {
        return isSchoolLeader;
    }

    /**
     * Get whether the user is a student administrator
     *
     * @return whether the user is a student administrator
     */
    public boolean isStudentAdministrator() {
        return isStudentAdministrator;
    }

    /**
     * Get whether the user is a team leader
     *
     * @return whether the user is a team leader
     */
    public boolean isTeamLeader() {
        return isTeamLeader;
    }

    /**
     * Get whether the user is a section leader
     *
     * @return whether the user is a section leader
     */
    public boolean isSectionLeader() {
        return isSectionLeader;
    }

    /**
     * Get whether the user is a mentor
     *
     * @return whether the user is a mentor
     */
    public boolean isMentor() {
        return isMentor;
    }

    /**
     * Get whether the user is a dean
     *
     * @return whether the user is a dean
     */
    public boolean isDean() {
        return isDean;
    }
}
