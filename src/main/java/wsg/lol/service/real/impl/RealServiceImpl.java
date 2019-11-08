package wsg.lol.service.real.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import wsg.lol.common.enums.rank.DivisionEnum;
import wsg.lol.common.enums.rank.PositionEnum;
import wsg.lol.common.enums.rank.RankQueueEnum;
import wsg.lol.common.enums.rank.TierEnum;
import wsg.lol.common.pojo.base.AppException;
import wsg.lol.common.pojo.base.BaseResult;
import wsg.lol.common.pojo.base.Page;
import wsg.lol.common.pojo.dmo.champion.ChampionMasteryDmo;
import wsg.lol.common.pojo.dmo.league.LeaguePositionDmo;
import wsg.lol.common.pojo.dmo.match.MatchReferenceDmo;
import wsg.lol.common.pojo.dmo.summoner.SummonerDmo;
import wsg.lol.common.pojo.dto.league.LeagueEntryDto;
import wsg.lol.common.pojo.dto.league.LeagueItemDto;
import wsg.lol.common.pojo.dto.league.LeagueListDto;
import wsg.lol.common.pojo.dto.match.*;
import wsg.lol.common.pojo.dto.summoner.ChampionMasteryDto;
import wsg.lol.common.pojo.dto.summoner.SummonerDto;
import wsg.lol.common.util.ResultUtils;
import wsg.lol.dao.api.impl.ChampionMasteryV4;
import wsg.lol.dao.api.impl.LeagueV4;
import wsg.lol.dao.api.impl.MatchV4;
import wsg.lol.dao.api.impl.SummonerV4;
import wsg.lol.dao.mongo.intf.MongoDao;
import wsg.lol.dao.mybatis.mapper.LeaguePositionMapper;
import wsg.lol.dao.mybatis.mapper.MasteryMapper;
import wsg.lol.dao.mybatis.mapper.MatchReferenceMapper;
import wsg.lol.dao.mybatis.mapper.SummonerMapper;
import wsg.lol.service.real.intf.RealService;

import java.util.*;

/**
 * wsg
 *
 * @author EastSunrise
 */
@Service("realAction")
public class RealServiceImpl implements RealService {

    private static Logger logger = LoggerFactory.getLogger(RealService.class);

    private SummonerMapper summonerMapper;

    private LeaguePositionMapper leaguePositionMapper;

    private MatchReferenceMapper referenceMapper;

    private MasteryMapper masteryMapper;

    private ChampionMasteryV4 championMasteryV4;

    private MatchV4 matchV4;

    private LeagueV4 leagueV4;

    private SummonerV4 summonerV4;

    private MongoDao mongoDao;

    @Override
    public BaseResult buildBaseLib() {
        return buildApexSummonerLib();
    }

    @Override
    public BaseResult buildApexSummonerLib() {
        Set<String> summonerIdSet = new HashSet<>();

        // get apex summoners
        for (TierEnum tier : TierEnum.apexValues()) {
            for (RankQueueEnum queue : RankQueueEnum.values()) {
                LeagueListDto leagueListDto = leagueV4.getApexLeagueByQueue(queue, tier);
                for (LeagueItemDto leagueItemDto : leagueListDto.getEntries()) {
                    summonerIdSet.add(leagueItemDto.getSummonerId());
                }
            }
        }

        return saveSummoners(summonerIdSet);
    }

    @Override
    public BaseResult buildPositionalSummonerLib(TierEnum tier, DivisionEnum division, PositionEnum position) {
        Set<String> summonerIdSet = new HashSet<>();
        for (RankQueueEnum queue : RankQueueEnum.positionalValues()) {
            for (int i = 0; ; i++) {
                List<LeagueEntryDto> leagueEntryDtoList = leagueV4.getAllPositionLeagues(queue, tier, division,
                        position, i);
                if (leagueEntryDtoList == null || leagueEntryDtoList.isEmpty())
                    break;
                for (LeagueEntryDto leagueEntryDto : leagueEntryDtoList) {
                    summonerIdSet.add(leagueEntryDto.getSummonerId());
                }
            }
        }

        return saveSummoners(summonerIdSet);
    }

