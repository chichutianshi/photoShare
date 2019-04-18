package com.cust.Entity;

public class Allphotos {
    private String photoid;

    private String ownerid;

    private String instruction;

    private String location;

    private String photourl;

    private Integer likenum;

    private String createTime;


    private String categories;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getPhotoid() {
        return photoid;
    }

    public void setPhotoid(String photoid) {
        this.photoid = photoid == null ? null : photoid.trim();
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid == null ? null : ownerid.trim();
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction == null ? null : instruction.trim();
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location == null ? null : location.trim();
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl == null ? null : photourl.trim();
    }

    public Integer getLikenum() {
        return likenum;
    }

    public void setLikenum(Integer likenum) {
        this.likenum = likenum;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", photoid=").append(photoid);
        sb.append(", ownerid=").append(ownerid);
        sb.append(", instruction=").append(instruction);
        sb.append(", location=").append(location);
        sb.append(", photourl=").append(photourl);
        sb.append(", likenum=").append(likenum);
        sb.append("]");
        return sb.toString();
    }
}