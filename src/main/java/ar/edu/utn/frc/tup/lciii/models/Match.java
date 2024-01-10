package ar.edu.utn.frc.tup.lciii.models;

public record Match(Long id, String date, TeamInMatch[] teams, String stadium, String pool) {
}
