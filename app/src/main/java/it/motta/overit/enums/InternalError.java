package it.motta.overit.enums;

public enum InternalError {

    GENERIC_ERROR(-500);

   private int code;

    InternalError(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return this.name();
    }

}
