package com.treeroot.iotapp.Model;

public class AddData {
    String name,charge,signal,status,type;
    boolean is_active;
    int project_id,room_id;

    public AddData(String name, boolean is_active) {
        this.name = name;
        this.is_active = is_active;
    }

    public AddData(boolean is_active, String name, int project_id) {
        this.is_active = is_active;
        this.name = name;
        this.project_id = project_id;
    }

    public AddData(String charge, boolean is_active, String name, int project_id, int room_id, String signal, String status, String type) {
        this.charge = charge;
        this.is_active = is_active;
        this.name = name;
        this.project_id = project_id;
        this.room_id = room_id;
        this.signal = signal;
        this.status = status;
        this.type = type;
    }
}
