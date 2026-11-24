package leoastray.examencinerest.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import leoastray.examencinerest.model.entity.Actor;
import leoastray.examencinerest.model.entity.Pelicula;
import leoastray.examencinerest.model.entity.Reparto;
import leoastray.examencinerest.model.repository.IActorRepository;
import leoastray.examencinerest.model.repository.IPeliculaRepository;
import leoastray.examencinerest.model.repository.IRepartoRepository;
import leoastray.examencinerest.service.impl.PeliculaServiceImpl;

class PeliculaRepartoTests {

    private static final String CUERPO = """
            {"actorId": 3, "personaje": "Doctora Lemaire", "salario": 80000}
            """;

    private IPeliculaRepository peliculaRepository;
    private IActorRepository actorRepository;
    private IRepartoRepository repartoRepository;
    private MockMvc mvc;
    private Pelicula pelicula;
    private Actor actor;

    @BeforeEach
    void preparar() {
        peliculaRepository = mock(IPeliculaRepository.class);
        actorRepository = mock(IActorRepository.class);
        repartoRepository = mock(IRepartoRepository.class);
        PeliculaServiceImpl servicio = new PeliculaServiceImpl(
                peliculaRepository, actorRepository, repartoRepository);
        PeliculaController controlador = new PeliculaController();
        ReflectionTestUtils.setField(controlador, "peliculaService", servicio);
        mvc = MockMvcBuilders.standaloneSetup(controlador).build();

        pelicula = new Pelicula("Titulo", "Director", 2020, 100);
        pelicula.setId(1L);
        actor = new Actor();
        actor.setId(3L);
        actor.setNombre("Actriz");
        when(peliculaRepository.findById(1L)).thenReturn(Optional.of(pelicula));
        when(actorRepository.findById(3L)).thenReturn(Optional.of(actor));
    }

    @Test
    void crearRepartoDevuelveDetalleActualizadoYLocation() throws Exception {
        Actor otroActor = new Actor();
        otroActor.setId(2L);
        otroActor.setNombre("Otro actor");
        pelicula.addRepartos(new Reparto("Testigo", 1000.0, pelicula, otroActor));

        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/peliculas/1"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.titulo").value("Titulo"))
                .andExpect(jsonPath("$.director").value("Director"))
                .andExpect(jsonPath("$.anio").value(2020))
                .andExpect(jsonPath("$.duracion").value(100))
                .andExpect(jsonPath("$.reparto.length()").value(2))
                .andExpect(jsonPath("$.reparto[0].actorId").value(2))
                .andExpect(jsonPath("$.reparto[1].actorId").value(3))
                .andExpect(jsonPath("$.reparto[1].actor").value("Actriz"))
                .andExpect(jsonPath("$.reparto[1].personaje").value("Doctora Lemaire"))
                .andExpect(jsonPath("$.reparto[1].salario").value(80000));

        ArgumentCaptor<Reparto> captor = ArgumentCaptor.forClass(Reparto.class);
        verify(repartoRepository).save(captor.capture());
        assertSame(pelicula, captor.getValue().getPelicula());
        assertSame(actor, captor.getValue().getActor());
        assertEquals("Doctora Lemaire", captor.getValue().getPersonaje());
        assertEquals(80000.0, captor.getValue().getSalario());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "{\"actorId\":null,\"personaje\":\"Doctora\",\"salario\":80000}",
            "{\"personaje\":\"Doctora\",\"salario\":80000}",
            "{\"actorId\":3,\"personaje\":null,\"salario\":80000}",
            "{\"actorId\":3,\"personaje\":\"\",\"salario\":80000}",
            "{\"actorId\":3,\"personaje\":\"   \",\"salario\":80000}",
            "{\"actorId\":3,\"personaje\":\"Doctora\",\"salario\":-1}"
    })
    void rechazarDatosInvalidosSinConsultarRepositorios(String cuerpo) throws Exception {
        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON).content(cuerpo))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(peliculaRepository, actorRepository, repartoRepository);
    }

    @Test
    void permitirSalarioCero() throws Exception {
        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"actorId\":3,\"personaje\":\"Doctora\",\"salario\":0}"))
                .andExpect(status().isCreated());
    }

    @Test
    void peliculaInexistenteDevuelve404() throws Exception {
        when(peliculaRepository.findById(1L)).thenReturn(Optional.empty());
        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isNotFound());
        verifyNoInteractions(actorRepository, repartoRepository);
    }

    @Test
    void actorInexistenteDevuelve404() throws Exception {
        when(actorRepository.findById(3L)).thenReturn(Optional.empty());
        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isNotFound());
        verifyNoInteractions(repartoRepository);
    }

    @Test
    void actorYaIncluidoDevuelve409SinGuardar() throws Exception {
        when(repartoRepository.existsByPeliculaIdAndActorId(1L, 3L)).thenReturn(true);
        mvc.perform(post("/api/peliculas/1/reparto")
                        .contentType(MediaType.APPLICATION_JSON).content(CUERPO))
                .andExpect(status().isConflict());
        verify(repartoRepository, never()).save(any(Reparto.class));
        assertEquals(0, pelicula.getRepartos().size());
    }
}
