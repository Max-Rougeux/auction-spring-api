package com.rougeux.auction.dal.jpa.contract;

import com.rougeux.auction.dal.jpa.entity.CategoryEntity;

public interface CategoryCount {
    CategoryEntity getCategory();
    long getCount();
}
