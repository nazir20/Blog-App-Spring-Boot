package com.example.BlogApp.Service;

import com.example.BlogApp.Entity.Blog;
import com.example.BlogApp.Repository.BlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    private BlogRepository blogRepository;
    public BlogService(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
    }
    /* GET Requests */

    public List<Blog> getAllBlogs(){
        return blogRepository.findAll();
    }

    public Blog getBlogById(int id){
        return blogRepository.findById(id).get();
    }

    /* POST Requests*/

    public void saveBlog(Blog blog){
        blogRepository.save(blog);
    }

    /* DELETE Requests */

    public void deleteBlogById(int id){
        blogRepository.deleteById(id);
    }
}
