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
        urlListDTO.setCategory_id(categoryId); // 반환 객체에 카테고리 아이디 설정

        List<UrlDTO> urlDTOList = new ArrayList<>(); // url 저장 리스트 생성
        for(int i=0;i<urlList.size();i++){ // 조회한 모든 url에 대해서
            List<UrlTag> urlTags = urlList.get(i).getUrlTagList(); // i번째 url의 모든 tag
            List<TagDTO> tagDTOList = new ArrayList<>();
            for(int j=0;j<urlTags.size();j++){ // 모든 tag정보 저장
                tagDTOList.add(new TagDTO(urlTags.get(j).getTag().getId(), urlTags.get(j).getTag().getName()));
            }
            // url 정보 저장
            urlDTOList.add(new UrlDTO(urlList.get(i).getId(), urlList.get(i).getUrl(),
                    urlList.get(i).getMemo(), urlList.get(i).getIsStarred(),
                    urlList.get(i).getIsWatched(), urlList.get(i).getCreateAt(), urlList.get(i).getWatchedAt(),
                    urlList.get(i).getCategory().getId(), tagDTOList));
        }

        urlListDTO.setUrlDTOList(urlDTOList); // 반환 객체 저장
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
        Url url = urlRepository.findById(urlId).get(); // url 조회
        if(url.getIsStarred()) url.setIsStarred(false); // true라면 false로 설정
        else url.setIsStarred(true); // false 라면 true로 설정

        return url;
    }

    public Url watchUrl(String urlId){
        Url url = urlRepository.findById(urlId).get(); // url 조회
        url.setIsWatched(true); // Iswatch 칼럼 true로 설정
        url.setWatchedAt(LocalDateTime.now()); // watchedAt 현재 날짜로 업데이트
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
