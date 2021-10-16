package org.springframework.samples.petclinic.owner;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.samples.petclinic.visit.Visit;
import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ContextConfiguration(classes = {OwnerController.class})
@ExtendWith(SpringExtension.class)
class OwnerControllerTest {
	@Autowired
	private OwnerController ownerController;

	@MockBean
	private OwnerRepository ownerRepository;

	@MockBean
	private VisitRepository visitRepository;

	@Test
	void testConstructor() {
		new OwnerController(mock(OwnerRepository.class), mock(VisitRepository.class));
	}

	@Test
	void testInitCreationForm() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/new");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testInitCreationForm2() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/new", "Uri Vars");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testInitFindForm() throws Exception {
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/find");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/findOwners"));
	}

	@Test
	void testInitFindForm2() throws Exception {
		MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/owners/find");
		getResult.contentType("Not all who wander are lost");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(getResult)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/findOwners"));
	}

	@Test
	void testInitUpdateOwnerForm() throws Exception {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/{ownerId}/edit", 123);
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testProcessCreationForm() throws Exception {
		doNothing().when(this.ownerRepository).save((Owner) any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/new")
			.param("address", "42 Main St")
			.param("city", "Oxford")
			.param("telephone", "4105551212")
			.param("firstName", "Jane")
			.param("lastName", "Doe");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/null"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/owners/null"));
	}

	@Test
	void testProcessCreationForm2() throws Exception {
		doNothing().when(this.ownerRepository).save((Owner) any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/new")
			.param("address", "")
			.param("city", "Oxford")
			.param("telephone", "4105551212")
			.param("firstName", "Jane")
			.param("lastName", "Doe");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testProcessFindForm() throws Exception {
		when(this.ownerRepository.findByLastName((String) any(), (org.springframework.data.domain.Pageable) any()))
			.thenReturn(new PageImpl<Owner>(new ArrayList<Owner>()));
		MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/owners");
		MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(1));
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/findOwners"));
	}

	@Test
	void testProcessFindForm2() throws Exception {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");

		ArrayList<Owner> ownerList = new ArrayList<Owner>();
		ownerList.add(owner);
		PageImpl<Owner> pageImpl = new PageImpl<Owner>(ownerList);
		when(this.ownerRepository.findByLastName((String) any(), (org.springframework.data.domain.Pageable) any()))
			.thenReturn(pageImpl);
		MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/owners");
		MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(1));
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/1"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/owners/1"));
	}

	@Test
	void testProcessFindForm3() throws Exception {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");

		Owner owner1 = new Owner();
		owner1.setLastName("Doe");
		owner1.setId(1);
		owner1.setCity("Oxford");
		owner1.setPetsInternal(new HashSet<Pet>());
		owner1.setAddress("42 Main St");
		owner1.setFirstName("Jane");
		owner1.setTelephone("4105551212");

		ArrayList<Owner> ownerList = new ArrayList<Owner>();
		ownerList.add(owner1);
		ownerList.add(owner);
		PageImpl<Owner> pageImpl = new PageImpl<Owner>(ownerList);
		when(this.ownerRepository.findByLastName((String) any(), (org.springframework.data.domain.Pageable) any()))
			.thenReturn(pageImpl);
		MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/owners");
		MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(1));
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(5))
			.andExpect(MockMvcResultMatchers.model()
				.attributeExists("currentPage", "listOwners", "owner", "totalItems", "totalPages"))
			.andExpect(MockMvcResultMatchers.view().name("owners/ownersList"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/ownersList"));
	}

	@Test
	void testProcessFindForm4() throws Exception {
		when(this.ownerRepository.findByLastName((String) any(), (org.springframework.data.domain.Pageable) any()))
			.thenReturn(new PageImpl<Owner>(new ArrayList<Owner>()));
		MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/owners");
		MockHttpServletRequestBuilder requestBuilder = getResult.param("page", String.valueOf(Short.SIZE));
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/findOwners"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/findOwners"));
	}

	@Test
	void testProcessUpdateOwnerForm() throws Exception {
		doNothing().when(this.ownerRepository).save((Owner) any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/edit", 123)
			.param("address", "42 Main St")
			.param("city", "Oxford")
			.param("telephone", "4105551212")
			.param("firstName", "Jane")
			.param("lastName", "Doe");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isFound())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/{ownerId}"))
			.andExpect(MockMvcResultMatchers.redirectedUrl("/owners/123"));
	}

	@Test
	void testProcessUpdateOwnerForm2() throws Exception {
		doNothing().when(this.ownerRepository).save((Owner) any());
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.post("/owners/{ownerId}/edit", 123)
			.param("address", "")
			.param("city", "Oxford")
			.param("telephone", "4105551212")
			.param("firstName", "Jane")
			.param("lastName", "Doe");
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/createOrUpdateOwnerForm"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/createOrUpdateOwnerForm"));
	}

	@Test
	void testShowOwner() throws Exception {
		Owner owner = new Owner();
		owner.setLastName("Doe");
		owner.setId(1);
		owner.setCity("Oxford");
		owner.setPetsInternal(new HashSet<Pet>());
		owner.setAddress("42 Main St");
		owner.setFirstName("Jane");
		owner.setTelephone("4105551212");
		when(this.ownerRepository.findById((Integer) any())).thenReturn(owner);
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/{ownerId}", 123);
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/ownerDetails"));
	}

	@Test
	void testShowOwner2() throws Exception {
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
		MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/owners/{ownerId}", 123);
		MockMvcBuilders.standaloneSetup(this.ownerController)
			.build()
			.perform(requestBuilder)
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().size(1))
			.andExpect(MockMvcResultMatchers.model().attributeExists("owner"))
			.andExpect(MockMvcResultMatchers.view().name("owners/ownerDetails"))
			.andExpect(MockMvcResultMatchers.forwardedUrl("owners/ownerDetails"));
	}
}

