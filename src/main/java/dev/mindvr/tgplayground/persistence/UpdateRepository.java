package dev.mindvr.tgplayground.persistence;

import org.springframework.stereotype.Repository;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UpdateRepository {
    private final Map<Integer, Update> updates = new HashMap<>();

    public Update save(Update update) {
        updates.put(update.getUpdateId(), update);
        return update;
    }
}
