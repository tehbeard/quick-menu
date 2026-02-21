package com.tehbeard.fabric.fastaction.data;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Handles execution of delayed tasks
 */
public class ActionButtonExecutor {

    private static final ActionButtonExecutor instance = new ActionButtonExecutor();

    public static ActionButtonExecutor getInstance()
    {
        return instance;
    }

    private final List<ActionButtonExecutorContext> delayedActions = new ArrayList<>();

    private final List<ActionButtonExecutorContext> actionsInbox = new ArrayList<>();

    public void queue(ActionButtonExecutorContext context){
        actionsInbox.add(context);
    }


    public void tick() {
        for(var it = delayedActions.iterator(); it.hasNext();){
            var action = it.next();
            action.decrementDelay();
            if(action.isReady()){
                action.run();
                it.remove();
            }
        }
        if(!actionsInbox.isEmpty()) {
            delayedActions.addAll(actionsInbox);
            actionsInbox.clear();
        }
    }
}
