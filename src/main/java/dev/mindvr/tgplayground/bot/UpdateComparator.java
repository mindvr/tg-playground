package dev.mindvr.tgplayground.bot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Comparator;


/**
 * LIFO
 */
@Component
public class UpdateComparator implements Comparator<Update> {
    private final Comparator<Update> delegate = Comparator.comparing(Update::getUpdateId).reversed();

    @Override
    public int compare(Update o1, Update o2) {
        return delegate.compare(o1, o2);
    }

}
