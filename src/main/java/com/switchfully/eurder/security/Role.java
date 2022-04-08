package com.switchfully.eurder.security;

import java.util.List;
import static com.google.common.collect.Lists.newArrayList;
import static com.switchfully.eurder.security.Feature.*;


public enum Role {
    ADMIN(newArrayList(CREATE_CUSTOMER, ADD_ITEM, NEW_ORDER,VIEW_ALL_CUSTOMER,VIEW_ONE_CUSTOMER)),
    CUSTOMER(newArrayList(CREATE_CUSTOMER, NEW_ORDER, VIEW_REPORT_OF_CUSTOMER));

    private final List<Feature> featureList;

    Role(List<Feature> featureList) {
        this.featureList = featureList;
    }

    public boolean containsFeature(Feature feature) {
        return featureList.contains(feature);
    }
}
