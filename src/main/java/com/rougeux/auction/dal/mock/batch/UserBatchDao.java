package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.User;

import java.util.Map;
import java.util.Set;

public interface UserBatchDao {
    Map<String, User> findAllByIds(Set<String> ids);
}