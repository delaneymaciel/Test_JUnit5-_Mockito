package br.com.dicasdeumdev.api.services.impl;

import br.com.dicasdeumdev.api.domain.User;
import br.com.dicasdeumdev.api.domain.dto.UserDTO;
import br.com.dicasdeumdev.api.repositories.UserRepository;
import br.com.dicasdeumdev.api.services.exceptions.DataIntegratyViolationException;
import br.com.dicasdeumdev.api.services.exceptions.ObjectNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    public static final Integer ID = 1;
    public static final String NAME = "Teste 1";
    public static final String EMAIL = "teste1@gmail.com";
    public static final String PASSWORD = "senha teste1";
    public static final int INDEX = 0;
    @InjectMocks
    private UserServiceImpl service;

    @Mock
    private UserRepository repository;

    @Mock
    private ModelMapper mapper;

    private User user;
    private UserDTO userDTO;
    private Optional<User> optionalUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
    }

    @Test
    void whenFindByIdThenReturnAnUserInstance() {
        when(repository.findById(Mockito.anyInt())).thenReturn(optionalUser);
        User response = service.findById(ID);

        Assertions.assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());
    }

    @Test
    void whenFindByIdThenReturnAnObjectNotFoundException() {
        when(repository.findById(Mockito.anyInt())).thenThrow(new ObjectNotFoundException("Objeto não encontrado."));

        try{
            service.findById(ID);
        } catch (Exception ex){
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("Objeto não encontrado.", ex.getMessage());
        }

    }
    @Test
    void whenfindAllThenReturnAnListOfUsers() {
        when(repository.findAll()).thenReturn(List.of(user));
        List<User> response = service.findAll();

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(User.class, response.get(INDEX).getClass());

        assertEquals(ID, response.get(INDEX).getId());
        assertEquals(NAME, response.get(INDEX).getName());
        assertEquals(EMAIL, response.get(INDEX).getEmail());
        assertEquals(PASSWORD, response.get(INDEX).getPassword());

    }

    @Test
    void whenCreateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(user);
        User response = service.create(userDTO);

        Assertions.assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(PASSWORD,response.getPassword());
    }

    @Test
    void whenCreateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);
        User response = service.create(userDTO);

        try{
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception ex){
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("E-mail já cadastrado no sistema.",ex.getMessage());
        }
    }
    @Test
    void whenUpdateThenReturnSuccess() {
        when(repository.save(any())).thenReturn(user);
        User response = service.update(userDTO);

        Assertions.assertNotNull(response);
        assertEquals(User.class, response.getClass());
        assertEquals(ID,response.getId());
        assertEquals(NAME,response.getName());
        assertEquals(EMAIL,response.getEmail());
        assertEquals(PASSWORD,response.getPassword());
    }
    @Test
    void whenUpdateThenReturnAnDataIntegrityViolationException() {
        when(repository.findByEmail(anyString())).thenReturn(optionalUser);
        User response = service.create(userDTO);

        try{
            optionalUser.get().setId(2);
            service.create(userDTO);
        } catch (Exception ex){
            assertEquals(DataIntegratyViolationException.class, ex.getClass());
            assertEquals("E-mail já cadastrado no sistema.",ex.getMessage());
        }
    }


    @Test
    void deleteWithSuccess() {
        when(repository.findById(anyInt())).thenReturn(optionalUser);
        Mockito.doNothing().when(repository).deleteById(anyInt());

        service.delete(ID);
        Mockito.verify(repository, times(1)).deleteById(anyInt());
    }

    @Test
    void deleteWithObjectNotFoundException() {
        when(repository.findById(anyInt())).
                thenThrow(new ObjectNotFoundException("Objeto não encontrado."));

        try {
            service.delete(ID);
        } catch (Exception ex) {
            assertEquals(ObjectNotFoundException.class, ex.getClass());
            assertEquals("Objeto não encontrado.", ex.getMessage());
        }
    }

    private void startUser(){
        user = new User(ID, NAME, EMAIL, PASSWORD);
        userDTO = new UserDTO(ID, NAME, EMAIL, PASSWORD);
        optionalUser = Optional.of(new User(ID, NAME, EMAIL, PASSWORD));
    }
}