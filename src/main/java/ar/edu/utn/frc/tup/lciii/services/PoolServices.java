package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.PoolDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.TeamDTO;
import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.Team;
import ar.edu.utn.frc.tup.lciii.restClient.MatchClient;
import ar.edu.utn.frc.tup.lciii.restClient.TeamClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PoolServices {
    @Autowired
    private MatchClient matchClient;
    @Autowired
    private TeamClient teamClient;

    public ResponseEntity<Match[]> getMatchs(){
        return matchClient.getPosts();
    }

    public List<Match> getMatchsByTeam(Long idTeam){
        List<Match> matchesByTeam = new ArrayList<>();
        ResponseEntity<Match[]> matches = getMatchs();
        if(matches.getStatusCode().is2xxSuccessful() && matches.getBody() != null){
            for (Match match: matches.getBody()) {
                if(match.teams()[0].id().equals(idTeam)){
                    matchesByTeam.add(match);
                }
                else if (match.teams()[1].id().equals(idTeam)){
                    matchesByTeam.add(match);
                }
            }
        }
        return matchesByTeam;
    }

    public ResponseEntity<Team[]> getTeam(){
        return teamClient.getPosts();
    }

    public Team getTeamById(Long idTeam){
        ResponseEntity<Team> team = teamClient.getPostsById(idTeam);
        return team.getBody();
    }

    public List<PoolDTO> getAllPools(){
        List<PoolDTO> poolDTOList = new ArrayList<>();
        poolDTOList.add(getPoolById("A"));
        poolDTOList.add(getPoolById("B"));
        poolDTOList.add(getPoolById("C"));
        poolDTOList.add(getPoolById("D"));
        return poolDTOList;
    }

    public PoolDTO getPoolById(String pool){
        PoolDTO poolDTO = new PoolDTO();
        poolDTO.setTeams(new ArrayList<>());
        poolDTO.setPool_id(pool);
        List<Team> teams = getTeamsByPool(pool);
        int i = 0;
        for (Team team:teams) {
            List<Match> matchesByTeam = getMatchsByTeam(teams.get(i).id());
            TeamDTO newTeam = new TeamDTO();
            newTeam.setTeam_id(team.id());
            newTeam.setTeam_name(team.name());
            newTeam.setCountry(team.country());
            newTeam.setMatches_played((long) matchesByTeam.size());
            newTeam.setWins(calculateWin(team, matchesByTeam));
            newTeam.setLosses(calculateLoss(team, matchesByTeam));
            newTeam.setDraws(calculateDraw(team, matchesByTeam));
            newTeam.setPoints_for(calculatePointFor(team, matchesByTeam));
            newTeam.setPoints_against(calculatePointAgain(team, matchesByTeam));
            newTeam.setPoints_differential(calculatePointDiferrential(newTeam.getPoints_for(), newTeam.getPoints_against()));
            newTeam.setTries_made(calculateTries(team, matchesByTeam));
            newTeam.setBonus_points(calculateBonusPoints(team, matchesByTeam));
            newTeam.setPoints(calculatePoints(team, matchesByTeam, newTeam.getBonus_points()));
            newTeam.setTotal_yellow_cards(calculateYellowCards(team, matchesByTeam));
            newTeam.setTotal_red_cards(calculateRedCards(team, matchesByTeam));
            poolDTO.getTeams().add(newTeam);
            i ++;
        }
        return poolDTO;
    }

    public Long calculateYellowCards(Team team, List<Match> matchesByTeam) {
        Long yellowCards = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                yellowCards += match.teams()[0].yellow_cards();
            }
            else{
                yellowCards += match.teams()[1].yellow_cards();
            }
        }
        return yellowCards;
    }

    public Long calculateRedCards(Team team, List<Match> matchesByTeam) {
        Long redCards = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                redCards += match.teams()[0].red_cards();
            }
            else{
                redCards += match.teams()[1].red_cards();
            }
        }
        return redCards;
    }

    public Long calculatePoints(Team team, List<Match> matchesByTeam, Long bonus) {
        Long points = bonus != null ? bonus : 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].points().equals(match.teams()[1].points()))
                points += 2;
            else if(match.teams()[0].id().equals(team.id())){
                if(match.teams()[0].points() > match.teams()[1].points())
                    points += 4;
            }
            else{
                if(match.teams()[1].points() > match.teams()[0].points())
                    points += 4;
            }
        }
        return points;
    }

    public Long calculateBonusPoints(Team team, List<Match> matchesByTeam) {
        Long bonus = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                if(match.teams()[0].tries() >= 4){
                    bonus ++;
                }
                if(match.teams()[0].points() < match.teams()[1].points()){
                    if(match.teams()[1].points()-match.teams()[0].points() <= 7){
                        bonus ++;
                    }
                }
            }
            else{
                if(match.teams()[1].tries() >= 4){
                    bonus ++;
                }
                if(match.teams()[1].points() < match.teams()[0].points()){
                    if(match.teams()[0].points()-match.teams()[1].points() <= 7){
                        bonus ++;
                    }
                }
            }
        }
        return bonus;
    }

    public Long calculateTries(Team team, List<Match> matchesByTeam) {
        Long tries = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                tries += match.teams()[0].tries();
            }
            else{
                tries += match.teams()[1].tries();
            }
        }
        return tries;
    }

    public Long calculatePointDiferrential(Long pointsFor, Long pointsAgainst) {
        return pointsFor - pointsAgainst;
    }

    public Long calculatePointAgain(Team team, List<Match> matchesByTeam) {
        Long pointsAgain = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                pointsAgain += match.teams()[1].points();
            }
            else{
                pointsAgain += match.teams()[0].points();
            }
        }
        return pointsAgain;
    }

    public Long calculatePointFor(Team team, List<Match> matchesByTeam) {
        Long pointsFor = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].id().equals(team.id())){
                pointsFor += match.teams()[0].points();
            }
            else{
                pointsFor += match.teams()[1].points();
            }
        }
        return pointsFor;
    }

    public Long calculateDraw(Team team, List<Match> matchesByTeam) {
        Long draw = 0L;
        for (Match match: matchesByTeam) {
            if(match.teams()[0].points().equals(match.teams()[1].points())) {
                draw ++;
            }
        }
        return draw;
    }

    public Long calculateWin(Team team, List<Match> matchesByTeam) {
        Long wins = 0L;
        for (Match match: matchesByTeam) {
            if (match.teams()[0].id().equals(team.id())){
                if(match.teams()[0].points() > match.teams()[1].points()) {
                    wins ++;
                }
            }
            else if (match.teams()[0].points() < match.teams()[1].points()){
                wins ++;
            }
        }
        return wins;
    }

    public Long calculateLoss(Team team, List<Match> matchesByTeam) {
        Long losses = 0L;
        for (Match match: matchesByTeam) {
            if (match.teams()[0].id().equals(team.id())){
                if(match.teams()[0].points() < match.teams()[1].points()) {
                    losses ++;
                }
            }
            else{
                if (match.teams()[1].points() < match.teams()[0].points()){
                    losses ++;
                }
            }
        }
        return losses;
    }

    public List<Team> getTeamsByPool(String pool) {
        List<Team> teams = new ArrayList<>();
        ResponseEntity<Team[]> allTeams = teamClient.getPosts();
        if(allTeams.getStatusCode().is2xxSuccessful() && allTeams.getBody() != null){
            for (Team t: allTeams.getBody()) {
                if(t.pool().equals(pool)){
                    teams.add(t);
                }
            }
        }
        return teams;
    }


}
