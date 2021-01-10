package lab3.model;

public abstract class Person {
    private String firstName;
    private String lastName;

    //constructors

    /**
     * Default constructor
     */
    public Person() {
        this.firstName = "";
        this.lastName = "";
    }

    /**
     * Constructor with parameters
     *
     * @param firstName first name
     * @param lastName  last name
     */
    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    //getters

    /**
     * Returns first name of the person
     *
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns last name of the person
     *
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    //setters

    /**
     * Sets first name of the person
     *
     * @param firstName first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets last name of the person
     *
     * @param lastName last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
