package com.rajamani.bms.Service;


import com.rajamani.bms.domain.Article;
import com.rajamani.bms.repo.ArticleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepo articleRepo;

    public Article save(Article article){
        if(article.getId() == null){
            article.setId(UUID.randomUUID().toString());
        }
        return articleRepo.save(article);
    }

    public Page<Article> getAll(Pageable pageable){
        return articleRepo.findAll(pageable);
    }

    public Optional<Article> getByLink(String link){
        return articleRepo.findByLink(link);
    }

    public Optional<Article> getById(String id){
        return articleRepo.findById(id);
    }

    public void deleteById(String id){
        articleRepo.deleteById(id);
    }

    public Page<Article> search(String q, Pageable pageable){
        return articleRepo.findByTitleContainingAndBodyContaining(q, q, pageable);
    }

}
