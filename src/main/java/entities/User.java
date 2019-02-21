package entities;

import javax.persistence.*;

@Entity
@Table(name = "USER")
public class User {
    @Id
    @Column(name = "LIB_CARD_NUM", length = 10)
    private String libCardNum;
    @Column(name = "PASSWORD", length = 10)
    private String password;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "USER_TYPE")
    private UserType userType;
    @Column(name = "HAS_DEBT")
    private boolean hasDebt;

    public User(String libCardNum, String password, UserType userType, boolean hasDebt) {
        this.libCardNum = libCardNum;
        this.password = password;
        this.userType = userType;
        this.hasDebt = hasDebt;
    }

    public User() {
    }

    public String getLibCardNum() {
        return libCardNum;
    }

    public void setLibCardNum(String libCardNum) {
        this.libCardNum = libCardNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isHasDebt() {
        return hasDebt;
    }

    public void setHasDebt(boolean hasDebt) {
        this.hasDebt = hasDebt;
    }
}
