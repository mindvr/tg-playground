package dev.mindvr.tgplayground.bot;

import dev.mindvr.tgplayground.ResourceLoader;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class UpdateComparatorTest {
    private final ResourceLoader loader = new ResourceLoader();
    private final UpdateComparator comparator = new UpdateComparator();

    @Test
    void testOrdering() {
        var u1 = loader.load(Update.class, this.getClass(), "u1.json");
        var u2 = loader.load(Update.class, this.getClass(), "u2.json");
        var u3 = loader.load(Update.class, this.getClass(), "u3.json");
        var u4 = loader.load(Update.class, this.getClass(), "u4.json");
        Update[] updates = {u4, u3, u2, u1};
        Update[] sorted = Arrays.copyOf(updates, updates.length);
        Arrays.sort(sorted, comparator);
        for (int i = 0; i < updates.length; i++) {
            assertEquals(updates[i], sorted[i]);
        }
    }

}