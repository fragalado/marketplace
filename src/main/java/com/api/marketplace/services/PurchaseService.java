package com.api.marketplace.services;

import com.api.marketplace.daos.User;
import com.api.marketplace.dtos.PurchaseRequestDTO;

public interface PurchaseService {

    void purchaseCourses(PurchaseRequestDTO request, User user);
}
