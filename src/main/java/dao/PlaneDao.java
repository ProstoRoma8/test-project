package dao;

import db.DatabaseConnector;
import models.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlaneDao {

    public List<Plane> getAllPlanes() {
        List<Plane> planes = new ArrayList<>();
        String sql = "SELECT * FROM planes";

        try (Connection conn = DatabaseConnector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String type = rs.getString("type");
                String model = rs.getString("model");
                int fuel = rs.getInt("fuel_consumption");
                int range = rs.getInt("flight_range_km");
                double speed = rs.getDouble("cruise_speed_kmh");
                double capacity = rs.getDouble("capacity");

                if ("PASSENGER".equals(type)) {
                    boolean biz = rs.getBoolean("has_business_class");
                    int bizSeats = rs.getInt("business_seats");
                    planes.add(new PassengerPlane(id, model, fuel, range, speed, capacity, biz, bizSeats));
                } else if ("CARGO".equals(type)) {
                    planes.add(new CargoPlane(id, model, fuel, range, speed, capacity));
                } else if ("HELICOPTER".equals(type)) {
                    int altitude = rs.getInt("max_altitude_m");
                    boolean hoist = rs.getBoolean("has_hoist");
                    planes.add(new Helicopter(id, model, fuel, range, speed, (int) capacity, altitude, hoist));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return planes;
    }

    public void savePlane(Plane p) {
        String sql = "INSERT INTO planes (model, fuel_consumption, flight_range_km, cruise_speed_kmh, " +
                "capacity, type, max_altitude_m, has_hoist, has_business_class, business_seats) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getModel());
            pstmt.setInt(2, p.getFuelConsumption());
            pstmt.setInt(3, p.getFlightRangeKm());
            pstmt.setDouble(4, p.getCruiseSpeedKmh());
            pstmt.setDouble(5, p.getCapacity());

            if (p instanceof PassengerPlane pp) {
                pstmt.setString(6, "PASSENGER");
                pstmt.setNull(7, Types.INTEGER); // max_altitude_m
                pstmt.setNull(8, Types.BIT);     // has_hoist
                pstmt.setBoolean(9, pp.isHasBusinessClass());
                pstmt.setInt(10, pp.getBusinessSeats());
            } else if (p instanceof CargoPlane) {
                pstmt.setString(6, "CARGO");
                pstmt.setNull(7, java.sql.Types.INTEGER); // max_altitude_m
                pstmt.setNull(8, java.sql.Types.BIT);     // has_hoist
                pstmt.setNull(9, java.sql.Types.BIT);     // has_business_class
                pstmt.setNull(10, java.sql.Types.INTEGER); // business_seats
            } else if (p instanceof Helicopter h) {
                pstmt.setString(6, "HELICOPTER");
                pstmt.setInt(7, h.getMaxAltitudeM());
                pstmt.setBoolean(8, h.isHasHoist());
                pstmt.setNull(9, Types.BIT);
                pstmt.setNull(10, Types.INTEGER);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updatePlane(Plane p) {
        String sql = "UPDATE planes SET model=?, fuel_consumption=?, flight_range_km=?, " +
                "cruise_speed_kmh=?, capacity=?, has_business_class=?, " +
                "business_seats=?, max_altitude_m=?, has_hoist=? WHERE id=?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, p.getModel());
            pstmt.setInt(2, p.getFuelConsumption());
            pstmt.setInt(3, p.getFlightRangeKm());
            pstmt.setDouble(4, p.getCruiseSpeedKmh());
            pstmt.setDouble(5, p.getCapacity());

            if (p instanceof PassengerPlane pp) {
                pstmt.setBoolean(6, pp.isHasBusinessClass());
                pstmt.setInt(7, pp.getBusinessSeats());
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setNull(9, Types.BIT);
            } else if (p instanceof CargoPlane) {
                pstmt.setNull(6, Types.BIT);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setNull(8, Types.INTEGER);
                pstmt.setNull(9, Types.BIT);
            } else if (p instanceof Helicopter h) {
                pstmt.setNull(6, Types.BIT);
                pstmt.setNull(7, Types.INTEGER);
                pstmt.setInt(8, h.getMaxAltitudeM());
                pstmt.setBoolean(9, h.isHasHoist());
            }

            pstmt.setInt(10, p.getId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePlane(Object id) {
        String sql = "DELETE FROM planes WHERE id = ?";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, (Integer) id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}