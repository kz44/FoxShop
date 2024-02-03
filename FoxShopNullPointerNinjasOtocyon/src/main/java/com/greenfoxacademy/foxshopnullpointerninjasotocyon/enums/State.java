package com.greenfoxacademy.foxshopnullpointerninjasotocyon.enums;

public enum State {
    PENDING("pending"), ACCEPTED("accepted"), DENIED("denied");

    private String statusValue;

    private State (String value){
        statusValue = value;
    }
    public String getStatusValue(){
        return statusValue;
    }

}
