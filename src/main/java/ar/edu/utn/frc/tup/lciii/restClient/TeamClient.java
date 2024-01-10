package ar.edu.utn.frc.tup.lciii.restClient;

import ar.edu.utn.frc.tup.lciii.models.Match;
import ar.edu.utn.frc.tup.lciii.models.Team;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TeamClient {
    private RestTemplate restTemplate = new RestTemplate();
    private String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/teams";

    @CircuitBreaker(name = "microCircuitBreaker", fallbackMethod = "fallback")
    public ResponseEntity<Team[]> getPosts() {
        return restTemplate.getForEntity(url, Team[].class);
    }
    @CircuitBreaker(name = "microCircuitBreaker", fallbackMethod = "fallback2")
    public ResponseEntity<Team> getPostsById(Long id) {
        return restTemplate.getForEntity(url + "/" + id, Team.class);
    }
    public ResponseEntity<Team[]> fallback(Exception e){
        return ResponseEntity.status(500).body(null);
    }
    public ResponseEntity<Team> fallback2(Exception e){
        return ResponseEntity.status(500).body(null);
    }
}
