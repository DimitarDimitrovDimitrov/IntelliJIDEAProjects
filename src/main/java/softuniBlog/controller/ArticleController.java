package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import softuniBlog.bindingModel.ArticleBindingModel;
import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.entity.User;
import softuniBlog.repository.ArticleRepository;
import softuniBlog.repository.CategoryRepository;
import softuniBlog.repository.UserRepository;

import java.util.List;

@Controller
public class ArticleController {
@Autowired
private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String create(Model model){
List<Category> categories = this.categoryRepository.findAll();

        model.addAttribute("view", "article/create");
      model.addAttribute("categories",categories);

      return "base-layout";
    }

    @PostMapping("/article/create")
    @PreAuthorize("isAuthenticated()")
    public String createProcess(ArticleBindingModel articleBindingModel){
        UserDetails user = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();

        User userEntity = this.userRepository.findByEmail(user.getUsername());
        Category category = this.categoryRepository.getOne(articleBindingModel.getCategoryId());

        Article articleEntity = new Article(
                articleBindingModel.getTitle(),articleBindingModel.getContent(),
                userEntity,category);
        this.articleRepository.saveAndFlush(articleEntity);
        return "redirect:/";
    }

@GetMapping("/article/{id}")
    public String details(Model model,@PathVariable Integer id){
        if (!this.articleRepository.existsById(id)) {
        return "redirect:/";
        }
        //newstuff
    if(!(SecurityContextHolder.getContext().getAuthentication()instanceof AnonymousAuthenticationToken)){
        UserDetails principal = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User entityUser = this.userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user",entityUser);
    }
        Article article = this.articleRepository.getOne(id);
        model.addAttribute("article",article);
        model.addAttribute("view","article/details");
        return "base-layout";
        }

        @GetMapping ("/article/edit/{id}")
        @PreAuthorize("isAuthenticated()")
        public String edit(@PathVariable Integer id, Model model)
        {
            if(!this.articleRepository.existsById(id)){
                return "redirect:/";
            }
            Article article = this.articleRepository.getOne(id);


            if(!isUserAuthorOrAdmin(article)){
                return "redirect:/article/" + id;
            }
            List<Category> categories = this.categoryRepository.findAll();

            model.addAttribute("view","article/edit");
            model.addAttribute("article",article);
            model.addAttribute("categories",categories);

            return "base-layout";
        }

        @PostMapping("/article/edit/{id}")
        @PreAuthorize("isAuthenticated()")
    public String editProcess(@PathVariable Integer id, ArticleBindingModel articleBindingModel)
        {
            if(!this.articleRepository.existsById(id)){
                return "redirect:/";
            }
            Article article = this.articleRepository.getOne(id);


            if(!isUserAuthorOrAdmin(article)){
                return "redirect:/article/" + id;
            }

            Category category = this.categoryRepository.getOne(articleBindingModel.getCategoryId());

            article.setCategory(category);
            article.setContent(articleBindingModel.getContent());
            article.setTitle(articleBindingModel.getTitle());

            this.articleRepository.saveAndFlush(article);

            return "redirect:/article/" + article.getId();
        }

@GetMapping("/article/delete/{id}")
@PreAuthorize("isAuthenticated()")
    public String delete(Model model, @PathVariable Integer id) {
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.getOne(id);
        //further check needed
    if(!isUserAuthorOrAdmin(article)){
        return "redirect:/article/" + id;
    }

        model.addAttribute("article",article);
        model.addAttribute("view", "article/delete");
        return "base-layout";
}

@PostMapping("/article/delete/{id}")
@PreAuthorize("isAuthenticated()")
public String deleteProcess(@PathVariable Integer id){
        if(!this.articleRepository.existsById(id)){
            return "redirect:/";
        }
        Article article = this.articleRepository.getOne(id);
        //further check needed
    if(!isUserAuthorOrAdmin(article)){
        return "redirect:/article/" + id;
    }
        this.articleRepository.delete(article);

        return "redirect:/";
}
private  boolean isUserAuthorOrAdmin(Article article){
        UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User userEntity = this.userRepository.findByEmail(user.getUsername());
        return userEntity.isAdmin() || userEntity.isAuthor(article);

}
}

