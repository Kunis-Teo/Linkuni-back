package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.tag.TagAddDto;
import com.kunis.linkuni.entity.Tag;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.UrlTag;
import com.kunis.linkuni.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UserUrlRepository userUrlRepository;
    private final UrlTagRepository urlTagRepository;
    private final UserRepository userRepository;

    public Url add(final Url urlEntity, final String userId){
        if(urlEntity==null || urlEntity.getUrl()==null){
            throw new RuntimeException("Invalid arguments");
        }
        final String url = urlEntity.getUrl();
        if(urlRepository.existsByUrlAndUser(url, userRepository.findById(userId).get())){
            log.warn("url already exists {}", url);
            throw new RuntimeException("url already exists");
        }
        urlEntity.setUser(userRepository.findById(userId).get());

        return urlRepository.save(urlEntity);
    }

    public void addTagToUrl(List<TagAddDto> tagList, Url url){
        try{
            for(int i=0;i<tagList.size();i++){
                Tag tag = tagRepository.findById(tagList.get(i).getTag_id()).get();
                UrlTag urlTag = new UrlTag(url, tag);
                urlTagRepository.save(urlTag);
            }
        }catch (Exception e) {
            throw new RuntimeException("add Tag to url error");
        }

    }


}
