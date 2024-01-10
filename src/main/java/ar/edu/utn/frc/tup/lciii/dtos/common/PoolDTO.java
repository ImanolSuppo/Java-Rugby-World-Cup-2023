package ar.edu.utn.frc.tup.lciii.dtos.common;

import lombok.Data;

import java.util.List;

@Data
public class PoolDTO {
    private String pool_id;
    private List<TeamDTO> teams;
}
