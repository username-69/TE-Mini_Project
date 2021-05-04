package com.example.vaccinationtracker;

import java.util.List;

public class userDB {
    private List<childDB> userChildren;

    public userDB() {
    }

    public userDB(List<childDB> userChildren) {
        this.userChildren = userChildren;
    }

    public List<childDB> getUserChildren() {
        return userChildren;
    }

    public void setUserChildren(List<childDB> userChildren) {
        this.userChildren = userChildren;
    }
}
