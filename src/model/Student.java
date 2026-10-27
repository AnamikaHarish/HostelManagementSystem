package model;

public class Student {
    private String studentId;
    private String name;
    private String department;
    private int semester;
    private String phone;

    // Default Constructor
    public Student() {
    }

    // Parameterized Constructor
    public Student(String studentId, String name, String department, int semester, String phone) {
        this.studentId = studentId;
        this.name = name;
        this.department = department;
        this.semester = semester;
        this.phone = phone;
    }

    // Getters and Setters
    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName() {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", semester=" + semester +
                ", phone='" + phone + '\'' +
                '}';
    }
}