// abstract class untuk merepresentasikan orang secara umum (tamu atau pegawai)
package model.person;

import java.io.Serializable;

public abstract class Person implements Serializable {
    private String id;
    private String name;
    private String phoneNumber;

    // konstruktor
    public Person(String id, String name, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    // setter getter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    // abstract
    public abstract String getRoleDetails();
}