    @Override
    public BaseResult updateSummoners() {
        // Get last unchecked summoners.
        logger.info("Get last unchecked summoners");
        Example example = new Example(SummonerDmo.class);
        example.setOrderByClause("LAST_CHECKED_TIME ASC");
        List<SummonerDmo> summonerBaseList = summonerMapper.selectByExampleAndRowBounds(example,
                new Page().getRowBounds());
        for (SummonerDmo summonerDmo : summonerBaseList) {
            updateSummonerById(summonerDmo.getId());
        }

        return BaseResult.success();
    }

    @Override
    @Transactional
    public BaseResult updateSummonerById(String summonerId) {
        logger.info("Start to update " + summonerId);
        // update base info.
        SummonerDmo summonerDmo = summonerMapper.selectByPrimaryKey(summonerId);
        if (summonerDmo == null) {
            SummonerDto summonerDto = summonerV4.getSummoner(SummonerV4.CondKeyEnum.ID, summonerId);
            if (1 != summonerMapper.insertSummoner(summonerDto)) {
                throw new AppException("Fail to insert summoner.");
            }
            summonerDmo = summonerMapper.selectByPrimaryKey(summonerId);
        }

        // update the league.
        List<LeagueEntryDto> positionDtoList = leagueV4.getLeaguePositionsBySummonerId(summonerId);
        for (LeagueEntryDto positionDto : positionDtoList) {
            LeaguePositionDmo leaguePositionDmo = leaguePositionMapper.selectByUnionKey(positionDto.getSummonerId(),
                    positionDto.getQueueType(), positionDto.getPosition());
            if (leaguePositionDmo == null) {
                if (1 != leaguePositionMapper.insertPosition(positionDto)) {
                    throw new AppException("Fail to insert position.");
                }
            } else {
                if (1 != leaguePositionMapper.updateByUnionKey(positionDto)) {
                    throw new AppException("Fail to update position.");
                }
            }
        }

        // update the mastery.
        List<ChampionMasteryDto> masteryDtoList = championMasteryV4.getChampionMasteryBySummonerId(summonerId);
        for (ChampionMasteryDto masteryDto : masteryDtoList) {
            ChampionMasteryDmo masteryDmo = masteryMapper.selectByUnionKey(masteryDto.getSummonerId(),
                    masteryDto.getChampionId());
            if (masteryDmo == null) {
                if (1 != masteryMapper.insertMastery(masteryDto)) {
                    throw new AppException("Fail to insert champion mastery.");
                }
            } else {
                if (1 != masteryMapper.updateByUnionKey(masteryDto)) {
                    throw new AppException("Fail to update champion mastery.");
                }
            }
        }

        Date lastCheckedTime = summonerDmo.getLastCheckedTime();
        QueryMatchListDto queryMatchListDto = new QueryMatchListDto();
        queryMatchListDto.setBeginTime(lastCheckedTime.getTime());
        for (long beginIndex = 0; ; beginIndex += QueryMatchListDto.MAX_INDEX_RANGE) {
            queryMatchListDto.setBeginIndex(beginIndex);
            lastCheckedTime = new Date();
            MatchListDto matchListDto = matchV4.getMatchListByAccount(summonerDmo.getAccountId(), queryMatchListDto);
            if (matchListDto == null) {
                break;
            }
            List<MatchReferenceDto> referenceDtoList = matchListDto.getMatches();
            if (referenceDtoList.isEmpty()) {
                break;
            }
            for (MatchReferenceDto referenceDto : referenceDtoList) {
                referenceDto.setSummonerId(summonerId);
                try {
                    if (1 != referenceMapper.insertReference(referenceDto)) {
                        throw new AppException("Fail to insert match reference.");
                    }
                } catch (DuplicateKeyException e) {
                    e.printStackTrace();
                }
            }
        }

        // update the last check time.
        SummonerDmo updateDmo = new SummonerDmo();
        updateDmo.setId(summonerDmo.getId());
        updateDmo.setLastCheckedTime(lastCheckedTime);
        if (1 != summonerMapper.updateByPrimaryKeySelective(updateDmo)) {
            throw new AppException("Fail to update the last check time of the summoner.");
        }

        return BaseResult.success();
    }

