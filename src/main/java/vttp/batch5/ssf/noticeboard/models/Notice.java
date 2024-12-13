package vttp.batch5.ssf.noticeboard.models;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Notice {
    
    //title of notice
    @NotNull(message="please input a title")
    @Size(min=3, max=128, message="title must be between 3 and 128 characters")
    private String title;

    //email address of person creating notice
    @Email(message="must be a well-formed email address")
    @NotNull(message="please input an email address")
    private String poster;

    //date at which post should be posted, format yyyy-MM-dd
    @Future(message="please input a date beginning tomorrow")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private Date postDate;

    @NotEmpty(message="please select at least one category")
    private List<String> categories;

    @NotBlank(message="please input the content of your notice")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Notice [title=" + title + ", poster=" + poster + ", postDate=" + postDate + ", categories=" + categories
                + ", text=" + text + "]";
    }

    

}
