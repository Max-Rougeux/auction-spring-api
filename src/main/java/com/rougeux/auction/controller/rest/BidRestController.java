package com.rougeux.auction.controller.rest;

import com.rougeux.auction.service.BidService;
import com.rougeux.auction.support.ApiCodes;
import com.rougeux.auction.web.api.ApiResponse;
import com.rougeux.auction.support.factory.ApiResponseFactory;
import com.rougeux.auction.web.bid.BidChartDto;
import com.rougeux.auction.web.bid.BidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/bids")
public class BidRestController {

    private final BidService service;
    private final ApiResponseFactory factory;

    @GetMapping("/{slug}")
    public ResponseEntity<ApiResponse<List<BidDto>>> getAllBySale(
            @RequestParam(defaultValue = "1") int page,
            @PathVariable String slug) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "bids.findBy.success",
                        service.getAllBySale(page, slug)));
    }

    @GetMapping("/latest")
    public ResponseEntity<ApiResponse<List<BidDto>>> getLatest() {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "bids.latest.success",
                        service.getLatest()));
    }

    @GetMapping("/{slug}/all")
    public ResponseEntity<ApiResponse<List<BidChartDto>>> getChartData(
            @PathVariable String slug) {
        return ResponseEntity.ok(
                factory.success(ApiCodes.CD_DATA_SUCCESS, "bids.findBy.success",
                        service.getChartData(slug)));
    }
}
