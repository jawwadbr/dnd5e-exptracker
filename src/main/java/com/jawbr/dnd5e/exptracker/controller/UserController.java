package com.jawbr.dnd5e.exptracker.controller;

import com.jawbr.dnd5e.exptracker.dto.request.UserRequestDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCampaignsDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserCreationDTO;
import com.jawbr.dnd5e.exptracker.dto.response.UserDTO;
import com.jawbr.dnd5e.exptracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user-info")
    public UserDTO userCheckInfo() {
        return userService.userCheckInfo();
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreationDTO> registerUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.registerUser(userRequestDTO), HttpStatus.CREATED);
    }

    //    @GetMapping("/joined-campaigns")
//    public UserCampaignsDTO getJoinedCampaigns() {
//        return userService.getJoinedCampaigns();
//    }
    @GetMapping("/joined-campaigns")
    public Page<UserCampaignsDTO> getJoinedCampaigns(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer pageSize
    )
    {
        return userService.getJoinedCampaigns(page, pageSize);
    }

}
