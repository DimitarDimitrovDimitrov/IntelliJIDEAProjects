package softuniBlog.controller;


import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import softuniBlog.FileUploadUtil;
import softuniBlog.bindingModel.UserBindingModel;
import softuniBlog.entity.Role;
import softuniBlog.entity.User;
import softuniBlog.repository.RoleRepository;
import softuniBlog.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("view","user/register");
        return "base-layout";
    }
    @PostMapping("/register")
    public String registerProcess(UserBindingModel userBindingModel){
        if(!userBindingModel.getPassword().equals(userBindingModel.getConfirmPassword())){
            return "redirect:/register";
        }
        BCryptPasswordEncoder bCryptPasswordEncoder  = new BCryptPasswordEncoder();
        User user = new User(
                userBindingModel.getEmail(),userBindingModel.getFullName(),bCryptPasswordEncoder.encode(userBindingModel.getPassword()),userBindingModel.getPhotos()       );
Role userRole = this.roleRepository.findByName("ROLE_USER");
user.addRole(userRole);
this.userRepository.saveAndFlush(user);
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(Model model){
        model.addAttribute("view","user/login");
        return "base-layout";
    }

    @RequestMapping(value ="/logout", method = RequestMethod.GET)
    public String logoutPage(HttpServletRequest request, HttpServletResponse response){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth!= null){
            new SecurityContextLogoutHandler().logout(request,response,auth);
        }
        return "redirect:/login?logout";
    }

    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public String profilePage(Model model){
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        User user = this.userRepository.findByEmail(principal.getUsername());
        model.addAttribute("user", user);
        model.addAttribute("view","user/profile");
        return "base-layout";
    }
//imgs
    /*
     * For future reference
    @RequestMapping(value="/getUserImage/{id}")
    public void getUserImage(HttpServletResponse response , @PathVariable("id") int id) throws IOException{

     response.setContentType("image/jpeg");
     // byte[] buffer = tweetService.getTweetByID(tweetID).getUserImage();
     byte[] buffer = userRepository.getOne(id).getPhotos();
      InputStream in1 = new ByteArrayInputStream(buffer);
      IOUtils.copy(in1, response.getOutputStream());        
    }
*/
   
    //imgs end

}