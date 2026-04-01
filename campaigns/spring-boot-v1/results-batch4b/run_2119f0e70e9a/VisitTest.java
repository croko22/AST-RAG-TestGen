import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class VisitTest {

    @InjectMocks
    private Visit visit;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        visit = new Visit();
    }

    @Test
    public void testConstructor() {
        assertNotNull(visit);
        assertNotNull(visit.getDate());
    }

    @Test
    public void testGetDate() {
        LocalDate date = LocalDate.now();
        visit.setDate(date);
        assertEquals(date, visit.getDate());
    }

    @Test
    public void testSetDate() {
        LocalDate date = LocalDate.now();
        visit.setDate(date);
        assertEquals(date, visit.getDate());
    }

    @Test
    public void testGetDescription() {
        String description = "Descripción de la visita";
        visit.setDescription(description);
        assertEquals(description, visit.getDescription());
    }

    @Test
    public void testSetDescription() {
        String description = "Descripción de la visita";
        visit.setDescription(description);
        assertEquals(description, visit.getDescription());
    }
}