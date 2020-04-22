package com.example.mynewsapplication;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class QueryUtilsTest {
    @Test
    public void fetchMyNewsData() {
        List<MyNews> news = QueryUtils.fetchMyNewsData("https://content.guardianapis.com/search?order-by=newest&page=10&q=science&api-key=test");

        Assert.assertFalse(news.isEmpty());
    }
}