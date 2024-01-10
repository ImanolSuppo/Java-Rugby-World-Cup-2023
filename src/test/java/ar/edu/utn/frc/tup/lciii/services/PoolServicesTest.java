package ar.edu.utn.frc.tup.lciii.services;

import ar.edu.utn.frc.tup.lciii.dtos.common.PoolDTO;
import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.Team;
import ar.edu.utn.frc.tup.lciii.models.TeamInMatch;
import ar.edu.utn.frc.tup.lciii.restClient.MatchClient;
import ar.edu.utn.frc.tup.lciii.restClient.TeamClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PoolServicesTest {

    @Autowired
    private PoolServices poolServices;
    @Autowired
    private MatchClient matchClient;

    @Autowired
    private TeamClient teamClient;

    List<Team> teamList = List.of(
            new Team(1L, "Les Bleus", "France", 2, "A"),
            new Team(2L, "The All Blacks", "New Zealand", 4, "A"),
            new Team(3L, "Gli Azzurri", "Italy", 11, "A"),
            new Team(4L, "Los Teros", "Uruguay", 17, "A"),
            new Team(5L, "The Welwitschias", "Namibia", 21, "A"),
            new Team(6L, "The Springboks", "South Africa", 3, "B"),
            new Team(7L, "", "Irelan", 1, "B"), // Corregí el nombre de Irlanda
            new Team(8L, "", "Scotland", 5, "B"),
            new Team(9L, "Ikale Tahi", "Tonga", 15, "B"),
            new Team(10L, "The Oaks", "Romania", 19, "B"),
            new Team(11L, "", "Wales", 7, "C"),
            new Team(12L, "The Wallabies", "Australia", 10, "C"),
            new Team(13L, "The Flying Fijians", "Fiji", 8, "C"),
            new Team(14L, "The Lelos", "Georgia", 13, "C"),
            new Team(15L, "Os Lobos", "Portugal", 16, "C"),
            new Team(16L, "", "England", 6, "D"),
            new Team(17L, "The Brave Blossoms", "Japan", 12, "D"),
            new Team(18L, "The Pumas", "Argentina", 9, "D"),
            new Team(19L, "Manu Samoa", "Samoa", 14, "D"),
            new Team(20L, "Los Condores", "Chile", 22, "D")
    );
    List<Match> matchList = List.of(
            new Match(1L, "2023-09-08", new TeamInMatch[]{
                    new TeamInMatch(1L, 27L, 8L, 0L, 0L),
                    new TeamInMatch(2L, 13L, 2L, 1L, 0L)
            }, "1", "A"),
            new Match(2L, "2023-09-08", new TeamInMatch[]{
                    new TeamInMatch(1L, 27L, 2L, 0L, 0L),
                    new TeamInMatch(2L, 13L, 2L, 1L, 0L)
            }, "1", "A"),
            new Match(3L, "2023-09-08", new TeamInMatch[]{
                    new TeamInMatch(1L, 27L, 2L, 0L, 0L),
                    new TeamInMatch(2L, 13L, 2L, 1L, 0L)
            }, "1", "A"),
            new Match(4L, "2023-09-08", new TeamInMatch[]{
                    new TeamInMatch(1L, 27L, 2L, 0L, 0L),
                    new TeamInMatch(2L, 13L, 2L, 1L, 0L)
            }, "1", "A")
    );



    @BeforeEach
    void setUp() {
    }

    @Test
    void getMatchs() {
        assertNotNull(matchClient.getPosts().getBody());
    }

    @Test
    void getMatchsByTeam() {
        Match[] matches = matchClient.getPosts().getBody();
        assert matches != null;
        assertEquals(30, matches.length);
    }

    @Test
    void getTeam() {
        ResponseEntity<Team[]> teamsArray = teamClient.getPosts();
        if(teamsArray.getBody() != null){
            List<Team> response = new ArrayList<>(Arrays.asList(teamsArray.getBody()));
            assertEquals(teamList, response);
        }
    }

    @Test
    void getTeamById() {
        ResponseEntity<Team> team = teamClient.getPostsById(1L);
        assertEquals(teamList.get(0), team.getBody());
    }

    @Test
    void getAllPools() {
        assertNotNull(poolServices.getAllPools());
    }

    @Test
    void getPoolById() {
        assertNotNull(poolServices.getPoolById("A"));
    }

    @Test
    void calculateYellowCards() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateYellowCards(team, matchList);
        assertEquals(0, response);
    }

    @Test
    void calculateRedCards() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateRedCards(team, matchList);
        assertEquals(0, response);
    }

    @Test
    void calculatePoints() {
        Team team = teamList.get(0);
        Long response = poolServices.calculatePoints(team, matchList, 0L);
        assertEquals(16, response);
    }

    @Test
    void calculateBonusPoints() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateBonusPoints(team, matchList);
        assertEquals(1, response);
    }

    @Test
    void calculateTries() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateTries(team, matchList);
        assertEquals(14, response);
    }

    @Test
    void calculatePointDiferrential() {
        Long pointsFor = 80L;
        Long pointsAgain = 40L;
        Long response = poolServices.calculatePointDiferrential(pointsFor, pointsAgain);
        assertEquals(40L, response);
    }

    @Test
    void calculatePointAgain() {
        Team team = teamList.get(0);
        Long response = poolServices.calculatePointAgain(team, matchList);
        assertEquals(52L, response);
    }

    @Test
    void calculatePointFor() {
        Team team = teamList.get(0);
        Long response = poolServices.calculatePointFor(team, matchList);
        assertEquals(108, response);
    }

    @Test
    void calculateDraw() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateDraw(team, matchList);
        assertEquals(0, response);
    }

    @Test
    void calculateWin() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateWin(team, matchList);
        assertEquals(4, response);
    }

    @Test
    void calculateLoss() {
        Team team = teamList.get(0);
        Long response = poolServices.calculateLoss(team, matchList);
        assertEquals(0, response);
    }

    @Test
    void getTeamsByPool() {
        assertEquals(5, poolServices.getTeamsByPool("A").size());
    }
}
