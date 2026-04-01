import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VetTest {

    @Mock
    private Specialty specialty;

    @Mock
    private Set<Specialty> specialties;

    @InjectMocks
    private Vet vet;

    @BeforeEach
    public void setup() {
        when(specialty.getName()).thenReturn("Especialidad");
        vet = new Vet();
    }

    @Test
    public void testGetSpecialtiesInternal() {
        Set<Specialty> internalSpecialties = vet.getSpecialtiesInternal();
        assertNotNull(internalSpecialties);
    }

    @Test
    public void testGetSpecialties() {
        List<Specialty> specialtiesList = new ArrayList<>();
        specialtiesList.add(specialty);
        when(vet.getSpecialtiesInternal()).thenReturn(specialties);
        when(specialties.stream()).thenReturn(specialtiesList.stream());

        List<Specialty> result = vet.getSpecialties();
        assertEquals(1, result.size());
        assertEquals("Especialidad", result.get(0).getName());
    }

    @Test
    public void testGetNrOfSpecialties() {
        when(vet.getSpecialtiesInternal()).thenReturn(specialties);
        when(specialties.size()).thenReturn(1);

        int result = vet.getNrOfSpecialties();
        assertEquals(1, result);
    }

    @Test
    public void testAddSpecialty() {
        when(vet.getSpecialtiesInternal()).thenReturn(specialties);

        vet.addSpecialty(specialty);
        verify(specialties, times(1)).add(specialty);
    }
}