package com.goodboy.telegram.bot.spring.impl.processors.annotations.webhook;

import com.goodboy.telegram.bot.api.methods.ApiRequest;
import com.goodboy.telegram.bot.api.methods.action.SendChatActionApi;
import com.goodboy.telegram.bot.api.methods.action.TelegramChatActionApi;
import com.goodboy.telegram.bot.spring.api.actions.FutureFastAction;
import com.goodboy.telegram.bot.spring.api.processor.HeavyweightScheduler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.DisposableBean;

import java.time.Instant;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;

@Slf4j
@RequiredArgsConstructor
public class NotificationHeavyweightScheduler implements HeavyweightScheduler, DisposableBean {

    // the power of 2 for missing division consistency like 25 / 2 = 12 != 12 * 2
    // default executing batch size, 10 is experimental value
    private final static int DEFAULT_EXECUTING_BATCH_SIZE = 8;
    // default executing batch size, 10 is experimental value
    private final static int UPPER_BOUND_EXECUTING_BATCH_SIZE = 64;

    // period btw task executions
    private final static int PERIOD_BETWEEN_SUCCESSIVE_EXECUTIONS  = 300;
    // minimum time shift for delaying executing thread
    private final static int MINIMUM_INCONGRUITY_SHIFT  = 300;

    private final ScheduledExecutorService executorService;
    private final Queue<NotificationTask> queue;

    private final TelegramChatActionApi api;

    public NotificationHeavyweightScheduler(Queue<NotificationTask> queue, TelegramChatActionApi api) {
        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.queue = queue;
        this.api = api;

        // this implementation based on single scheduled thread
        // fix rate is 300 millis
        // dynamic task limitation
        this.executorService.scheduleAtFixedRate(new NotificationTaskScheduler(), 0, PERIOD_BETWEEN_SUCCESSIVE_EXECUTIONS, TimeUnit.MILLISECONDS);
    }

    // task clearing the queue
    // task dynamically change batch size dependent in miss calls
    // got max and min batch size
    // got inner executing delay (sleep) if new task (tasks in queue ordered)  from queue got estimated
    // execution time above now
    private class NotificationTaskScheduler implements Runnable{

        private int BATCH_SIZE = DEFAULT_EXECUTING_BATCH_SIZE;

        @Override
        public void run() {
            try {
                int i = 0;

                for (; i < BATCH_SIZE; i++) {
                    final NotificationTask task = queue.poll();

                    // queue is empty
                    if (task == null)
                        break;

                    final long estimatedExecutionTime = task.getTimestamp();

                    // if estimated task time in the future and difference btw estimated time and now more than
                    // fixed incongruity value - sleep thread on time diff
                    final long incongruity;
                    if ((incongruity = estimatedExecutionTime - getTimestamp()) > 0 && incongruity > MINIMUM_INCONGRUITY_SHIFT)
                        TimeUnit.MILLISECONDS.sleep(incongruity - MINIMUM_INCONGRUITY_SHIFT);

                    final FutureFastAction action = task.getAction();

                    // check that task not executed yet
                    if(!action.isDone()) {
                        // notify client about fast action
                        NotificationHeavyweightScheduler.this.notify(action);

                        // registry task if possible
                        registryNewNotificationTask(action, true);
                    }
                }

                dynamicBatchSizeEvaluation(BATCH_SIZE - i);
            } catch (Exception exception) {
                log.warn("[heavyweight scheduler] queue executing task partly failed", exception);
            }
        }

        private void dynamicBatchSizeEvaluation(int miss) {
            if(miss == BATCH_SIZE)
                BATCH_SIZE = Math.max(BATCH_SIZE / 2, DEFAULT_EXECUTING_BATCH_SIZE);
            else if(miss == 0){
                BATCH_SIZE = Math.min(BATCH_SIZE * 2, UPPER_BOUND_EXECUTING_BATCH_SIZE);
            }
        }
    }

    @Override
    public void poll(@NotNull FutureFastAction fast) {
        try {
            // notification before creating task create 'arbitrary' order in the queue
            // this expression describes simple ordering algorithm based on natural timeline
            // action -do-> notify [then] task(fast, now + 5 seconds) -add in the end-> queue[1|...|n|here] <-get first- scheduler
            // 1) all tasks have to be executed in order
            // 2) order have to be based on timeline - that mean that tasks created early should be executed in priority
            // 3) notification before adding in the queue guarantees that if we adding task in the end of
            // queue task will be last in timeline - coz we knew the fact that chat action is executing fixed time
            // equal 5 seconds; simple addition operation of max shift value guarantees 'arbitrary' order - all already existing tasks will be
            // younger

            // try to send heavy operation permanently - dont check is done
            notify(fast);

            // attempt to registry new fast action
            registryNewNotificationTask(fast, false);
        } catch (Exception exception) {
            log.warn(format("[heavyweight scheduler] poll registry for { chat_id = %s  } failed", fast.getChatId()), exception);
        }
    }

    private void notify(@NotNull FutureFastAction fast) {
        try {
            api.sendChatAction(new ApiRequest<SendChatActionApi>()
                    .setApi(new SendChatActionApi(fast.getChatId(), fast.next()))
                    .setToken(fast.getToke())
            );
        } catch (Exception exception) {
            log.warn(format("[heavyweight scheduler] chat action notification for { chat_id = %s } failed", fast.getChatId()), exception);
        }
    }

    private void registryNewNotificationTask(@NotNull FutureFastAction action, boolean doneIsChecked) {
        // if fast action already needed
        if(doneIsChecked || !action.isDone()) {
            // if task not already executed registry it in the scheduler
            // create task with current time + 5s
            final var task = new NotificationTask(action, getTimestamp() + 5000);

            // registry task
            queue.offer(task);
        }
    }

    /**
     * @return timestamp in milli !
     */
    private long getTimestamp() {
        return Instant.now().toEpochMilli();
    }

    @Override
    public void destroy() throws Exception {
        executorService.shutdown();
    }

    @Getter
    @RequiredArgsConstructor
    private static class NotificationTask {
        private final FutureFastAction action;
        private final long timestamp;
    }
}
