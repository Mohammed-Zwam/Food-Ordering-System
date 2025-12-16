package com.pattern.food_ordering_system.entity;
import com.pattern.food_ordering_system.model.user.Role;
import javafx.scene.image.ImageView;

public class User {
    private long id;
    private String userName;
    private String phoneNumber;
    private String zone;
    private String userImgPath;
    private String password;
    private String confirmPassword;
    private Role role;
    private ImageView imageProfile;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getUserImgPath() {
        return userImgPath;
    }

    public void setUserImgPath(String userImgPath) {
        this.userImgPath = userImgPath;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImageView getImageProfile() {
        return imageProfile;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setImageProfile(ImageView imageProfile) {
        this.imageProfile = imageProfile;
    }
}
