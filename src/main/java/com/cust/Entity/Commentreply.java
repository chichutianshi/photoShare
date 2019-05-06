package com.cust.Entity;

public class Commentreply {
    private Integer id;

    private String commentid;

    private String fromid;

    private String fromname;

    private String content;

    private String createtime;

    private String fromurl;

    public String getFromurl() { return fromurl; }

    public void setFromurl(String fromurl) { this.fromurl = fromurl; }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCommentid() {
        return commentid;
    }

    public void setCommentid(String commentid) {
        this.commentid = commentid == null ? null : commentid.trim();
    }

    public String getFromid() {
        return fromid;
    }

    public void setFromid(String fromid) {
        this.fromid = fromid == null ? null : fromid.trim();
    }

    public String getFromname() {
        return fromname;
    }

    public void setFromname(String fromname) {
        this.fromname = fromname == null ? null : fromname.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime == null ? null : createtime.trim();
    }

    @Override
    public String toString() {
        return "Commentreply{" +
                "id=" + id +
                ", commentid='" + commentid + '\'' +
                ", fromid='" + fromid + '\'' +
                ", fromname='" + fromname + '\'' +
                ", content='" + content + '\'' +
                ", createtime='" + createtime + '\'' +
                ", fromurl='" + fromurl + '\'' +
                '}';
    }
}