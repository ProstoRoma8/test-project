package main;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppContextTest {

    @Test
    void testAirlineNotNull() {
        assertNotNull(AppContext.airline);
    }

    @Test
    void testAirlineName() {
        assertEquals("My Airline", AppContext.airline.getName());
    }

    @Test
    void testLoggerNotNull() {
        assertNotNull(AppContext.logger);
    }

    @Test
    void testLoggerName() {
        assertEquals("AirLineApp", AppContext.logger.getName());
    }

    @Test
    void testAirlineFleetNotNull() {
        assertNotNull(AppContext.airline.getFleet());
    }
}
