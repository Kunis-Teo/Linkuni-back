package com.kunis.linkuni.service;

import com.kunis.linkuni.dto.tag.TagDTO;
import com.kunis.linkuni.dto.tag.TagListDTO;
import com.kunis.linkuni.entity.Tag;
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
        tag.setUser(userRepository.findById(userId).get());
        return tagRepository.save(tag);
    }

    public TagListDTO getTagList(String userId){
        List<Tag> tagList = tagRepository.findByUser(userRepository.findById(userId).get());
        TagListDTO tagListDTO = new TagListDTO();
        List<TagDTO> tagDTOList = new ArrayList<>();
        for(int i=0;i<tagList.size();i++){
            tagDTOList.add(new TagDTO(tagList.get(i).getId(), tagList.get(i).getName()));
        }
        tagListDTO.setTagDTOList(tagDTOList);
        return tagListDTO;
    }

}
