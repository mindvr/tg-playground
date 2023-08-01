package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Ordering;
import dev.mindvr.tgplayground.persistence.UpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateContextFactoryTest {
    @Mock
    TgBot bot;

    @Mock
    UpdateRepository updateRepository;

    @Mock
    ObjectMapper objectMapper;

    @InjectMocks
    UpdateContextFactory contextFactory;

    List<Update> updates = new ArrayList<>();

    Comparator<Update> testComparator = Comparator.comparing(u -> Optional.of(u)
            .map(Update::getMessage)
            .map(Message::getDate)
            .orElse(null));

    @BeforeEach
    void setUp() {
        contextFactory.setUpdateClassifier(u -> Optional.of(u)
                .map(Update::getMessage)
                .map(Message::getText)
                .map(Long::parseLong)
                .orElse(null));
        contextFactory.setUpdateComparator(testComparator);
    }

    Update buildUpdate(int id, int from, int order) {
        Update update = new Update();
        update.setUpdateId(id);
        update.setMessage(new Message());
        update.getMessage().setText(Integer.toString(from));
        update.getMessage().setDate(order);
        return update;
    }

    @Test
    void botAndUpdateArePassedAsIs() {
        Update update = new Update();
        UpdateContext context = contextFactory.build(update, bot);
        assertSame(bot, context.getBot());
        assertSame(update, context.getUpdate());
    }

    @Test
    void contextContainsUpdatesOfTheSameClass() {
        when(updateRepository.findAll()).thenReturn(updates);
        updates.addAll(List.of(
                buildUpdate(1, 1, 1),
                buildUpdate(2, 2, 1)));
        assertFalse(contextFactory.build(buildUpdate(3, 1, 1), bot).getPrevious().isEmpty());
        assertFalse(contextFactory.build(buildUpdate(3, 2, 1), bot).getPrevious().isEmpty());
        assertTrue(contextFactory.build(buildUpdate(3, 3, 1), bot).getPrevious().isEmpty());
    }

    @Test
    void previousAreSortedWindowSizedAndDoNotContainTheUpdate() {
        when(updateRepository.findAll()).thenReturn(updates);
        updates.addAll(
                List.of(
                        buildUpdate(1, 1, 1),
                        buildUpdate(2, 1, 2),
                        buildUpdate(3, 1, 3)
                )
        );
        Update probe = buildUpdate(1, 1, 1);
        var context = contextFactory.build(probe, bot);
        List<Update> previous = context.getPrevious();
        assertEquals(UpdateContextFactory.CONTEXT_WINDOW_SIZE, previous.size());
        assertEquals(2, previous.get(0).getUpdateId());
        assertTrue(Ordering.from(testComparator).isOrdered(previous));
    }
}