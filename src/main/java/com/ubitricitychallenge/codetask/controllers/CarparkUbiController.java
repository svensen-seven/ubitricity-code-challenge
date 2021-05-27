package com.ubitricitychallenge.codetask.controllers;

import com.ubitricitychallenge.codetask.domain.MissingChargingPointException;
import com.ubitricitychallenge.codetask.domain.OperationForbiddenException;
import com.ubitricitychallenge.codetask.services.CarparkUbiService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

@RestController
@RequestMapping("/api/v1/carparkubi")
public final class CarparkUbiController {

    private final CarparkUbiService carparkUbiService;

    private final Logger logger = LoggerFactory.getLogger(CarparkUbiController.class);

    public CarparkUbiController(CarparkUbiService carparkUbiService) {
        this.carparkUbiService = Objects.requireNonNull(carparkUbiService);
    }

    @PostMapping("connect")
    @ApiOperation(
            value = "Connect charging point",
            notes = "Connect charging point. Connecting already connected charging point is forbidden"
            )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 403, message = "Charging point is already connected"),
            @ApiResponse(code = 404, message = "Charging point id is out of range")
            })
    public ResponseEntity<Object> connectChargingPoint(
            @ApiParam(
                    value = "ID value for the charging point you need to connect",
                    required = true, allowableValues = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9"
                    )
            @RequestParam("charging_point_id") int chargingPointId) {
        logger.info("connectChargingPoint is called with chargingPointId = {}", chargingPointId);
        try {
            carparkUbiService.connectChargingPoint(chargingPointId);
        } catch (MissingChargingPointException e) {
            logger.error("Could not connect charging point", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find charging point", e);
        } catch (OperationForbiddenException e) {
            logger.error("Could not connect charging point", e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Charging point could not be connected", e);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("disconnect")
    @ApiOperation(
            value = "Disconnect charging point",
            notes = "Disconnect charging point. Disconnecting already disconnected charging point is forbidden"
            )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful operation"),
            @ApiResponse(code = 403, message = "Charging point is already disconnected"),
            @ApiResponse(code = 404, message = "Charging point id is out of range")
            })
    public ResponseEntity<Object> disconnectChargingPoint(
            @ApiParam(
                    value = "ID value for the charging point you need to disconnect",
                    required = true, allowableValues = "0, 1, 2, 3, 4, 5, 6, 7, 8, 9")
            @RequestParam("charging_point_id") int chargingPointId) {
        logger.info("disconnectChargingPoint is called with chargingPointId = {}", chargingPointId);
        try {
            carparkUbiService.disconnectChargingPoint(chargingPointId);
        } catch (MissingChargingPointException e) {
            logger.error("Could not disconnect charging point", e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find charging point", e);
        } catch (OperationForbiddenException e) {
            logger.error("Could not disconnect charging point", e);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Charging point could not be disconnected", e);
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "report", produces = "text/plain")
    @ApiOperation(value = "Get status report", notes = "Return a report of current status of all charging points")
    public String getReport() {
        logger.info("getReport is called");
        return carparkUbiService.createReport();
    }

}
