package wsg.lol.dao.mybatis.mapper.rune;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import wsg.lol.common.pojo.dto.rune.RuneTreeDto;
import wsg.lol.dao.mybatis.config.StaticStrategy;

/**
 * Mapper interface for information of rune trees.
 *
 * @author Kingen
 */
@Repository
@Mapper
public interface RuneTreeMapper extends StaticStrategy<RuneTreeDto> {
}