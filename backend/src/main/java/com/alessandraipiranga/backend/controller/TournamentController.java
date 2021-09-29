package com.alessandraipiranga.backend.controller;

import com.alessandraipiranga.backend.api.Team;
import com.alessandraipiranga.backend.api.Tournament;
import com.alessandraipiranga.backend.model.GroupEntity;
import com.alessandraipiranga.backend.model.TournamentEntity;
import com.alessandraipiranga.backend.service.TournamentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Optional;
import java.util.Set;

import static javax.servlet.http.HttpServletResponse.SC_CREATED;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Tag(name = TournamentController.TOURNAMENT_CONTROLLER_TAG,
        description = "Provides CRUD operations for a Entity")
@Api(tags = TournamentController.TOURNAMENT_CONTROLLER_TAG)
@CrossOrigin
@RestController
public class TournamentController {

    public static final String TOURNAMENT_CONTROLLER_TAG = "Tournament";
    private final TournamentService tournamentService;

    @Autowired
    public TournamentController(TournamentService tournamentService) {
        this.tournamentService = tournamentService;
    }

    @GetMapping(
            value = "/tournament/{id}",
            produces = APPLICATION_JSON_VALUE
    )
    @ApiResponses(value = {
            @ApiResponse(code = SC_OK, message = "Tournament found"),
            @ApiResponse(code = SC_NOT_FOUND, message = "Tournament not found")

    })
    public ResponseEntity<Tournament> findTournament(@PathVariable String id) {
        Optional<TournamentEntity> tournamentEntity = tournamentService.find(id);
        if (tournamentEntity.isPresent()) {
            Tournament findTournament = map(tournamentEntity.get());
            return ok(findTournament);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping(
            value = "/tournament",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(code = HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = SC_CREATED, message = "Tournament created")
    })
    public ResponseEntity<Tournament> createTournament(@RequestBody Tournament tournament) {
        int rounds = tournament.getRounds();
        int groups = tournament.getGroups();
        TournamentEntity createdTournamentEntity = tournamentService.createTournament(rounds, groups);

        Tournament createdTournament = map(createdTournamentEntity);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdTournament.getId())
                .toUri();
        return ResponseEntity.created(location).body(createdTournament);
    }

    private Tournament map(TournamentEntity tournamentEntity) {
        Tournament tournament = new Tournament();
        tournament.setId(tournamentEntity.getTournamentId());
        tournament.setRounds(tournamentEntity.getRounds());
        tournament.setStatus(tournamentEntity.getStatus());

        Set<GroupEntity> groups = tournamentEntity.getGroups();
        groups.stream()
                .map(this::map)
                .forEach(tournament::addTeam);
        return tournament;
    }

    private Team map(GroupEntity groupEntity) {
        Team team = new Team();
        team.setName(groupEntity.getName());
        team.setId(groupEntity.getId());
        return team;
    }
}
