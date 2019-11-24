package wsg.lol.controller.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import wsg.lol.common.base.GenericResult;
import wsg.lol.common.util.ResultUtils;
import wsg.lol.service.intf.LeagueService;
import wsg.lol.service.system.intf.SystemService;

/**
 * Runner to build the base data. Run only when initializing the database.
 *
 * @author Kingen
 */
@Service
@Order(value = 2)
public class BuildRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(BuildRunner.class);

    private SystemService systemService;

    private LeagueService leagueService;

    @Override
    public void run(ApplicationArguments args) {
        logger.info("Initializing the database.");
        GenericResult<Boolean> result = systemService.isDatabaseInitialized();
        if (result.getObject()) {
            logger.info("The database has been initialized.");
            return;
        }

        ResultUtils.assertSuccess(leagueService.initialByLeagues());
        ResultUtils.assertSuccess(systemService.initialized());

        logger.info("Succeed in initializing the database.");
    }

    @Autowired
    public void setLeagueService(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @Autowired
    public void setSystemService(SystemService systemService) {
        this.systemService = systemService;
    }
}
