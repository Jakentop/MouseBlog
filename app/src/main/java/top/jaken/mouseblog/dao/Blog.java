package top.jaken.mouseblog.dao;

/**
 * 博客的Dao用于博客的临时存放
 */
public class Blog {
    private  String blogTitle;
    private String blogBody;
    private Integer tagId;

    public String getBlogTitle() {
        return blogTitle;
    }

    public void setBlogTitle(String blogTitle) {
        this.blogTitle = blogTitle;
    }

    public String getBlogBody() {
        return blogBody;
    }

    public void setBlogBody(String blogBody) {
        this.blogBody = blogBody;
    }

    public Integer getTagId() {
        return tagId;
    }

    public void setTagId(Integer tagId) {
        this.tagId = tagId;
    }

    public Blog(Integer tagId) {
        this.blogTitle = null;
        this.blogBody = null;
        this.tagId = tagId;
    }

    public Blog(String blogTitle, String blogBody, Integer tagId) {
        this.blogTitle = blogTitle;
        this.blogBody = blogBody;
        this.tagId = tagId;
    }
}
