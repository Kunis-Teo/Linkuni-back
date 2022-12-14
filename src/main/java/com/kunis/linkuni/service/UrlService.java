package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.tag.TagAddDto;
import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.dto.url.UrlAllListDto;
import com.kunis.linkuni.dto.url.UrlDTO;
import com.kunis.linkuni.dto.url.UrlListDTO;
import com.kunis.linkuni.entity.*;
import com.kunis.linkuni.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class UrlService {

    private final UrlRepository urlRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;
    private final UrlTagRepository urlTagRepository;
    private final UserRepository userRepository;

    public Url add(final Url urlEntity, final String userId){
        User user = userRepository.findById(userId).get();
        if(urlEntity==null || urlEntity.getUrl()==null){
            throw new RuntimeException("Invalid arguments");
        }
        final String url = urlEntity.getUrl();
        if(urlRepository.existsByUrlAndUser(url, user)){
            log.warn("url already exists {}", url);
            throw new RuntimeException("url already exists");
        }
        urlEntity.setUser(user);

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
        List<Url> urlList = urlRepository.findByCategoryJoinFetch(categoryRepository.findById(categoryId).get());

        UrlListDTO urlListDTO = new UrlListDTO();
        urlListDTO.setCategory_id(categoryId); // ?????? ????????? ???????????? ????????? ??????

        List<UrlDTO> urlDTOList = new ArrayList<>(); // url ?????? ????????? ??????
        for(int i=0;i<urlList.size();i++){ // ????????? ?????? url??? ?????????
            List<UrlTag> urlTags = urlList.get(i).getUrlTagList(); // i?????? url??? ?????? tag
            List<TagDTO> tagDTOList = new ArrayList<>();
            for(int j=0;j<urlTags.size();j++){ // ?????? tag?????? ??????
                tagDTOList.add(new TagDTO(urlTags.get(j).getTag().getId(), urlTags.get(j).getTag().getName()));
            }
            // url ?????? ??????
            urlDTOList.add(new UrlDTO(urlList.get(i).getId(), urlList.get(i).getUrl(),
                    urlList.get(i).getMemo(), urlList.get(i).getIsStarred(),
                    urlList.get(i).getIsWatched(), urlList.get(i).getCreateAt(), urlList.get(i).getWatchedAt(),
                    urlList.get(i).getCategory().getId(), tagDTOList));
        }

        urlListDTO.setUrlDTOList(urlDTOList); // ?????? ?????? ??????
        return urlListDTO;
    }

    public UrlAllListDto getUrlAllList(String userId){
        List<Category> categoryList = categoryRepository.findByUserJoinFetch(userRepository.findById(userId).get());

        UrlAllListDto urlAllListDto = new UrlAllListDto();
        List<UrlListDTO> urlListDTOList = new ArrayList<>();

        for(int k=0;k<categoryList.size();k++){
            List<Url> urlList = categoryList.get(k).getUrlList();
            UrlListDTO urlListDTO = new UrlListDTO();
            urlListDTO.setCategory_id(categoryList.get(k).getId());
            List<UrlDTO> urlDTOList = new ArrayList<>();
            for(int i=0;i<urlList.size();i++){
                List<UrlTag> urlTags = urlTagRepository.findByUrl(urlList.get(i).getId());
                List<TagDTO> tagDTOList = new ArrayList<>();
                for(int j=0;j<urlTags.size();j++){
                    tagDTOList.add(new TagDTO(urlTags.get(j).getTag().getId(), urlTags.get(j).getTag().getName()));
                }
                urlDTOList.add(new UrlDTO(urlList.get(i).getId(), urlList.get(i).getUrl(),
                        urlList.get(i).getMemo(), urlList.get(i).getIsStarred(),
                        urlList.get(i).getIsWatched(), urlList.get(i).getCreateAt(), urlList.get(i).getWatchedAt(),
                        urlList.get(i).getCategory().getId(), tagDTOList));
            }
            urlListDTO.setUrlDTOList(urlDTOList);
            urlListDTOList.add(urlListDTO);
        }

        urlAllListDto.setUrlListDTOList(urlListDTOList);
        return urlAllListDto;
    }

    public Url setStart(String urlId){
        Url url = urlRepository.findById(urlId).get(); // url ??????
        if(url.getIsStarred()) url.setIsStarred(false); // true?????? false??? ??????
        else url.setIsStarred(true); // false ?????? true??? ??????

        return url;
    }

    public Url watchUrl(String urlId){
        Url url = urlRepository.findById(urlId).get(); // url ??????
        url.setIsWatched(true); // Iswatch ?????? true??? ??????
        url.setWatchedAt(LocalDateTime.now()); // watchedAt ?????? ????????? ????????????
        return url;
    }

    public void deleteUrl(String urlId){
        try {
            urlRepository.delete(urlRepository.findById(urlId).get());
        }catch(Exception e){
            log.warn("fail delete url id {}", urlId);
            throw new RuntimeException("fail delete url id");
        }

    }

    public List<Url> getAllUrl(String userId){
        return urlRepository.findByUser(userRepository.findById(userId).get());
    }

    public Url update(Url entity, String userId, String category_id, List<TagDTO> tagDTOList) {
        entity.setCategory(categoryRepository.findById(category_id).get());
        entity.setUser(userRepository.findById(userId).get());
        // tag ?????? ?????? ??????

        Url original = urlRepository.findByIdJoinFetch(entity.getId());
        original.setUrl(entity.getUrl());
        original.setMemo(entity.getMemo());
        original.setCategory(entity.getCategory());
        //original.setUrlTagList();
        urlRepository.save(original);

        return original;
    }
}
