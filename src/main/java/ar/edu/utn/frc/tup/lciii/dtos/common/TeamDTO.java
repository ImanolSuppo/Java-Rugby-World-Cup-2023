package ar.edu.utn.frc.tup.lciii.dtos.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TeamDTO {

    private Long team_id;
    private String team_name;
    private String country;
    private Long matches_played;
    private Long wins;
    private Long draws;
    private Long losses;
    private Long points_for;
    private Long points_against;
    private Long points_differential;
    private Long tries_made;
    private Long bonus_points;
    private Long points;
    private Long total_yellow_cards;
    private Long total_red_cards;
}
