package com.treeroot.iotapp.Model;

import java.util.ArrayList;
import java.util.List;

public class DeviceType {
    List<DeviceType> objects = null;

    public List<DeviceType> getObjects() {
        return objects;
    }

    public void setObjects(List<DeviceType> objects) {
        this.objects = objects;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String id,name;
}
