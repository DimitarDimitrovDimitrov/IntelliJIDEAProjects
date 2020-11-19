package softuniBlog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import softuniBlog.entity.Article;
import softuniBlog.entity.Category;
import softuniBlog.repository.CategoryRepository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {
@Autowired
private CategoryRepository categoryRepository;
    @GetMapping("/")
    public String index(Model model){
        List<Category> categories = this.categoryRepository.findAll();


        model.addAttribute("view","home/index");
       model.addAttribute("categories", categories);
        return "base-layout";
    }

    @RequestMapping("/error/403")
    public String accessDenied(Model model){
        model.addAttribute("view","error/403");

        return "base-layout";
    }
@GetMapping ("/category/{id}")
public String listArticles(Model model, @PathVariable Integer id) {
	
	if(!this.categoryRepository.existsById(id)) {
		return "redirect:/";
	}
	model.addAttribute("view","home/list-articles");
	Category category = this.categoryRepository.getOne(id);
	Set<Article>articles = category.getArticles();
	
	model.addAttribute("articles",articles);
	model.addAttribute("category",category);
	return "base-layout";
}



}

