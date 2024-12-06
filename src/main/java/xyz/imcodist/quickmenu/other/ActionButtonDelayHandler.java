package xyz.imcodist.quickmenu.other;

import net.minecraft.client.MinecraftClient;
import xyz.imcodist.quickmenu.data.ActionButtonData;

import java.util.ArrayList;
import java.util.List;

public class ActionButtonDelayHandler {

    public static final ActionButtonDelayHandler INSTANCE = new ActionButtonDelayHandler();

    private final List<ActionButtonData.ActionButtonDataContext> delayedActions = new ArrayList<>();

    private final List<ActionButtonData.ActionButtonDataContext> actionsInbox = new ArrayList<>();

    public void add(ActionButtonData.ActionButtonDataContext context){
        actionsInbox.add(context);
    }


    public void doDelayChecks() {
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
