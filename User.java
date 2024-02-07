package Online_banking_management;

public class User {
    protected static int userId;
    private String userName;
    private String password;
    private String userType;
    private int age;

  
    public User(int userId, String userName, String password, String userType, int age) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
        this.userType = userType;
        this.age = age;
    }

  
    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getUserType() {
        return userType;
    }

    public long getAge() {
        return age;
    }

 
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
