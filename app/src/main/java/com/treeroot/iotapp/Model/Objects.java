package com.treeroot.iotapp.Model;

import com.google.gson.annotations.SerializedName;

public class Objects {

    String id, name;
    @SerializedName("created_at")
    String created;
    @SerializedName("project_id")
    String projectId;
    @SerializedName("is_active")
    boolean isActive;
    @SerializedName("updated_at")
    String updated;
    @SerializedName("user_id")
    String userId;

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

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }


    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }


}
