package com.virjar.vscrawler.core.processor.configurableprocessor.annotiondriven;

import com.google.common.collect.Lists;
import com.virjar.vscrawler.core.seed.Seed;
import com.virjar.vscrawler.core.selector.combine.AbstractSelectable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by virjar on 2017/12/10.<br/>
 * covert data to field and inject to model
 */
public class Render<T extends AbstractAutoProcessModel> {
    private List<FetchTask> fetchTaskList = Lists.newLinkedList();

    public void registerTask(FetchTask fetchTask) {
        fetchTaskList.add(fetchTask);
    }

    public List<Seed> injectField(T model, AbstractSelectable abstractSelectable) {
        List<Seed> newSeeds = Lists.newLinkedList();
        try {
            for (FetchTask fetchTask : fetchTaskList) {
                Object data = fetchTask.getModelSelector().select(abstractSelectable).createOrGetModel();
                if (data == null) {
                    continue;
                }
                Field field = fetchTask.getField();
                if (field.getType().isAssignableFrom(data.getClass())) {
                    field.set(model, data);
                    continue;
                }
                //TODO
            }
        } catch (Exception e) {
            throw new RuntimeException("can not inject data for model:" + model.getClass().getName(), e);
        }
        return newSeeds;
    }
}
