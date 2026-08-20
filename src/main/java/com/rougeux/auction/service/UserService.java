package com.rougeux.auction.service;

import com.rougeux.auction.dal.UserDao;
import com.rougeux.auction.exception.NotFoundException;
import com.rougeux.auction.mapper.UserMapper;
import com.rougeux.auction.web.api.Meta;
import com.rougeux.auction.web.api.Slice;
import com.rougeux.auction.web.user.UserDto;
import com.rougeux.auction.web.user.UserSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.rougeux.auction.support.ApiCodes.CD_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao repository;
    public static final int DEFAULT_LIMIT = 10;

    @Cacheable(value = "users", key = "#page")
    public Slice<List<UserDto>> getAll(int page) {
        List<UserDto> data = repository.findAllProjected(page, DEFAULT_LIMIT).stream()
                .map(UserMapper::toDto)
                .toList();

        long total = repository.count();

        return Slice.<List<UserDto>>builder()
                .data(data)
                .meta(Meta.builder()
                        .page(page)
                        .size(DEFAULT_LIMIT)
                        .total(total)
                        .pages((int) Math.ceil((double) total / DEFAULT_LIMIT))
                        .build())
                .build();
    }

    @Cacheable(value = "userProjection", key = "#slug")
    public UserSummaryDto getBySlug(String slug) {
        return UserMapper.toSummaryDto(repository.findProjectedBySlug(slug)
                .orElseThrow(() -> new NotFoundException(CD_NOT_FOUND, "users.error.notFound")));
    }
}
