package com.spicy.user;

import lombok.Getter;
import lombok.Setter;

public class User {
    @Getter @Setter private String name;

    public void put(String _name) {
        this.name = _name;
    }


}
