// class untuk entitas pegawai hotel, turunan dari person. menyimpan data login admin
package model.person;

public class Employee extends Person {
    private String position;
    private double salary;
    private String username;
    private String password;

    // konstruktor yang juga memanggil konstruktor parent (person)
    public Employee(String id, String name, String phoneNumber, String position, double salary, String username, String password) {
        super(id, name, phoneNumber); // pemanggilan konstruktor superclass
        this.position = position;
        this.salary = salary;
        this.username = username;
        this.password = password;
    }

    // kumpulan getter dan setter
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // overriding: mengubah atau menimpa implementasi method dari parent class
    @Override
    public String getRoleDetails() {
        return "Employee - Position: " + position + ", Salary: $" + salary;
    }
}
