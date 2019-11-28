package wsg.lol.dao.mybatis.mapper.summoner;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.additional.insert.InsertListMapper;
import tk.mybatis.mapper.common.base.update.UpdateByPrimaryKeySelectiveMapper;
import wsg.lol.common.pojo.domain.summoner.LeagueEntryDo;

/**
 * Mapper for entries of the league.
 *
 * @author Kingen
 */
@Mapper
@Repository
public interface LeagueEntryMapper extends InsertListMapper<LeagueEntryDo>, UpdateByPrimaryKeySelectiveMapper<LeagueEntryDo> {
}