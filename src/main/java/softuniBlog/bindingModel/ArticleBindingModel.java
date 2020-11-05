package softuniBlog.bindingModel;


import com.sun.istack.NotNull;

public class ArticleBindingModel {
    private Integer categoryId;

    public Integer getCategoryId(){return categoryId;}

    public void setCategoryId(Integer categoryId){this.categoryId = categoryId;}

    @NotNull
    private String title;

    @NotNull
    private String content;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
