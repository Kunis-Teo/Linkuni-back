package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.tag.TagAddDto;
import com.kunis.linkuni.dto.url.UrlDTO;
import com.kunis.linkuni.dto.url.UrlListDTO;
import com.kunis.linkuni.entity.Tag;
import com.kunis.linkuni.entity.Url;
import com.kunis.linkuni.entity.UrlTag;
import com.kunis.linkuni.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
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

    public UrlListDTO getUrlListByCategory(String categoryId){
        List<Url> urlList = urlRepository.findByCategory(categoryRepository.findById(categoryId).get());

        UrlListDTO urlListDTO = new UrlListDTO();
        urlListDTO.setCategory_id(categoryId);
        List<UrlDTO> urlDTOList = new ArrayList<>();
        for(int i=0;i<urlList.size();i++){
            urlDTOList.add(new UrlDTO(urlList.get(i).getId(), urlList.get(i).getUrl(),
                    urlList.get(i).getMemo(), urlList.get(i).getIsStarred(),
                    urlList.get(i).getIsWatched(), urlList.get(i).getCreateAt(), urlList.get(i).getWatchedAt(),
                    urlList.get(i).getCategory().getId()));
        }
        urlListDTO.setUrlDTOList(urlDTOList);
        return urlListDTO;
    }

    public Url setStart(String urlId){
        Url url = urlRepository.findById(urlId).get();
        if(url.getIsStarred()) url.setIsStarred(false);
        else url.setIsStarred(true);

        return url;
    }

    public void deleteUrl(String urlId){
        urlRepository.delete(urlRepository.findById(urlId).get());
    }

    public List<Url> getAllUrl(String userId){
        return urlRepository.findByUser(userRepository.findById(userId).get());
    }

    public List<Url> update(Url entity, String userId, String category_id) {
        final Optional<Url> original = urlRepository.findById(entity.getId());
        entity.setCategory(categoryRepository.findById(category_id).get());

        original.ifPresent(url -> {
            url.setUrl(entity.getUrl());
            url.setMemo(entity.getMemo());
            url.setCategory(entity.getCategory());

            urlRepository.save(url);
        });

        return getAllUrl(userId);
    }
}
