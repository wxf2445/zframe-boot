package com.zlzkj.app.model;

import java.io.Serializable;
import java.util.HashMap;

public class Row extends HashMap<String, Object> implements Serializable {
    private static final long serialVersionUID = 129701856673425221L;

    public Row() {
    }

    public Object get(Object key) {
        if (super.get(key) != null) {
            String oType = super.get(key).getClass().getSimpleName();
            if (oType.equals("String[]")) {
                return ((String[])((String[])super.get(key)))[0];
            }

            if (oType.equals("byte[]")) {
                return new String((byte[])((byte[])super.get(key)));
            }
        }

        return super.get(key);
    }

    public Integer getInt(Object key) {
        return Integer.valueOf(this.get(key).toString());
    }

    public Long getLong(Object key) {
        return Long.valueOf(this.get(key).toString());
    }

    public Short getShort(Object key) {
        return Short.valueOf(this.get(key).toString());
    }

    public Byte getByte(Object key) {
        return Byte.valueOf(this.get(key).toString());
    }

    public Float getFloat(Object key) {
        return Float.valueOf(this.get(key).toString());
    }

    public Double getDouble(Object key) {
        return Double.valueOf(this.get(key).toString());
    }

    public String getString(Object key) {
        return this.get(key).toString();
    }
}

