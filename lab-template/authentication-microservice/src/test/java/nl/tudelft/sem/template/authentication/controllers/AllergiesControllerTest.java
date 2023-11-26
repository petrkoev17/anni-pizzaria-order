package nl.tudelft.sem.template.authentication.controllers;

import commons.UserRole;
import commons.authentication.AuthenticationManager;
import commons.authentication.JwtTokenVerify;
import nl.tudelft.sem.template.authentication.domain.user.AppUser;
import nl.tudelft.sem.template.authentication.domain.user.UserRepository;
import nl.tudelft.sem.template.authentication.domain.user.valueobjects.HashedPassword;
import nl.tudelft.sem.template.authentication.domain.user.valueobjects.NetId;
import nl.tudelft.sem.template.authentication.models.AllergiesRequestModel;
import nl.tudelft.sem.template.authentication.models.AllergiesResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@ActiveProfiles({"test", "mockTokenVerifier", "mockAuthenticationManager"})
@AutoConfigureMockMvc
class AllergiesControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Autowired
    private transient JwtTokenVerify mockJwtTokenVerifier;

    private transient AuthenticationManager mockAuthenticationManager;

    private transient AllergiesController allergiesController;

    private transient UserRepository userRepository;

    public AllergiesControllerTest() {
        mockAuthenticationManager = mock(AuthenticationManager.class);
        userRepository = mock(UserRepository.class);
        allergiesController = new AllergiesController(userRepository, mockAuthenticationManager);
    }

    @BeforeEach
    void setUp() {
        List<Long> list = new ArrayList<>(List.of(1L, 2L, 3L));
        nl.tudelft.sem.template.authentication.domain.user.valueobjects.UserRole ur =
                new nl.tudelft.sem.template.authentication.domain.user.valueobjects.UserRole("customer");

        AppUser user = new AppUser(new NetId("ExampleUser"), new HashedPassword("123"), ur, list);
        when(mockAuthenticationManager.getNetId()).thenReturn("ExampleUser");
        when(mockJwtTokenVerifier.getUserIdFromToken(anyString())).thenReturn("ExampleUser");
        when(mockAuthenticationManager.getRole()).thenReturn("customer");
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.validateToken(anyString())).thenReturn(true);
        when(mockJwtTokenVerifier.getUserRoleFromToken(anyString())).thenReturn(
                new UserRole("customer"));
        when(userRepository.findByNetId(new NetId("ExampleUser"))).thenReturn(Optional.of(user));
    }

    @Test
    void addAllergyOk() throws Exception {
        AllergiesRequestModel ar = new AllergiesRequestModel();
        List<Long> allergies = new ArrayList<>(List.of(1L, 2L));
        ar.setAllergies(allergies);

        ResponseEntity r = allergiesController.addAllergy(ar);

        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void addAllergyFail() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("NoUser");
        when(mockJwtTokenVerifier.getUserIdFromToken(anyString())).thenReturn("NoUser");
        when(userRepository.findByNetId(new NetId("NoUser"))).thenReturn(Optional.empty());

        AllergiesRequestModel ar = new AllergiesRequestModel();
        List<Long> allergies = new ArrayList<>(List.of(1L, 2L));
        ar.setAllergies(allergies);

        assertThatThrownBy(() -> {
            ResponseEntity r = allergiesController.addAllergy(ar);
        }).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400 BAD_REQUEST \"Invalid token\"");

    }

    @Test
    void addAllergySuccessful() throws Exception {
        AllergiesRequestModel ar = new AllergiesRequestModel();
        List<Long> allergies = new ArrayList<>(List.of(5L, 6L));
        ar.setAllergies(allergies);

        ResponseEntity r = allergiesController.addAllergy(ar);

        ResponseEntity<AllergiesResponseModel> result = allergiesController.getAllergies();
        List<Long> real = new ArrayList<>(List.of(1L, 2L, 3L, 5L, 6L));
        assertThat(result.getBody().getAllergies()).isEqualTo(real);
    }

    @Test
    void getAllergiesOk() throws Exception {
        ResponseEntity<AllergiesResponseModel> r = allergiesController.getAllergies();
        assertThat(r.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getAllergiesList() throws Exception {
        ResponseEntity<AllergiesResponseModel> r = allergiesController.getAllergies();
        List<Long> real = new ArrayList<>(List.of(1L, 2L, 3L));
        assertThat(r.getBody().getAllergies()).isEqualTo(real);
    }

    @Test
    void getAllergiesNoUserFound() throws Exception {
        when(mockAuthenticationManager.getNetId()).thenReturn("NoUser");
        when(mockJwtTokenVerifier.getUserIdFromToken(anyString())).thenReturn("NoUser");
        when(userRepository.findByNetId(new NetId("NoUser"))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            ResponseEntity<AllergiesResponseModel> e = allergiesController.getAllergies();
        }).isInstanceOf(ResponseStatusException.class)
                .hasMessageContaining("400 BAD_REQUEST \"Invalid token\"");
    }
}