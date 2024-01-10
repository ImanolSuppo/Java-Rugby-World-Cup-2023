package ar.edu.utn.frc.tup.lciii.restClient;

import ar.edu.utn.frc.tup.lciii.models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TeamClientTest {
    @MockBean
    private RestTemplate restTemplate;
    @SpyBean
    private TeamClient teamClient;
    private String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams";
    @BeforeEach
    void setUp() {
    }

    @Test
    void getPosts() {
        Mockito.when(restTemplate.getForEntity(url, Team[].class)).thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Team[]> response = teamClient.getPosts();
        assertNotNull(response.getBody());
    }

//    @Test
//    public void getPostsById() {
//        Mockito.when(restTemplate.getForEntity(url + "/" + 1, Team.class).getBody()).thenReturn(null);
//        assertNotNull(restTemplate.getForEntity(url + "/" + 1, Team.class).getBody());
//    }
}