package com.financia.quotes.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financia.common.MemberFavorites;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Mapper
@Repository("memberFavoritesMapper")
public interface MemberFavoritesMapper extends BaseMapper<MemberFavorites> {

    @Select("select * from member_favorites where member_id=#{memberId} and symbol =#{symbol} and type=#{type}")
    MemberFavorites getByMemberIdAndSymbol(@Param("memberId") Long memberId, @Param("symbol") String symbol,@Param("type") String type);

    @Update("update member_favorites set status=#{status} where id=#{id}")
    void updateStatus(@Param("id") Long id, @Param("status") Integer status);

    int insert(MemberFavorites memberFavorites);
}
