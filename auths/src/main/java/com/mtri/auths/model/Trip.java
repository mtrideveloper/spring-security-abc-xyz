package com.mtri.auths.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Bắt đầu trip → set tracking = true, startTime = now, thêm điểm đầu.
 * Nhận tọa độ mới → mỗi lần client gửi tọa độ mới, bạn tính quãng đường từ điểm trước đó → cộng dồn vào distanceMeters → thêm vào points.
 * Dừng trip → set tracking = false, endTime = now.
*/
@Document(collection = "trips")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Trip {
    @Id
    private String tripId;
    private String userId;
    private double distanceMeters;
    // private List<GeoPoint> points;
    private List<GeoJsonPoint> points;
    @CreatedDate
    private LocalDateTime startTime;
    @LastModifiedDate
    private LocalDateTime endTime;
    private boolean tracking; // đang bật hay tắt theo dõi
}