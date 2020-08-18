package com.rajamani.bms.Controllers;


import com.rajamani.bms.Exception.NotFoundException;
import com.rajamani.bms.Service.ArticleService;
import com.rajamani.bms.Service.UserService;
import com.rajamani.bms.domain.Article;
import com.rajamani.bms.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal UserDetails userDetails,
                        @RequestParam(required = false, value = "q") String q, @RequestParam(required = false, value = "page") Integer page,
                        @RequestParam(required = false, value = "size") Integer size){
        if (q == null){
            model.addAttribute("articles", articleService.getAll(getPageable(page, size)));
        } else {
            model.addAttribute("articles", articleService.search(q, getPageable(page, size)));
        }
        return "articles/index";
    }

    private Pageable getPageable(Integer page, Integer size) {
        if (page == null || size == null) {
            return PageRequest.of(0, 15);
        }
        return PageRequest.of(page, size, Sort.by(Sort.DEFAULT_DIRECTION, "createdDate"));
    }

    @GetMapping("/show/{link}")
    public String getBlogByLink(@AuthenticationPrincipal UserDetails userDetails,
                                @PathVariable String link, Model model){

        Optional<Article> article = articleService.getByLink(link);
        if(article.isPresent()){
            model.addAttribute("article", article.get());
        } else {
            throwNotFoundExc(link);
        }
        return "articles/showarticle";
    }

    private String throwNotFoundExc(String link) {
        throw new NotFoundException("Article not found with id "+link);
    }

    @GetMapping("/new")
    public String newPostArticle(){
        return "articles/createarticle";
    }

    @GetMapping("/edit/{id}")
    public String edit(@AuthenticationPrincipal UserDetails userDetails,
                       @PathVariable String id, Model model){

        Optional<Article> article = articleService.getById(id);
        if(article.isPresent()){
            model.addAttribute("article", article.get());
        } else {
            return throwNotFoundExc(id);
        }
        return "articles/createarticle";
    }

    @PostMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal UserDetails userDetails,
                         @PathVariable String id, Model model){
        articleService.deleteById(id);
        model.addAttribute("message","Article id: "+id+" is deleted");
        model.addAttribute("articles", articleService.getAll(PageRequest.of(0, 10)));
        return "articles/index";
    }

    @PostMapping
    public String save(@AuthenticationPrincipal UserDetails userDetails, Article article,
                       Model model){
        if (article.getId() == null || article.getId().length() == 0){
            User user = userService.getUserByName(userDetails.getUsername());
            article.setAuthor(user);
        } else {
            Optional<Article> optionalArticle = articleService.getById(article.getId());
            if(optionalArticle.isPresent()){
                article.setAuthor(optionalArticle.get().getAuthor());
            }
        }
        articleService.save(article);
        return "redirect:/articles/show"+article.getLink();
    }

    @GetMapping("/rest")
    @ResponseBody
    public Page<Article> articlesRest(@RequestParam(required = false, value = "page") Integer page,
                                      @RequestParam(required = false, value = "size") Integer size) {
        return articleService.getAll(getPageable(page, size));
    }


}
