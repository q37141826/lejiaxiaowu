package com.qixiu.lejia.core.me.points;

import com.qixiu.lejia.beans.PointsHistory;
import com.qixiu.lejia.beans.PrizeDetail;
import com.qixiu.lejia.beans.home.story.StoryBean;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static List<PrizeDetail> getSampleData() {
        List<PrizeDetail> list = new ArrayList<>();
        for (int i = 0; i < 35; i++) {
            PrizeDetail prizeDetail = new PrizeDetail();
            prizeDetail.setAge("" + i);

            if (i >= 0 && i < 5) {
                prizeDetail.setTime("2015-06-03");
                prizeDetail.setName("王老吉系列" + i);
            } else if (i >= 5 && i < 15) {
                prizeDetail.setTime("2015-07-06");
                prizeDetail.setName("加多宝系列" + i);

            } else if (i >= 15 && i < 25) {
                prizeDetail.setTime("2018-06-04");
                prizeDetail.setName("赵日天系列" + i);
            } else {
                prizeDetail.setTime("2016-05-02");
                prizeDetail.setName("王二小系列" + i);
            }
            list.add(prizeDetail);
        }
        return list;
    }

    public static List<PointsHistory> getTextData() {
        List<PointsHistory> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            PointsHistory pointsHistory = new PointsHistory();
            if (i % 2 == 0) {
                pointsHistory.setType(1);
                pointsHistory.setPoints(i + "");
                pointsHistory.setTime("2018-03-" + i);
                pointsHistory.setWay("这么帅系列" + i);
            } else {
                pointsHistory.setType(0);
                pointsHistory.setPoints(i + "");
                pointsHistory.setTime("2018-09-" + i);
                pointsHistory.setWay("这么丑系列" + i);
            }
            list.add(pointsHistory);
        }
        return list;
    }

    public static List<StoryBean.ListBean> getStoryBeans() {
        List<StoryBean.ListBean> storyBeans = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            StoryBean.ListBean storyBean = new StoryBean.ListBean();
            storyBean.setTitle("你这么美"+i);
            storyBean.setRead("人生就像一座山，重要的不是它的高低，而在于灵秀；人生就像一场雨，重要的不是它的大小，而在于及时。");
            storyBean.setUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1531808412&di=114a95ff95c556fc87c7034bffe702b6&src=http://pic.58pic.com/58pic/13/71/26/37q58PICMgF_1024.jpg");
            storyBeans.add(storyBean);
        }
        return storyBeans;
    }
}
