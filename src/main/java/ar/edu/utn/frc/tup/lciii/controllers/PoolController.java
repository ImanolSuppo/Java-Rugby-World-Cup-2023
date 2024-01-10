package ar.edu.utn.frc.tup.lciii.controllers;

import ar.edu.utn.frc.tup.lciii.dtos.common.PoolDTO;
import ar.edu.utn.frc.tup.lciii.services.PoolServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pool")
public class PoolController {

    @Autowired
    private PoolServices poolServices;
    @GetMapping
    public ResponseEntity<List<PoolDTO>> getPools(){
        return ResponseEntity.ok(poolServices.getAllPools());
    }

    @GetMapping("/{pool_id}")
    public ResponseEntity<PoolDTO> getPools(@PathVariable String pool_id){
        return ResponseEntity.ok(poolServices.getPoolById(pool_id));
    }
}
