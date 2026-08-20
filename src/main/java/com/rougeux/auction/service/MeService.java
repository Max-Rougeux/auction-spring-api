package com.rougeux.auction.service;

import com.rougeux.auction.mapper.UserMapper;
import com.rougeux.auction.service.cache.MeCache;
import com.rougeux.auction.web.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MeService {

    private final MeCache cache;

    public UserDto getProfile(String subject) {
        return UserMapper.toDto(cache.getProfile(subject));
    }
}
