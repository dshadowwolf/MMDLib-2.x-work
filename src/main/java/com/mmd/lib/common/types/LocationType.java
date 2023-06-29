package com.mmd.lib.common.types;

public enum LocationType {
    NONE("NONE"),
    MATERIAL("MATERIAL"),
    BLOCK("BLOCK"),
    ITEM("ITEM");

    private final String myTypeName;

    LocationType(String typeName) {
        myTypeName = typeName;
    }

    @Override
    public String toString() {
        return "LocationType{" +
                "myTypeName='" + myTypeName + '\'' +
                '}';
    }

    public String getMyTypeName() {
        return myTypeName;
    }
}
