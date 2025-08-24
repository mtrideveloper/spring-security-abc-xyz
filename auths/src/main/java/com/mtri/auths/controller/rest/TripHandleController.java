package com.mtri.auths.controller.rest;

import org.springframework.web.bind.annotation.RestController;

import com.mtri.auths.dto.req.StartTripRequest;
import com.mtri.auths.model.Trip;
import com.mtri.auths.service.trip.TripService;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/trip")
public class TripHandleController {
    private TripService tripService;

    public TripHandleController(TripService tripService) {
        this.tripService = tripService;
    }

    // ====== 1. Bắt đầu trip ======
    @PostMapping("/start")
    public Trip startTrip(
            @RequestBody StartTripRequest startTripRequest) {
        return tripService.startTrackingService(startTripRequest.tripId, startTripRequest.enable);
    }

    /**
     * body gửi về cho server phải là:
     * {
     * "type": "Point",
     * "coordinates": [106.660172, 10.762622]
     * }
     * Để GeoJsonPoint mongodb phân tích.
     */
    @PostMapping("/track/{tripId}")
    public Trip trackLocation(
            @PathVariable String tripId,
            @RequestBody GeoJsonPoint point) {
        return tripService.trackLocationService(tripId, point);
    }

    // ====== 3. Dừng trip ======
    @PostMapping("/stop/{tripId}")
    public Trip stopTrip(
            @PathVariable String tripId) {
        return tripService.stopTripService(tripId);
    }
}
