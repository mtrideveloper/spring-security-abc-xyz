package com.mtri.auths.service.trip;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.mtri.auths.model.Trip;
import com.mtri.auths.repo.TripRepository;

@Service
public class TripService {
    private final TripRepository tripRepository;
    
    private static final double EARTH_RADIUS_KM = 6371.0;

    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    // ====== Bật tracking ======
    public Trip startTrackingService(String tripId, boolean enable) {
        Trip trip = tripRepository.findById(tripId).orElseThrow();
        trip.setTracking(enable);
        return tripRepository.save(trip);
    }

    // ====== Cập nhật tọa độ ======
    public Trip trackLocationService(String tripId, GeoJsonPoint newPoint) {
        Trip trip = tripRepository.findById(tripId).orElseThrow();

        // Nếu tracking đang tắt -> bỏ qua
        if (!trip.isTracking()) return trip;

        List<GeoJsonPoint> points = trip.getPoints();
        if (points == null) {
            points = new ArrayList<>();
            trip.setPoints(points);
        }

        // Nếu có điểm trước đó -> cộng thêm khoảng cách
        if (!points.isEmpty()) {
            GeoJsonPoint last = points.get(points.size() - 1);
            double distanceMeters = haversineMeters(
                    last.getY(), last.getX(),
                    newPoint.getY(), newPoint.getX()
            );
            trip.setDistanceMeters(trip.getDistanceMeters() + distanceMeters);
        }

        // Lưu điểm mới
        points.add(newPoint);
        return tripRepository.save(trip);
    }

    // ====== Tắt tracking ======
    public Trip stopTripService(String tripId) {
        Trip trip = tripRepository.findById(tripId).orElseThrow();

        if (!trip.isTracking())
            return trip;

        trip.setTracking(false);
        trip.setEndTime(LocalDateTime.now());

        // // Nếu muốn dùng Google Directions để lấy quãng đường chính xác hơn
        // if (useGoogleDirections && trip.getPoints().size() >= 2) {
        //     double googleDistance = getDistanceFromGoogle(trip.getPoints());
        //     trip.setDistanceMeters(googleDistance);
        // }

        return tripRepository.save(trip);
    }

    public Trip getTripById(String tripId)
    {
        return tripRepository.findByTripId(tripId);
    }

    public List<Trip> getAllTrips() {
        return tripRepository.findAllByOrderByStartTimeDesc();
    }

    // ====== Tính khoảng cách Haversine ======
    private double haversineMeters(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2)
                + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));

        return EARTH_RADIUS_KM * c * 1000; // đổi km sang mét
    }

    // // ====== Lấy quãng đường từ Google Directions API ======
    // private double getDistanceFromGoogle(List<GeoJsonPoint> points) {
    //     GeoJsonPoint start = points.get(0);
    //     GeoJsonPoint end = points.get(points.size() - 1);

    //     // Google yêu cầu "lat,lon"
    //     String origin = start.getY() + "," + start.getX();
    //     String destination = end.getY() + "," + end.getX();

    //     String url = String.format(
    //             "https://maps.googleapis.com/maps/api/directions/json?origin=%s&destination=%s&key=%s",
    //             origin, destination, googleApiKey);

    //     ResponseEntity<java.util.Map<String, Object>> resp = restTemplate.exchange(
    //             url,
    //             HttpMethod.GET,
    //             null,
    //             new ParameterizedTypeReference<java.util.Map<String, Object>>() {
    //             });

    //     var response = resp.getBody();
    //     if (response == null)
    //         return 0;

    //     Object routesObj = response.get("routes");
    //     if (!(routesObj instanceof List<?> routes) || routes.isEmpty())
    //         return 0;

    //     Object legsObj = ((Map<?, ?>) routes.get(0)).get("legs");
    //     if (!(legsObj instanceof List<?> legs) || legs.isEmpty())
    //         return 0;

    //     Object distanceObj = ((Map<?, ?>) legs.get(0)).get("distance");
    //     if (!(distanceObj instanceof Map<?, ?> distanceMap))
    //         return 0;

    //     Object value = distanceMap.get("value");
    //     return (value instanceof Number num) ? num.doubleValue() : 0;
    // }
}
