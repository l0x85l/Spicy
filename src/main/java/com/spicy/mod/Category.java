package com.spicy.mod;

public enum Category
{
    COMBAT("Combat"),
    MOVEMENT("Movement"),
    RENDER("Render"),
    PLAYER("Player");

    private final String name;


    Category(final String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}