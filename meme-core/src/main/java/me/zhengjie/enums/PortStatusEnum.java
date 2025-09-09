package me.zhengjie.enums;

public enum PortStatusEnum {

    PORT_A("A", "A面"),
    PORT_B("B", "B面"),
    ;
    private String code;

    private String desc;

    PortStatusEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
