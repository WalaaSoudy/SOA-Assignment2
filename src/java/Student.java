/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

public class Student {
    private String studentID;
    private String firstName;
    private String lastName;
    private String gender;
    private String gpa;
    private String level;
    private String address;

    public Student(String studentID, String firstName, String lastName, String gender, String gpa, String level, String address) {
        this.studentID = studentID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.gpa = gpa;
        this.level = level;
        this.address = address;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getField(String field) {
        switch (field) {
            case "FirstName":
                return firstName;
            case "LastName":
                return lastName;
            case "Gender":
                return gender;
            case "GPA":
                return gpa;
            case "Level":
                return level;
            case "Address":
                return address;
            default:
                return "";
        }
    }
      // Represent the student as a comma-separated string
    public String toFileString() {
        return String.join(",", studentID, firstName, lastName, gender, gpa, level, address);
    }

    @Override
    public String toString()
    {
        // You can customize this method as needed for display purposes
        return "Student{" +
                "studentID='" + studentID + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", gender='" + gender + '\'' +
                ", gpa='" + gpa + '\'' +
                ", level='" + level + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
