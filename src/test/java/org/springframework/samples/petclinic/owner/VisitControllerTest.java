package org.springframework.samples.petclinic.owner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {VisitController.class})
@ExtendWith(SpringExtension.class)
class VisitControllerTest {
	@MockBean
	private PetRepository petRepository;

	@Autowired
	private VisitController visitController;

	@MockBean
	private VisitRepository visitRepository;

	@Test
	void testConstructor() {
		new VisitController(mock(VisitRepository.class), mock(PetRepository.class));
	}

	@Test
	void testLoadPetWithVisit() {
		when(this.visitRepository.findByPetId((Integer) any())).thenReturn(new ArrayList<Visit>());

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
		HashMap<String, Object> stringObjectMap = new HashMap<String, Object>(1);
		assertEquals(1, this.visitController.loadPetWithVisit(123, stringObjectMap).getPetId().intValue());
		verify(this.visitRepository).findByPetId((Integer) any());
		verify(this.petRepository).findById((Integer) any());
		assertEquals(1, stringObjectMap.size());
		assertEquals(1, ((Pet) stringObjectMap.get("pet")).getVisitsInternal().size());
		assertEquals(1, ((Pet) stringObjectMap.get("pet")).getVisits().size());
	}

	@Test
	void testInitNewVisitForm() throws Exception {
		when(this.visitRepository.findByPetId((Integer) any())).thenReturn(new ArrayList<Visit>());

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
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/*/pets/{petId}/visits/new", 123);
		MockMvcBuilders.standaloneSetup(this.visitController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(2))
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet", "visit"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdateVisitForm"));
	}

	@Test
	void testProcessNewVisitForm() throws Exception {
		doNothing().when(this.visitRepository).save((Visit) any());
		when(this.visitRepository.findByPetId((Integer) any())).thenReturn(new ArrayList<Visit>());

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
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", 123)
			.param("description", "The characteristics of someone or something");
		MockMvcBuilders.standaloneSetup(this.visitController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.model().size(2))
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet", "visit"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/owners/*"));
	}

	@Test
	void testProcessNewVisitForm2() throws Exception {
		doNothing().when(this.visitRepository).save((Visit) any());
		when(this.visitRepository.findByPetId((Integer) any())).thenReturn(new ArrayList<Visit>());

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
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/*/pets/{petId}/visits/new", 123)
			.param("description", "");
		MockMvcBuilders.standaloneSetup(this.visitController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(2))
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet", "visit"))
			.andExpect(MockMvcResultMatchers.view().name("pets/createOrUpdateVisitForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("pets/createOrUpdateVisitForm"));
	}
}

