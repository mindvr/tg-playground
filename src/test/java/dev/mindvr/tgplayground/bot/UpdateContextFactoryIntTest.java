package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.ResourceLoader;
import dev.mindvr.tgplayground.persistence.UpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateContextFactoryIntTest {
    ResourceLoader loader = new ResourceLoader();
    @Mock
    UpdateRepository updateRepository;
    List<Update> updates = new ArrayList<>();
    //consecutive, u3 is from other user
    Update u1 = loader.load(Update.class, this.getClass(), "u1.json");
    Update u2 = loader.load(Update.class, this.getClass(), "u2.json");
    Update u3 = loader.load(Update.class, this.getClass(), "u3.json");
    Update u4 = loader.load(Update.class, this.getClass(), "u4.json");
    @Mock
    ObjectMapper objectMapper;
    @Mock
    TgBot bot;

    @InjectMocks
    UpdateContextFactory updateContextFactory;

    @BeforeEach
    void setUp() {
        when(updateRepository.findAll()).thenReturn(updates);
        updateContextFactory.setUpdateComparator(new UpdateComparator());
        updateContextFactory.setUpdateClassifier(new UpdateClassifier());
    }

    @Test
    void u1() {
        updates.add(u1);
        var context = updateContextFactory.build(u1, bot);
        assertSame(u1, context.getUpdate());
        assertTrue(context.getPrevious().isEmpty());
    }

    @Test
    void u2() {
        updates.addAll(List.of(u2, u1));
        var context = updateContextFactory.build(u2, bot);
        assertSame(u2, context.getUpdate());
        assertEquals(u1, context.getPrevious().get(0));
    }

    @Test
    void u3() {
        updates.addAll(List.of(u1, u3, u2));
        var context = updateContextFactory.build(u3, bot);
        assertSame(u3, context.getUpdate());
        assertTrue(context.getPrevious().isEmpty());
    }

    @Test
    void u4() {
        updates.addAll(List.of(u4, u1, u3, u2));
        var context = updateContextFactory.build(u4, bot);
        assertSame(u4, context.getUpdate());
        assertEquals(u2, context.getPrevious().get(0));
    }
}