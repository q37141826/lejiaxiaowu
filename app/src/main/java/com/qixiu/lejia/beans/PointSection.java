package com.qixiu.lejia.beans;

import com.chad.library.adapter.base.entity.SectionEntity;

public class PointSection extends SectionEntity<PointsHistory> {
    public PointSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public PointSection(PointsHistory pointsHistory) {
        super(pointsHistory);
    }
}
