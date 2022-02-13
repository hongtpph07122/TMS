package com.tms.api.params;

public class ConstParams {

    public enum RescueStatus {
        NEW(1);

        int value;

        RescueStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}
