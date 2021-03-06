package ameba.event;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author icode
 */
public class EventTest {

    private static final Logger logger = LoggerFactory.getLogger(EventTest.class);

    @Test
    public void publish() {
        for (int i = 0; i < 10; i++) {
            SystemEventBus.subscribe(TestEvent.class, new AsyncListener<TestEvent>() {
                @Override
                public void onReceive(TestEvent event) {
                    logger.info("async receive message : {}", event.message);
                }
            });

            SystemEventBus.subscribe(TestEvent1.class, new AsyncListener<TestEvent1>() {
                @Override
                public void onReceive(TestEvent1 event) {
                    logger.info("TestEvent1 async receive message : {}", event.message);
                }
            });
        }
        for (int i = 0; i < 5; i++) {
            SystemEventBus.subscribe(TestEvent.class, new Listener<TestEvent>() {
                @Override
                public void onReceive(TestEvent event) {
                    logger.info("receive message : {}", event.message);
                }
            });

            SystemEventBus.subscribe(TestEvent1.class, new Listener<TestEvent1>() {
                @Override
                public void onReceive(TestEvent1 event) {
                    logger.info("TestEvent1 receive message : {}", event.message);
                }
            });
        }

        logger.info("publish message ..");
        for (int i = 0; i < 10; i++) {
            SystemEventBus.publish(new TestEvent("message: " + i));
            SystemEventBus.publish(new TestEvent1("message: " + i));
        }

        try {
            synchronized (this) {
                wait(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class TestEvent extends Event {
        public String message;

        public TestEvent(String message) {
            this.message = message;
        }
    }

    public static class TestEvent1 extends Event {
        public String message;

        public TestEvent1(String message) {
            this.message = message;
        }
    }
}
