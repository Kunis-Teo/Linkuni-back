package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.dto.tag.TagListDTO;
import com.kunis.linkuni.entity.Tag;
import com.kunis.linkuni.entity.User;
import com.kunis.linkuni.repository.TagRepository;
import com.kunis.linkuni.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;
    private final UserRepository userRepository;

    public Tag registerTag(Tag tag, String userId){
        // 로그인한 유저 찾기
        User user = userRepository.findById(userId).get();
        // 로그인한 유저가 동일한 이름의 카테고리가 존재하면 예외 발생
        if(tagRepository.existsByUserAndName(user, tag.getName())){
            log.warn("tag name already exists {}", tag.getName());
            throw new RuntimeException("tag name already exists");
        }

        tag.setUser(user); // user 설정
        return tagRepository.save(tag); // 저장
    }

    public TagListDTO getTagList(String userId){
        // 로그인한 유저가 등록한 테그 조회
        List<Tag> tagList = tagRepository.findByUser(userRepository.findById(userId).get());

        TagListDTO tagListDTO = new TagListDTO(); // 반환 객체 선언
        List<TagDTO> tagDTOList = new ArrayList<>(); // 반환 객체에 저장할 리스트 선언
        // 리스트에 조회한 태그 정보 저장
        for(int i=0;i<tagList.size();i++){
            tagDTOList.add(new TagDTO(tagList.get(i).getId(), tagList.get(i).getName()));
        }

        tagListDTO.setTagDTOList(tagDTOList); // 반환 객체에 태그 정보 저장 리스트 저장
        return tagListDTO;
    }

}
