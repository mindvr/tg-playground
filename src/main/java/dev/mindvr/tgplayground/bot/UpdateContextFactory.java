package dev.mindvr.tgplayground.bot;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.mindvr.tgplayground.persistence.UpdateRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

@Component
@Slf4j
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class UpdateContextFactory {
    public static final int CONTEXT_WINDOW_SIZE = 1;
    private final UpdateRepository updateRepository;
    private final ObjectMapper objectMapper;
    @Setter
    private Function<Update, Long> updateClassifier;
    @Setter
    private Comparator<Update> updateComparator;

    @SneakyThrows
    public UpdateContext build(Update update, TgBot bot) {
        UpdateContext context = new UpdateContext();
        context.setBot(bot);
        context.setUpdate(update);
        context.setPrevious(getContext(update));
        log.info("built context, update {}, previous {}",
                objectMapper.writeValueAsString(update),
                objectMapper.writeValueAsString(context.getPrevious()));
        return context;
    }

    private List<Update> getContext(Update update) {
        Long marker = updateClassifier.apply(update);
        if (marker == null) return List.of();
        return updateRepository.findAll().stream()
                .filter(filterByMarker(marker))
                .filter(notEqualTo(update))
                .sorted(updateComparator)
                .limit(CONTEXT_WINDOW_SIZE)
                .toList();
    }

    private Predicate<Update> filterByMarker(Long marker) {
        return update -> marker.equals(updateClassifier.apply(update));
    }

    private Predicate<Update> notEqualTo(Update update) {
        var id = update.getUpdateId();
        return u -> !id.equals(u.getUpdateId());
    }
}
