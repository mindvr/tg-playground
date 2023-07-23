package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.command.Command;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EntryPointTest {

    @Mock
    Command mockCommand;

    @Mock
    Update mockUpdate;

    @Mock
    ObjectMapper objectMapper;

    @Mock
    UpdateContextFactory contextFactory;


    @InjectMocks
    EntryPoint entryPoint;

    @Test
    void applicableCommandIsCalled() {
        when(mockCommand.isApplicable(any())).thenReturn(true);
        entryPoint.setCommands(List.of(mockCommand));

        entryPoint.onUpdateReceived(mockUpdate);

        verify(mockCommand, times(1)).handle(any());
    }

    @Test
    void nonApplicableCommandIsNotCalled() {
        when(mockCommand.isApplicable(any())).thenReturn(false);
        entryPoint.setCommands(List.of(mockCommand));

        entryPoint.onUpdateReceived(mockUpdate);

        verify(mockCommand, never()).handle(any());
    }
}