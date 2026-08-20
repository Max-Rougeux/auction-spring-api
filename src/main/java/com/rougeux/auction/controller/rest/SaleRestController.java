package com.rougeux.auction.controller.rest;

import com.rougeux.auction.service.SaleService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.sale.SaleCardDto;
import com.rougeux.auction.web.sale.SaleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sales")
public class SaleRestController {

    private final SaleService service;
    private final ApiResponseFactory factory;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SaleCardDto>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String category) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "sales.findAll.success",
                        service.getAll(page, category)));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<SaleDetailDto>> getBySlug(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "sales.findBy.success",
                        service.getBySlug(slug)));
    }
}
