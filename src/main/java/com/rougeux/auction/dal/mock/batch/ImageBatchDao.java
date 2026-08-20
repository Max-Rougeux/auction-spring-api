package com.rougeux.auction.dal.mock.batch;

import com.rougeux.auction.domain.bo.Image;

import java.util.Map;
import java.util.Set;

public interface ImageBatchDao {
    Map<String, Image> findAllByIds(Set<String> ids);
}