package com.rougeux.auction.service;

import com.rougeux.auction.dal.BidDao;
import com.rougeux.auction.domain.bo.Bid;
import com.rougeux.auction.domain.projection.BidProjection;
import com.rougeux.auction.mapper.BidMapper;
import com.rougeux.auction.web.api.Meta;
import com.rougeux.auction.web.api.Slice;
import com.rougeux.auction.web.bid.BidChartDto;
import com.rougeux.auction.web.bid.BidDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BidService {

    private final BidDao repository;
    public static final int DEFAULT_LIMIT = 10;

    public Slice<List<BidDto>> getAllBySale(int page, String slug) {
        List<BidDto> data = repository.findAllProjectedBySale(page, DEFAULT_LIMIT, slug).stream()
                .map(BidMapper::toDto)
                .toList();

        long total = repository.count(slug);

        return Slice.<List<BidDto>>builder()
                .data(data)
                .meta(Meta.builder()
                        .page(page)
                        .size(DEFAULT_LIMIT)
                        .total(total)
                        .pages((int) Math.ceil((double) total / DEFAULT_LIMIT))
                        .build())
                .build();
    }

    public List<BidDto> getLatest() {
        return repository.findLatestProjected(DEFAULT_LIMIT).stream()
                .map(BidMapper::toDto)
                .toList();
    }

    public List<BidChartDto> getChartData(String slug) {
        return repository.findAllPointsBySale(slug).stream()
                .map(BidMapper::toChartDto)
                .toList();
    }

    public BidProjection getFirstBySale(String slug) {
        return repository.findFirstBySale(slug).orElse(null);
    }

    public void save(Bid bid) {
        repository.save(bid);
    }
}
