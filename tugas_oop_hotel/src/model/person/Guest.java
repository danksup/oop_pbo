// class untuk entitas tamu hotel, turunan dari class person
package model.person;

public class Guest extends Person {
    private String address;

    public Guest(String id, String name, String phoneNumber, String address) {
        super(id, name, phoneNumber);
        this.address = address;
    }

    
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String getRoleDetails() {
        return "Guest - Address: " + address;
    }
}
