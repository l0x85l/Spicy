package com.spicy.setting;

import com.spicy.setting.interfaces.ICustomSetting;

public class $<X> {

    private String label;
    private X val;
    private X min;
    private X max;
    private StateListener stateListener;

    public $(X _val) {
        val = _val;
    }

    public $(X _val, X _min, X _max) {
        val = _val;
        min = _min;
        max = _max;
    }

    public void setVal(X _val) {
        if (stateListener != null) {
            if (!stateListener.onChange(val, _val)) {
                return;
            }
        }
        this.val = _val;
    }

    public void setValRaw(Object val) {
        setVal((X) val);
    }

    public X val() {
        return val;
    }

    public void load(Object object) {

        // todo check this code
        if (val.getClass().isInstance( ICustomSetting.class )) {
            ((ICustomSetting)val).load( (String) object );
        } else {
            if (object instanceof String && !(val instanceof String)) {

                /*
                <int, float, double, enum, long, byte, short>
                load from string
                 */
                if (val instanceof Integer) {
                    setVal((X) Integer.valueOf((String) object));
                } else if (val instanceof Boolean) {
                    if (((String) object).isEmpty()) {
                        setValRaw(!(Boolean)val);
                    } else {
                        setValRaw(Boolean.valueOf((String) object));
                    }
                } else if (val instanceof Float) {
                    setVal((X) Float.valueOf((String) object));
                } else if (val instanceof Double) {
                    setVal((X) Double.valueOf((String) object));
                } else if (val instanceof Long) {
                    setVal((X) Long.valueOf((String) object));
                } else if (val instanceof Byte) {
                    setVal((X) Byte.valueOf((String) object));
                } else if (val instanceof Short) {
                    setVal((X) Short.valueOf((String) object));
                } else if (val instanceof Enum) {
                    try {
                        for (Object obj : ((Enum)val).getDeclaringClass().getEnumConstants()) {
                            Enum en = (Enum)obj;
                            if (en.name().equalsIgnoreCase((String) object)) {
                                setVal((X) en);
                                break;
                            }
                        }
                    } catch (Exception ex) {

                    }
                } else
                    setVal((X) object);
            } else {
                setVal((X) object);
            }
        }
    }

    public void setStateListener(StateListener stateListener) {
        this.stateListener = stateListener;
    }

    public StateListener stateListener() {
        return stateListener;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }

    public X max() {
        return max;
    }

    public X min() {
        return min;
    }
}