package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class User implements Serializable {

    //private static final long serialVersionUID = -7306724305413428761L;

    private String pwd;

    private String email;

    public User() {

    }

    public User(String email,String pwd) {
        this.email = email;
        this.pwd = pwd;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

