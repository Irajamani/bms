package com.rajamani.bms.Controllers;


import com.rajamani.bms.Exception.NotFoundException;
import com.rajamani.bms.Service.ArticleService;
import com.rajamani.bms.Service.UserService;
import com.rajamani.bms.domain.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    private void throwNotFoundExc(String link) {
        throw new NotFoundException("Article not found with id "+link);
    }


}