    @Override
    public BaseResult extendLib() {
        logger.info("Get last unchecked matches.");
        Example example = new Example(MatchReferenceDmo.class);
        example.setOrderByClause("CHECKED ASC");
        List<MatchReferenceDmo> referenceDmoList = referenceMapper.selectByExampleAndRowBounds(example,
                new Page().getRowBounds());

        for (MatchReferenceDmo referenceDmo : referenceDmoList) {
            updateMatchReference(referenceDmo.getId());
        }
        return BaseResult.success();
    }

    @Override
    @Transactional
    public BaseResult updateMatchReference(Integer id) {
        MatchReferenceDmo referenceDmo = referenceMapper.selectByPrimaryKey(id);
        if (referenceDmo == null || referenceDmo.getChecked()) {
            return BaseResult.success();
        }

        MatchDto matchDto = mongoDao.getCollectionById(referenceDmo.getGameId(), MatchDto.class);
        if (matchDto == null) {
            matchDto = matchV4.getMatchById(referenceDmo.getGameId());
            List<ParticipantIdentityDto> identityDtoList = matchDto.getParticipantIdentities();
            Set<String> idSet = new HashSet<>();
            for (ParticipantIdentityDto identityDto : identityDtoList) {
                idSet.add(identityDto.getPlayer().getSummonerId());
            }
            BaseResult baseResult = saveSummoners(idSet);
            if (baseResult.isSuccess()) {
                mongoDao.insertDocument(matchDto);
            }
        }

        MatchReferenceDmo updateDmo = new MatchReferenceDmo();
        updateDmo.setId(referenceDmo.getId());
        updateDmo.setChecked(false);
        if (1 != referenceMapper.updateByPrimaryKeySelective(updateDmo)) {
            throw new AppException("Fail to update the checked status of the match reference.");
        }
        return BaseResult.success();
    }

    private BaseResult saveSummoners(Set<String> summonerIdSet) {
        if (summonerIdSet.isEmpty()) {
            return BaseResult.success();
        }
        List<String> idUncheckedList = summonerMapper.removeSummonersExist(new ArrayList<>(summonerIdSet));

        logger.info("Save summoners: " + idUncheckedList.size());
        Set<String> errorSet = new HashSet<>();
        for (String id : idUncheckedList) {
            SummonerDto summonerDto = summonerV4.getSummoner(SummonerV4.CondKeyEnum.ID, id);
            if (1 != summonerMapper.insertSummoner(summonerDto)) {
                logger.error("Fail to insert summoner.");
                errorSet.add(id);
            }
        }

        if (!errorSet.isEmpty()) {
            return ResultUtils.fail("The summoners weren't all inserted successfully.");
        }
        return BaseResult.success();
    }

    @Autowired
    public void setLeaguePositionMapper(LeaguePositionMapper leaguePositionMapper) {
        this.leaguePositionMapper = leaguePositionMapper;
    }

    @Autowired
    public void setSummonerMapper(SummonerMapper summonerMapper) {
        this.summonerMapper = summonerMapper;
    }

    @Autowired
    public void setReferenceMapper(MatchReferenceMapper referenceMapper) {
        this.referenceMapper = referenceMapper;
    }

    @Autowired
    public void setMatchV4(MatchV4 matchV4) {
        this.matchV4 = matchV4;
    }

    @Autowired
    public void setLeagueV4(LeagueV4 leagueV4) {
        this.leagueV4 = leagueV4;
    }

    @Autowired
    public void setSummonerV4(SummonerV4 summonerV4) {
        this.summonerV4 = summonerV4;
    }

    @Autowired
    public void setChampionMasteryV4(ChampionMasteryV4 championMasteryV4) {
        this.championMasteryV4 = championMasteryV4;
    }

    @Autowired
    public void setMasteryMapper(MasteryMapper masteryMapper) {
        this.masteryMapper = masteryMapper;
    }

    @Autowired
    public void setMongoDao(MongoDao mongoDao) {
        this.mongoDao = mongoDao;
    }
}
