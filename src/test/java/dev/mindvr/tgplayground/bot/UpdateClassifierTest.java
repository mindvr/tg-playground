package dev.mindvr.tgplayground.bot;

import dev.mindvr.tgplayground.ResourceLoader;
import org.junit.jupiter.api.Test;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UpdateClassifierTest {
    UpdateClassifier classifier = new UpdateClassifier();
    ResourceLoader resourceLoader = new ResourceLoader();

    @Test
    void chatIdIsReturned() {
        var u1 = resourceLoader.load(Update.class, this.getClass(), "u1.json");
        assertEquals(98760000, classifier.apply(u1));
    }


}