package com.tms.api.enums;

public enum GeoEnum {
    VN(4, "Vietnam"),
    INDO(9, "Indonesia"),
    TH(10, "Thailand"),
    ;

    private final Integer id;
    private final String name;

    GeoEnum(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static GeoEnum convert(Integer id) {
        switch (id) {
            case 4:
                return VN;
            case 9:
                return INDO;
            case 10:
                return TH;
            default:
                return null;
        }
    }
}
