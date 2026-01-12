package com.tehbeard.fabric.quickaction.data;

import com.tehbeard.fabric.quickaction.data.action.IActionTask;
import xyz.imcodist.quickmenu.data.ActionButtonData;
import xyz.imcodist.quickmenu.other.ActionButtonDelayHandler;

import java.util.List;

public class ActionButtonExecutorContext {

    private List<IActionTask> tasks;
    private long delay;

    public ActionButtonExecutorContext(List<IActionTask> tasks, long delay) {
        this.tasks = tasks;
        this.delay = delay;
    }

    public void run() {
        for (var idx = 0; idx < tasks.size(); idx++) {
            var action = tasks.get(idx);
            var nextDelay = action.run();
            if (nextDelay > 0) {
                // TODO : Envelope the remaining actions in a timer.
                var nextSet = new ActionButtonExecutorContext(tasks.subList(idx + 1, tasks.size()), nextDelay);
                // TODO - Create a timer construct that listens
                ActionButtonExecutor.getInstance().queue(nextSet);
                return;
            }
        }
    }

    public long getDelay() {
        return delay;
    }

    public void decrementDelay()
    {
        delay = Math.max(0, delay - 1);
    }

    public boolean isReady()
    {
        return delay == 0;
    }
}
