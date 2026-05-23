import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class PetTest {

    @Mock
    private PetType petType;

    @Mock
    private Visit visit;

    @InjectMocks
    private Pet pet;

    @BeforeEach
    void setup() {
        pet = new Pet();
    }

    @Test
    void testSetBirthDate() {
        LocalDate birthDate = LocalDate.now();
        pet.setBirthDate(birthDate);
        assertEquals(birthDate, pet.getBirthDate());
    }

    @Test
    void testGetType() {
        pet.setType(petType);
        assertEquals(petType, pet.getType());
    }

    @Test
    void testGetVisits() {
        Set<Visit> visits = new LinkedHashSet<>();
        visits.add(visit);
        pet.addVisit(visit);
        Collection<Visit> result = pet.getVisits();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testAddVisit() {
        pet.addVisit(visit);
        verify(visit, org.mockito.times(0)).toString(); // No hay interacción con visit
        assertEquals(1, pet.getVisits().size());
    }
}