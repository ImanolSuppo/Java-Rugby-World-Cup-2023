package ar.edu.utn.frc.tup.lciii.restClient;

import ar.edu.utn.frc.tup.lciii.models.Match;
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
class MatchClientTest {

    @MockBean
    private RestTemplate restTemplate;
    @SpyBean
    private MatchClient matchClient;
    private String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/matches";
    @BeforeEach
    void setUp() {
    }

    @Test
    void getPosts() {
        Mockito.when(restTemplate.getForEntity(url, Match[].class)).thenReturn(ResponseEntity.ok(null));
        ResponseEntity<Match[]> response = matchClient.getPosts();
        assertNotNull(response.getBody());
    }
}