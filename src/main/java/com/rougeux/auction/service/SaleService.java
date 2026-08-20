package com.rougeux.auction.service;

import com.rougeux.auction.dal.SaleDao;
import com.rougeux.auction.domain.bo.Sale;
import com.rougeux.auction.mapper.SaleMapper;
import com.rougeux.auction.service.cache.SaleCache;
import com.rougeux.auction.web.api.Meta;
import com.rougeux.auction.web.api.Slice;
import com.rougeux.auction.web.sale.SaleCardDto;
import com.rougeux.auction.web.sale.SaleDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SaleService {

    private final SaleCache cache;
    private final SaleDao repository;
    public static final int DEFAULT_LIMIT = 10;

    public Slice<List<SaleCardDto>> getAll(int page, String category) {
        List<SaleCardDto> data = cache.getAll(page, DEFAULT_LIMIT, category).stream()
                .map(SaleMapper::toCardDto)
                .toList();

        long total = repository.count(category);

        return Slice.<List<SaleCardDto>>builder()
                .data(data)
                .meta(Meta.builder()
                        .page(page)
                        .size(DEFAULT_LIMIT)
                        .total(total)
                        .pages((int) Math.ceil((double) total / DEFAULT_LIMIT))
                        .build())
                .build();
    }

    public SaleDetailDto getBySlug(String slug) {
        return SaleMapper.toDetailDto(cache.getProjectionBySlug(slug));
    }

    public void save(Sale sale) {
        cache.persist(sale);
    }
}
