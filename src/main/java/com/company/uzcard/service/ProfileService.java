package com.company.uzcard.service;

import com.company.uzcard.dto.ProfileDTO;
import com.company.uzcard.entity.ProfileEntity;
import com.company.uzcard.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {

    private final ProfileRepository profileRepository;

    public ProfileService(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    public ProfileDTO save(ProfileDTO dto){

        ProfileEntity entity = new ProfileEntity();

        entity.setName(dto.getName());
        entity.setSurname(dto.getSurname());
        entity.setMiddleName(dto.getMiddleName());
        entity.setBirthDate(dto.getBirthDate());
        entity.setCreatedAt(LocalDateTime.now());

        profileRepository.save(entity);
        return toDto(entity);
    }

    public ProfileEntity save(ProfileEntity entity){

        profileRepository.save(entity);
        return entity;
    }

    public ProfileEntity get(Long id){
        if (id == null)
            return null;

        return profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profile Not Found"));
    }

    public ProfileDTO getById(Long id){
        return toDto(get(id));
    }

    public List<ProfileDTO> getAll(){
        return profileRepository.findAll().stream()
                .map(this::toDto).collect(Collectors.toList());
    }

    public void updateById(ProfileDTO dto, Long id){
        ProfileEntity entity = get(id);

        if (dto.getName() != null){
            entity.setName(dto.getName());
        }

        if (dto.getSurname() != null){
            entity.setSurname(dto.getSurname());
        }

        if (dto.getMiddleName() != null){
            entity.setMiddleName(dto.getMiddleName());
        }

        if (dto.getBirthDate() != null){
            entity.setBirthDate(dto.getBirthDate());
        }

        profileRepository.save(entity);
    }

    public void deleteById(Long id){
        profileRepository.deleteById(id);
    }


    public ProfileDTO toDto(ProfileEntity entity){
        if (entity == null)
            return null;

        ProfileDTO dto = new ProfileDTO();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setSurname(entity.getSurname());
        dto.setMiddleName(entity.getMiddleName());
        dto.setBirthDate(entity.getBirthDate());
        dto.setCreatedAt(entity.getCreatedAt());

        return dto;
    }
}
