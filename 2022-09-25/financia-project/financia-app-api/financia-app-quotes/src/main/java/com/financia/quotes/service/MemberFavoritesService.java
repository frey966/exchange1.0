package com.financia.quotes.service;

import com.financia.common.MemberFavorites;

import java.util.List;

public interface MemberFavoritesService {

    List<MemberFavorites> getMemberFavoritesList(MemberFavorites memberFavorites);

    void updateStatus( Long id,  Integer status);

    void insert(MemberFavorites memberFavorites);
}
