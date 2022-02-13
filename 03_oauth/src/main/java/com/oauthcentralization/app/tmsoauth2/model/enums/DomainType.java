package com.oauthcentralization.app.tmsoauth2.model.enums;

import com.oauthcentralization.app.tmsoauth2.utilities.CollectionsUtility;
import com.oauthcentralization.app.tmsoauth2.utilities.StringUtility;
import com.oauthcentralization.app.tmsoauth2.utils.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings({"all"})
public enum DomainType {
    DOMAIN_TELEPHONE_247_VN(1, "@tele247.com", OrganizationType.VIETNAM),
    DOMAIN_EKIWI_VN(2, "@ekiwi.com", OrganizationType.VIETNAM),
    DOMAIN_EKIWI_ID(3, "@ekiwiID.com", OrganizationType.INDONESIA),
    DOMAIN_EN_FORTE_TH(4, "@enforte.asia", OrganizationType.THAILAND),
    DOMAIN_UNDEFINED(100, "@undefined.com", OrganizationType.UNDEFINE);


    private final int order;
    private final String name;
    private final OrganizationType organizationType;

    DomainType(int order, String name, OrganizationType organizationType) {
        this.order = order;
        this.name = name;
        this.organizationType = organizationType;
    }

    public static List<DomainType> findByOrganizationType(OrganizationType organizationType) {
        if (!ObjectUtils.allNotNull(organizationType)) {
            return Collections.emptyList();
        }

        List<DomainType> domainTypes = new ArrayList<>();

        for (DomainType domainType : DomainType.values()) {
            if (domainType.getOrganizationType().equals(organizationType)) {
                domainTypes.add(domainType);
            }
        }
        return domainTypes;
    }

    public static boolean isValidByName(OrganizationType organizationType, String domain) {
        if (!ObjectUtils.allNotNull(organizationType) || StringUtility.isEmpty(domain)) {
            return false;
        }

        List<DomainType> domainTypes = findByOrganizationType(organizationType);
        if (CollectionsUtility.isEmpty(domainTypes)) {
            return false;
        }

        List<String> domains = domainTypes.stream().map(domainType -> domainType.getName().toLowerCase()).collect(Collectors.toList());
        return CollectionUtils.containsInstance(domains, StringUtility.trimSingleWhitespace(domain).toLowerCase());
    }

    public static boolean isDomainNameEndWiths(OrganizationType organizationType, String domain) {

        if (!ObjectUtils.allNotNull(organizationType) || StringUtility.isEmpty(domain)) {
            return false;
        }

        List<DomainType> domainTypes = findByOrganizationType(organizationType);
        if (CollectionsUtility.isEmpty(domainTypes)) {
            return false;
        }

        domain = StringUtility.trimSingleWhitespace(domain);
        List<String> domains = domainTypes.stream().map(DomainType::getName).collect(Collectors.toList());

        for (String namespace : domains) {
            if (domain.endsWith(namespace)) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public static boolean isDomainNameEndWiths(List<DomainType> domainTypes, String domain) {

        if (CollectionsUtility.isEmpty(domainTypes) || StringUtility.isEmpty(domain)) {
            return false;
        }
        domain = StringUtility.trimSingleWhitespace(domain);

        List<String> domains = domainTypes.stream().map(DomainType::getName).collect(Collectors.toList());

        for (String namespace : domains) {
            if (domain.endsWith(namespace)) {
                return true;
            } else {
                continue;
            }
        }

        return false;
    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }
}
