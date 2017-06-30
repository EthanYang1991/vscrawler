package com.virjar.vscrawler.core.selector.model.convert;

import java.util.List;
import java.util.Map;

import org.jsoup.select.Elements;

import com.google.common.collect.Maps;
import com.virjar.vscrawler.core.selector.model.AbstractSelectable;
import com.virjar.vscrawler.core.selector.model.selectables.*;

import lombok.Getter;

/**
 * Created by virjar on 17/6/30.
 */
public class Converters {
    private static Map<RegistryHolder, NodeConvert> nodeConvertMap = Maps.newHashMap();
    static {
        registerDefault();
    }

    private static void registerHtm() {
        register(HtmlNode.class, HtmlNode.class, new NodeConvert<HtmlNode, HtmlNode>() {
            @Override
            public HtmlNode convert(HtmlNode from) {
                return from;
            }
        });

        register(JsonNode.class, HtmlNode.class, new NodeConvert<JsonNode, HtmlNode>() {
            @Override
            public HtmlNode convert(JsonNode from) {
                // json to json
                throw new UnsupportedOperationException("can not convert a json data to html data");
            }
        });

        register(RawNode.class, HtmlNode.class, new NodeConvert<RawNode, HtmlNode>() {
            @Override
            public HtmlNode convert(RawNode from) {
                return new HtmlNode(from.getBaseUrl(), from.getRowText());
            }
        });

        register(StringNode.class, HtmlNode.class, new NodeConvert<StringNode, HtmlNode>() {
            @Override
            public HtmlNode convert(StringNode from) {
                List<String> strings = from.createOrGetModel();
                if (strings.size() >= 1) {
                    return new HtmlNode(from.getBaseUrl(), strings.get(0));
                }
                return new HtmlNode(from.getBaseUrl(), "");
            }
        });

        register(ElementsNode.class, HtmlNode.class, new NodeConvert<ElementsNode, HtmlNode>() {
            @Override
            public HtmlNode convert(ElementsNode from) {
                Elements orGetModel = from.createOrGetModel();
                if (orGetModel.size() == 1) {
                    HtmlNode htmlNode = new HtmlNode(from.getBaseUrl(), orGetModel.first().html());
                    htmlNode.setModel(orGetModel.first());
                }
                return null;
            }
        });
    }

    private static void registerJson() {

    }

    private static void registerRow() {

    }

    private static void registerRegex() {

    }

    private static void registerString() {

    }

    private static void registerXpath() {

    }

    private static void registerDefault() {
        registerHtm();
        registerJson();
        registerRow();
        registerRegex();
        registerString();
        registerXpath();
    }

    public static <F extends AbstractSelectable, T extends AbstractSelectable> void register(Class<F> from, Class<T> to,
            NodeConvert<F, T> nodeConvert) {
        nodeConvertMap.put(new RegistryHolder(from, to), nodeConvert);
    }

    @SuppressWarnings("unchecked")
    public static <F extends AbstractSelectable, T extends AbstractSelectable> NodeConvert<F, T> findConvert(
            Class<? extends AbstractSelectable> from, Class<? extends AbstractSelectable> to) {
        return nodeConvertMap.get(new RegistryHolder(from, to));
    }

    private static class RegistryHolder {
        RegistryHolder(Class<? extends AbstractSelectable> from, Class<? extends AbstractSelectable> to) {
            this.from = from;
            this.to = to;
        }

        @Getter
        private Class<? extends AbstractSelectable> from;
        @Getter
        private Class<? extends AbstractSelectable> to;

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;

            RegistryHolder that = (RegistryHolder) o;

            if (from != null ? !from.equals(that.from) : that.from != null)
                return false;
            return to != null ? to.equals(that.to) : that.to == null;

        }

        @Override
        public int hashCode() {
            int result = from != null ? from.hashCode() : 0;
            result = 31 * result + (to != null ? to.hashCode() : 0);
            return result;
        }
    }

}
