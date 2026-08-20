package com.rougeux.auction.controller.rest;

import com.rougeux.auction.service.CategoryService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.category.CategoryCountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final CategoryService service;
    private final ApiResponseFactory factory;

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryCountDto>>> getAll() {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "categories.findAll.success",
                        service.getAll()));
    }
}