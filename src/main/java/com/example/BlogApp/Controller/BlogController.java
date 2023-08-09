package com.example.BlogApp.Controller;

import com.example.BlogApp.Entity.Blog;
import com.example.BlogApp.Service.BlogService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;
    /*
     *   Adding initBinder to convert trim input string
     *   Removing leading and trailing whitespaces
     *   Resolve issues for validation
     * */
    @InitBinder
    public void initBinder(WebDataBinder webDataBinder){
        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        webDataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }


    @GetMapping("/")
    public String indexApi(Model model){
        List<Blog> blogs = blogService.getAllBlogs();
        int blogCount = blogs.size();
        model.addAttribute("blogs", blogs);
        model.addAttribute("blogCount", blogCount);
        model.addAttribute("pageTitle", "Blogger | Home");
        return "index";
    }

    @GetMapping("/blog/{id}")
    public String showBlog(@PathVariable int id, Model model){
        Blog blog = blogService.getBlogById(id);
        if(blog == null){
            return "redirect:/";
        }
        model.addAttribute("blog", blog);
        model.addAttribute("pageTitle", "Blogger | Blog");
        return "blog";
    }

    @GetMapping("/publish-blog")
    public String getPublishBlog(Model model){
        model.addAttribute("blog", new Blog());
        model.addAttribute("pageTitle", "Blogger | Add Blog");
        return "add-blog";
    }

    @PostMapping("/publish-blog")
    public String postPublishBlog(@Valid @ModelAttribute("blog") Blog blog, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "add-blog";
        }else{
            if (blog.getId() != null) {
                // Blog already has an ID, indicating an update
                Blog existingBlog = blogService.getBlogById(blog.getId());
                if (existingBlog != null) {
                    // Preserving the createdAt value from the existing blog
                    blog.setCreatedAt();
                }
            }

            blogService.saveBlog(blog);
            return "redirect:/";
        }
    }

    @RequestMapping("edit-blog/{id}")
    public ModelAndView editBlog(@PathVariable int id, Model model){
        ModelAndView modelAndView = new ModelAndView("add-blog");
        model.addAttribute("pageTitle", "Blogger | Edit Blog");
        Blog blog = blogService.getBlogById(id);
        modelAndView.addObject(blog);
        return modelAndView;
    }


    @RequestMapping("/delete-blog/{id}")
    public String deleteBlog(@PathVariable int id){
        blogService.deleteBlogById(id);
        return "redirect:/";
    }


    /* Handle 404 - Not Found */
    @RequestMapping(value = "/{path:[^\\.]*}")
    public String handle404(@PathVariable String path) {
        return "page-404";
    }
}
