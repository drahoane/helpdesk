package cz.cvut.fel.b221.earomo.seminar.helpdesk.model.enumeration;

public enum Role {
    MANAGER("ROLE_MANAGER"), EMPLOYEE("ROLE_EMPLOYEE"), CUSTOMER("ROLE_CUSTOMER");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
