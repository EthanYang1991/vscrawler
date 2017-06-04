package com.virjar.vscrawler.core.processor;

import java.net.MalformedURLException;
import java.net.URL;

import com.virjar.vscrawler.core.net.session.CrawlerSession;
import com.virjar.vscrawler.core.seed.Seed;

import lombok.extern.slf4j.Slf4j;

/**
 * Created by virjar on 17/5/20.
 * 
 * @author virjar
 * @since 0.0.1
 */
@Slf4j
public abstract class AutoParseSeedProcessor implements SeedProcessor {
    private InheritableThreadLocal<CrawlerSession> crawlerSessionInheritableThreadLocal = new InheritableThreadLocal<>();

    @Override
    public void process(Seed seed, CrawlerSession crawlerSession, CrawlResult crawlResult) {
        URL url;
        try {
            url = new URL(seed.getData());
        } catch (MalformedURLException e) {
            log.warn("this seed is not a url:{}", seed.getData(), e);
            seed.setIgnore(true);
            return;
        }
        try {
            crawlerSessionInheritableThreadLocal.set(crawlerSession);
            parse(seed, download(crawlerSession, url), crawlResult);
        } finally {
            crawlerSessionInheritableThreadLocal.remove();
        }
    }

    protected abstract void parse(Seed seed, String result, CrawlResult crawlResult);

    /**
     * 自动解析的支持,会限制使用场景,封装意味着放弃灵活
     * 
     * @param crawlerSession session
     * @param url 链接
     * @return 下载结果
     */
    protected String download(CrawlerSession crawlerSession, URL url) {
        return crawlerSession.getCrawlerHttpClient().get(url.toString());
    }

    public CrawlerSession getCrawlerSession() {
        return crawlerSessionInheritableThreadLocal.get();
    }
}
