package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import softuniBlog.entity.Article;
import softuniBlog.repository.ArticleRepository;

import java.util.List;

@Controller
public class HomeController {
@Autowired
private ArticleRepository articleRepository;
    @GetMapping("/")
    public String index(Model model){
        List<Article> articles = this.articleRepository.findAll();


        model.addAttribute("view" ,"home/index");
       model.addAttribute("articles", articles);
        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view","error/403");

        return "base-layout";
    }

}

