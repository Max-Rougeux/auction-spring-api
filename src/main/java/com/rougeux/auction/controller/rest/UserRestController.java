package com.rougeux.auction.controller.rest;

import com.rougeux.auction.service.UserService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.user.UserDto;
import com.rougeux.auction.web.user.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService service;
    private final ApiResponseFactory factory;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAll(
            @RequestParam(defaultValue = "1") int page) {
         return ResponseEntity.ok(
                 factory.success(ApiCodes.CD_DATA_SUCCESS, "users.findAll.success",
                         service.getAll(page)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<UserSummaryDto>> getBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "users.findBy.success",
                        service.getBySlug(slug)));
    }
}
