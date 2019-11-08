package wsg.lol.dao.mybatis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import wsg.lol.common.pojo.dmo.league.LeagueItemDmo;
import wsg.lol.dao.mybatis.common.BaseMapper;

@Repository
@Mapper
public interface LeagueItemMapper extends BaseMapper<LeagueItemDmo> {

}