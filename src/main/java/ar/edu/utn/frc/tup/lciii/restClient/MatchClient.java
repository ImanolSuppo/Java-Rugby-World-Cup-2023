package ar.edu.utn.frc.tup.lciii.restClient;

import ar.edu.utn.frc.tup.lciii.models.Match;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class MatchClient {
    private RestTemplate restTemplate = new RestTemplate();
    private String url = "https://my-json-server.typicode.com/LCIV-2023/fake-api-rwc2023/matches";

    @CircuitBreaker(name = "microCircuitBreaker", fallbackMethod = "fallback")
    public ResponseEntity<Match[]> getPosts() {
        return restTemplate.getForEntity(url, Match[].class);
    }

    public ResponseEntity<Match[]> fallback(Exception e){
        return ResponseEntity.status(500).body(null);
    }
}
