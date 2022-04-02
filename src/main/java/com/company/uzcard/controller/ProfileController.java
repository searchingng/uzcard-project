package com.company.uzcard.controller;

import com.company.uzcard.dto.ProfileDTO;
import com.company.uzcard.service.ProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;

@RestController
@RequestMapping("/profile")
@Slf4j
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    public ResponseEntity<ProfileDTO> create(@RequestBody ProfileDTO dto){
        log.info("New User registrated: {}", dto);
        return ResponseEntity.ok(profileService.save(dto));
    }

    @GetMapping
    public ResponseEntity<List<ProfileDTO>> getAll(){
        log.debug("All User Request");
        return ResponseEntity.ok(profileService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDTO> getById(@PathVariable Long id){
        log.debug("getting request for User ID: {}", id);
        return ResponseEntity.ok(profileService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateById(@PathVariable Long id,
                                             @RequestBody ProfileDTO dto){
        dto.setId(id);
        try {
            profileService.updateById(dto, id);
            log.info("User is updated: {}", dto);
        } catch (RuntimeException e){
            log.error("User Not Found, id = {}", id);
        }
        return ResponseEntity.ok("Successfully Saved!!!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id){
        log.info("User is deleted, id = {}", id);
        profileService.deleteById(id);
        return ResponseEntity.ok("Successfully Deleted!!!");
    }



}
