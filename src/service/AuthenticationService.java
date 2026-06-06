// service memvalidasi username dan password saat login (single responsibility)
package service;

import model.person.Employee;

public class AuthenticationService {
    
    public static Employee authenticate(String username, String password) {
        HotelManager manager = HotelManager.getInstance();
        for (Employee emp : manager.getEmployees()) {
            if (emp.getUsername().equals(username) && emp.getPassword().equals(password)) {
                return emp;
            }
        }
        return null;
    }
}
