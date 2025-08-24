package com.mtri.auths.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.mtri.auths.service.trip.TripService;
import com.mtri.auths.util.PathConstants;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping
@RequiredArgsConstructor
public class TripViewController {

    private final TripService tripService;

    // 1. Trang danh sách
    @GetMapping(PathConstants.LIST_TRIP_PATH)
    public String listTrips(Model model) {
        model.addAttribute("trips", tripService.getAllTrips());
        return "trip-list"; // -> trip-list.html
    }

    // 2. Trang chi tiết 1 trip
    @GetMapping("/trip/{tripId}")
    public String tripDetail(@PathVariable String tripId, Model model) {
        var trip = tripService.getTripById(tripId);
        model.addAttribute("tripId", tripId);
        model.addAttribute("trip", trip);
        // model.addAttribute("distanceKM", trip.getDistanceKM()); // giả sử có field này
        return "trip-detail"; // -> trip-detail.html
    }
}
