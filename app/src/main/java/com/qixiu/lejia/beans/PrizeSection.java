package com.qixiu.lejia.beans;

import com.chad.library.adapter.base.entity.SectionEntity;

public class PrizeSection extends SectionEntity<PrizeDetail> {
    public PrizeSection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public PrizeSection(PrizeDetail prizeDetail) {
        super(prizeDetail);
    }
}
