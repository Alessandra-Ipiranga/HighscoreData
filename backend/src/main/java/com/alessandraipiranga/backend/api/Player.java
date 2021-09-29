package com.alessandraipiranga.backend.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Player {

    private Long id;

    @ApiModelProperty(required = true, example = "Max Muster", notes = "The name of the player")
    private String name;

}
