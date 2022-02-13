package com.tms.api.task;

import com.tms.api.dto.TeamConfig;
import com.tms.api.helper.Helper;
import com.tms.api.helper.LogHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class AutoReloadConfigTeam {

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger logger = LoggerFactory.getLogger(AutoReloadConfigTeam.class);

    private static HashMap<Integer, TeamConfig> configTeamJob = null;

    @Value("${config.team-config.is-auto-reload}")
    public boolean isAutoLoadTeamConfig;

    @Scheduled(fixedDelayString = "${config.team-config.time-auto-reload}")
    public void refreshTeamJob(){
        logger.info("*********START SAVING TEAM INFO ON CACHE JOB********");
        if(!isAutoLoadTeamConfig){
            logger.info("*********START SAVING TEAM INFO ON CACHE OFF********");
            return;
        }

        if(configTeamJob == null)
            configTeamJob = new HashMap<>();
        else
            configTeamJob.clear();

        try {
            readConfigTeam();
        } catch (Exception e) {
            logger.error("CAN NOT READ CONFIG TEAM. {}", e.getMessage());
        }
    }

    public List<TeamConfig> saveTeamJob(){
        StringBuilder sql = new StringBuilder();
        sql.append(" select tm.user_id, tm.team_id, t.manager_id")
                .append(" from or_team_member tm")
                .append(" left join or_team t on t.id = tm.team_id");

        Query query = entityManager.createNativeQuery(sql.toString());
        List<TeamConfig> result = new ArrayList<>();
        List<Object[]> rows = query.getResultList();
        for(Object[] row : rows){
            TeamConfig teamConfig = new TeamConfig();
            if (row[0] != null){
                teamConfig.setUserId((Integer) row[0]);
            }
            if (row[1] != null){
                teamConfig.setTeam((Integer) row[1]);
            }
            if (row[2] != null){
                teamConfig.setTeamSupervisor((Integer) row[2]);
            }
            result.add(teamConfig);
        }

        return result;

    }

    private void readConfigTeam() {
        List<TeamConfig> listTeamJob = saveTeamJob();

        logger.info("---------- REFRESH TEAM CONFIG ----------");
        for (TeamConfig teamConfig : listTeamJob) {
            configTeamJob.put(teamConfig.getUserId(), teamConfig);
            logger.info(String.format("Refresh campaign %s config: ", teamConfig.getUserId()) + LogHelper.toJson(teamConfig));
        }
    }

    public Integer getConfigTeam(Integer userId){
        if(userId != null || userId.intValue() == 0){
            if(configTeamJob.containsKey(userId)){
                return configTeamJob.get(userId).getTeam();
            }
        }
        else
            return null;

        return null;
    }

    public Integer getConfigTeamSupervisor(Integer userId){
        if(userId != null || userId.intValue() == 0){
            if(configTeamJob.containsKey(userId)){
                return configTeamJob.get(userId).getTeamSupervisor();
            }
        }
        else
            return null;

        return null;
    }
}
