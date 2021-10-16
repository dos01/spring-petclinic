package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {PetController.class})
@ExtendWith(SpringExtension.class)
class PetControllerTest {
	@MockBean
	private OwnerRepository ownerRepository;

	@Autowired
	private PetController petController;

	@MockBean
	private PetRepository petRepository;

	@Test
	void testConstructor() {
		new PetController(mock(PetRepository.class), mock(OwnerRepository.class));
	}

	@Test
	void testPopulatePetTypes() {
		ArrayList<PetType> petTypeList = new ArrayList<PetType>();
		when(this.petRepository.findPetTypes()).thenReturn(petTypeList);
		Collection<PetType> actualPopulatePetTypesResult = this.petController.populatePetTypes();
		assertSame(petTypeList, actualPopulatePetTypesResult);
		assertTrue(actualPopulatePetTypesResult.isEmpty());
		verify(this.petRepository).findPetTypes();
	}

	@Test
	void testFindOwner() {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		assertSame(owner, this.petController.findOwner(123));
		verify(this.ownerRepository).findById((Integer) any());
	}

	@Test
	void testInitCreationForm() throws Exception {
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/{ownerId}/pets/new", 123);
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}

	@Test
	void testInitUpdateForm() throws Exception {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");

		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.ofEpochDay(1L));
		pet.setOwner(owner);
		pet.setVisitsInternal(new ArrayList<Visit>());
		pet.setId(1);
		pet.setName("Bella");
		pet.setType(petType);
		when(this.petRepository.findById((Integer) any())).thenReturn(pet);
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner1 = new Owner();
		owner1.setLastName("Doe");
		owner1.setId(1);
		owner1.setCity("Oxford");
		owner1.setPetsInternal(new HashSet<Pet>());
		owner1.setAddress("42 Main St");
		owner1.setFirstName("Jane");
		owner1.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/{ownerId}/pets/{petId}/edit",
			123, 123);
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}

	@Test
	void testProcessCreationForm() throws Exception {
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 123);
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}

	@Test
	void testProcessCreationForm2() throws Exception {
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 123)
			.param("name", "Bella");
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}

	@Test
	void testProcessCreationForm3() throws Exception {
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");

		PetType petType = new PetType();
		petType.setId(1);
		petType.setName("Dog");

		Pet pet = new Pet();
		pet.setBirthDate(LocalDate.ofEpochDay(1L));
		pet.setOwner(owner);
		pet.setVisitsInternal(new ArrayList<Visit>());
		pet.setId(1);
		pet.setName("Bella");
		pet.setType(petType);

		HashSet<Pet> petSet = new HashSet<Pet>();
		petSet.add(pet);

		Owner owner1 = new Owner();
		owner1.setLastName("Doe");
		owner1.setId(1);
		owner1.setCity("Oxford");
		owner1.setPetsInternal(petSet);
		owner1.setAddress("42 Main St");
		owner1.setFirstName("Jane");
		owner1.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner1);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/pets/new", 123)
			.param("name", "Bella");
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}

	@Test
	void testProcessUpdateForm() throws Exception {
		when(this.petRepository.findPetTypes()).thenReturn(new ArrayList<PetType>());

		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/pets/{petId}/edit",
			123, 123);
		MockMvcBuilders.standaloneSetup(this.petController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(3))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner", "pet", "types"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdatePetForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdatePetForm"));
	}
}

