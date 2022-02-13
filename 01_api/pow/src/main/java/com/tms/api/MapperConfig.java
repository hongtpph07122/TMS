package com.tms.api;

import com.tms.api.helper.Helper;
import com.tms.entity.CLBasket;
import com.tms.entity.CLFresh;
import com.tms.entity.SaleOrder;
import com.tms.entity.log.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {
    @Bean
    ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true);
        PropertyMap<CLFresh, InsSaleOrder> cLFreshToInsSaleOrderMap = new PropertyMap<CLFresh, InsSaleOrder>() {
            @Override
            protected void configure() {
                map().setLeadName(source.getName());
                map().setLeadPhone(source.getPhone());
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setCreateby(null);
                skip().setModifyby(null);
            }
        };

        PropertyMap<CLBasket, InsCLFreshV3> cLBasketToInFreshV3Map = new PropertyMap<CLBasket, InsCLFreshV3>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);
                map().setAttribute(source.getAttribute2());
            }
        };
        PropertyMap<CLBasket, InsCLInActiveV3> cLBasketToInsCLActiveMap = new PropertyMap<CLBasket, InsCLInActiveV3>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);
                map().setAttribute(source.getAttribute2());
                map().setClickId(source.getClickId());
                map().setLeadStatus(source.getStatus());
            }
        };

        PropertyMap<CLFresh, InsSaleOrderV2> cLFreshToInsSaleOrderV2Map = new PropertyMap<CLFresh, InsSaleOrderV2>() {
            @Override
            protected void configure() {
                map().setLeadName(source.getName());
                map().setLeadPhone(source.getPhone());
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setCreateby(null);
                skip().setModifyby(null);
            }
        };

        PropertyMap<CLFresh, UpdSaleOrder> cLFreshToUpdSaleOrderMap = new PropertyMap<CLFresh, UpdSaleOrder>() {
            @Override
            protected void configure() {
                map().setLeadName(source.getName());
                map().setLeadPhone(source.getPhone());
                skip().setModifyby(null);
            }
        };
        PropertyMap<CLFresh, UpdSaleOrderV2> cLFreshToUpdSaleOrderV2Map = new PropertyMap<CLFresh, UpdSaleOrderV2>() {
            @Override
            protected void configure() {
                map().setLeadName(source.getName());
                map().setLeadPhone(source.getPhone());
                skip().setModifyby(null);
            }
        };

        PropertyMap<CLFresh, UpdLead> cLFreshToUpdLead = new PropertyMap<CLFresh, UpdLead>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);
                //TODO comment 2 line after if bussiness change
                skip().setNextCallTime(null);
                skip().setLastCallTime(null);
            }
        };
        PropertyMap<CLFresh, UpdLeadV4> cLFreshToUpdLeadV4 = new PropertyMap<CLFresh, UpdLeadV4>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);
                //TODO comment 2 line after if bussiness change
                skip().setNextCallTime(null);
                skip().setLastCallTime(null);
            }
        };
        PropertyMap<CLFresh, UpdLeadV5> cLFreshToUpdLeadV5 = new PropertyMap<CLFresh, UpdLeadV5>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);
                //TODO comment 2 line after if bussiness change
                skip().setNextCallTime(null);
                skip().setLastCallTime(null);
            }
        };
        PropertyMap<com.tms.entity.CLBasket, com.tms.entity.log.UpdCLBasket> cLBasketToUpdCLBasketMap = new PropertyMap<com.tms.entity.CLBasket, com.tms.entity.log.UpdCLBasket>() {
            @Override
            protected void configure() {
                skip().setModifydate(null);
                skip().setCreatedate(null);
                skip().setModifyby(null);

            }
        };

        PropertyMap<SaleOrder, UpdSaleOrder> saleOrderToUpdSaleOrderMap = new PropertyMap<SaleOrder, UpdSaleOrder>() {
            @Override
            protected void configure() {
                skip().setModifyby(null);
            }
        };

        PropertyMap<SaleOrder, UpdSaleOrderV2> saleOrderToUpdSaleOrderV2Map = new PropertyMap<SaleOrder, UpdSaleOrderV2>() {
            @Override
            protected void configure() {
                skip().setModifyby(null);
            }
        };

        PropertyMap<CLFresh, InsMktData> clFreshInsMktDataPropertyMap = new PropertyMap<CLFresh, InsMktData>() {
            @Override
            protected void configure() {
                map().setCampaignName(source.getCampaignName());
                map().setProductCode(Helper.toString(source.getProdId()));
                map().setProductName(source.getProdName());
                map().setCustName(source.getName());
                map().setCustPhone(source.getPhone());
                map().setCustArea(Helper.IntegerTryParse(source.getProvince()));
                skip().setCreatedate(null);
            }
        };

        PropertyMap<InsCLFreshV10, InsMktData> insCLFreshV10InsMktDataPropertyMap = new PropertyMap<InsCLFreshV10, InsMktData>() {
            @Override
            protected void configure() {
                map().setProductCode(Helper.toString(source.getProdId()));
                map().setProductName(source.getProdName());
                map().setCustName(source.getName());
                map().setCustPhone(source.getPhone());
                map().setCustArea(Helper.IntegerTryParse(source.getProvince()));
                skip().setCreatedate(null);
            }
        };

        PropertyMap<CLFresh, UpdMktData> clFreshUpdMktDataPropertyMap = new PropertyMap<CLFresh, UpdMktData>() {
            @Override
            protected void configure() {
                map().setCampaignName(source.getCampaignName());
                map().setProductCode(Helper.toString(source.getProdId()));
                map().setProductName(source.getProdName());
                map().setCustName(source.getName());
                map().setCustPhone(source.getPhone());
                map().setCustArea(Helper.IntegerTryParse(source.getProvince()));
            }
        };

        modelMapper.addMappings(cLFreshToInsSaleOrderMap);
        modelMapper.addMappings(cLFreshToInsSaleOrderV2Map);
        modelMapper.addMappings(cLFreshToUpdSaleOrderMap);
        modelMapper.addMappings(saleOrderToUpdSaleOrderV2Map);
        modelMapper.addMappings(cLFreshToUpdSaleOrderV2Map);
        modelMapper.addMappings(cLFreshToUpdLead);
        modelMapper.addMappings(saleOrderToUpdSaleOrderMap);
        modelMapper.addMappings(cLFreshToUpdLeadV4);
        modelMapper.addMappings(cLFreshToUpdLeadV5);
        modelMapper.addMappings(cLBasketToInFreshV3Map);
        modelMapper.addMappings(cLBasketToInsCLActiveMap);
        modelMapper.addMappings(clFreshInsMktDataPropertyMap);
        modelMapper.addMappings(insCLFreshV10InsMktDataPropertyMap);
        modelMapper.addMappings(clFreshUpdMktDataPropertyMap);
        return modelMapper;
    }
}
