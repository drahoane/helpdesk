package cz.cvut.fel.b221.earomo.seminar.helpdesk.mock;

import cz.cvut.fel.b221.earomo.seminar.helpdesk.factory.UserFactory;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.CustomerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.EmployeeUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.ManagerUser;
import cz.cvut.fel.b221.earomo.seminar.helpdesk.model.UserType;

public class UserMock {
    public static void mock() {
        UserFactory userFactory = new UserFactory();
        CustomerUser cu1 = (CustomerUser) userFactory.createUser("Alan", "Black", "alan@black.com", "none", UserType.CUSTOMER);
        CustomerUser cu2 = (CustomerUser) userFactory.createUser("Miriam", "Orange", "miriam@orange.com", "none", UserType.CUSTOMER);
        EmployeeUser eu1 = (EmployeeUser) userFactory.createUser("John", "Smith", "john@smith.com", "none", UserType.EMPLOYEE);
        ManagerUser mu1 = (ManagerUser) userFactory.createUser("Peter", "Tee", "peter@tee.com", "none", UserType.MANAGER);
    }
}
