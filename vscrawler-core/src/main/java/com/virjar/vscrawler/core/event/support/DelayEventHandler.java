package com.virjar.vscrawler.core.event.support;

import com.google.common.collect.Maps;
import com.virjar.vscrawler.core.VSCrawlerContext;
import com.virjar.vscrawler.core.event.Event;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by virjar on 17/6/17.
 */
public class DelayEventHandler implements InvocationHandler {
    private long activeTime;
    private VSCrawlerContext vsCrawlerContext;

    public DelayEventHandler(long activeTime, VSCrawlerContext vsCrawlerContext) {
        this.activeTime = activeTime;
        this.vsCrawlerContext = vsCrawlerContext;

    }

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        AutoEvent annotation = method.getAnnotation(AutoEvent.class);
        if (annotation == null) {
            throw new IllegalStateException("can not make " + method.getName() + "  to be a event");
        }
        String topic = annotation.topic();
        if (StringUtils.isEmpty(topic)) {
            topic = method.getDeclaringClass().getName() + "#" + method.getName();
        }

        boolean sync = annotation.sync();

        boolean clearExpire = annotation.clearExpire();

        Event event = new Event(topic);

        Map data = Maps.newHashMap();
        data.put("AutoEvent", true);
        data.put("proxy", proxy);
        data.put("method", method);
        data.put("args", args);
        event.setTime(activeTime);
        event.setData(data);
        event.setSync(sync);
        event.setCleanExpire(clearExpire);
        vsCrawlerContext.getEventLoop().offerEvent(event);
        return null;
    }
}
