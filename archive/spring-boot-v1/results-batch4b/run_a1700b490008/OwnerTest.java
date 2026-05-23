import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.constraints.Pattern;
import org.springframework.samples.petclinic.model.Person;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Visit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OwnerTest {

    @Mock
    private Pet pet;

    @Mock
    private Visit visit;

    @InjectMocks
    private Owner owner;

    @BeforeEach
    public void setup() {
        owner = new Owner();
    }

    @Test
    public void testGetAddress() {
        String address = "Calle Falsa 123";
        owner.setAddress(address);
        assertEquals(address, owner.getAddress());
    }

    @Test
    public void testGetCity() {
        String city = "Ciudad Falsa";
        owner.setCity(city);
        assertEquals(city, owner.getCity());
    }

    @Test
    public void testGetTelephone() {
        String telephone = "1234567890";
        owner.setTelephone(telephone);
        assertEquals(telephone, owner.getTelephone());
    }

    @Test
    public void testGetPets() {
        List<Pet> pets = new ArrayList<>();
        pets.add(pet);
        owner.getPets().addAll(pets);
        assertEquals(pets, owner.getPets());
    }

    @Test
    public void testAddPet() {
        when(pet.isNew()).thenReturn(true);
        owner.addPet(pet);
        assertEquals(1, owner.getPets().size());
    }

    @Test
    public void testGetPetByName() {
        when(pet.getName()).thenReturn("Fido");
        owner.getPets().add(pet);
        Pet result = owner.getPet("Fido");
        assertEquals(pet, result);
    }

    @Test
    public void testGetPetById() {
        when(pet.getId()).thenReturn(1);
        owner.getPets().add(pet);
        Pet result = owner.getPet(1);
        assertEquals(pet, result);
    }

    @Test
    public void testGetPetByNameIgnoreNew() {
        when(pet.getName()).thenReturn("Fido");
        when(pet.isNew()).thenReturn(true);
        owner.getPets().add(pet);
        Pet result = owner.getPet("Fido", true);
        assertNull(result);
    }

    @Test
    public void testAddVisit() {
        when(pet.getId()).thenReturn(1);
        owner.getPets().add(pet);
        owner.addVisit(1, visit);
        verify(pet, times(1)).addVisit(visit);
    }
}