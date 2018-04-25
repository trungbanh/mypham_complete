package com.example.vuphu.app.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreatedOrder {

    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("ownerUid")
    @Expose
    private String ownerUid;
    @SerializedName("product")
    @Expose
    private String product;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUid() {
        return ownerUid;
    }

    public void setOwnerUid(String ownerUid) {
        this.ownerUid = ownerUid;
    }

}