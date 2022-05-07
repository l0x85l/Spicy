package com.spicy.setting;

public abstract class StateListener<X> {
    public abstract boolean onChange(X oldValue, X newValue);
}