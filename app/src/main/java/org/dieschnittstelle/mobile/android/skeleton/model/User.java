package org.dieschnittstelle.mobile.android.skeleton.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

@Entity
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String mail;

    private String pw;

    public User(){

    }

    public User(String email, String pw){
        this.mail = email;
        this.pw = pw;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getMail() {
        return mail;
    }

    public String getPw() {
        return pw;
    }
}

