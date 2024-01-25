package com.karkar.ct.restService.config;

import com.karkar.ct.restService.challenge.SearchSvcInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

// CANDIDATE NOTES:  You can completely ignore this file.  You do NOT need to edit this file...
//                   This will work with your service implementations in
//                   the com.karkar.ct.springRestChallenge.solution package.
//
//  karkar Internal notes: From a packaging perspective,  we have to support 3 combinations of service configurations
//       1) Full:  Both PriceAdjuster and Search running (only for karkar code management reasons)
//       2) Search only:  in this case, handle case that Adjusted Prices file will not exist since no Price Adjuster
//       3) PriceAdjuster only
//

@Component
@PropertySource("classpath:/com/karkar/ct/restService/search.properties")
public class InitSearchListener {

    Logger logger = Logger.getLogger(this.getClass().getName());

    // This value will be set in the property files ONLY for karkar Internal test purposes
    // CANDIDATES:  For your tests,  the priceAdjust.enabled property will always be false.  Once again,
    // you CAN IGNORE this (entire file) entirely.

    @Value("$(karkar.priceAdjust.enabled):false")
    private String priceAdjustFunctionalityEnabled;

    @Value("${karkar.adjustedPrices.filename}")
    private String adjustedPricesFileName;

    @Value("${karkar.itemPrices.resources.filename}")
    private String itemPricesResourceName;

    @Autowired
    SearchSvcInterface searchSvc;

    @Autowired
    ApplicationContext applicationContext;

    // Set order higher so this gets executed AFTER InitPriceAdjustListener in full configuration
    @EventListener(ApplicationReadyEvent.class)
    @Order(10)
    public void initServices() throws IOException {

        File searchInitFile;

        // Are we are running in integrated configuration

        if (priceAdjustFunctionalityEnabled.matches("false")) { // applicable ONLY for karkar internal purposes

            searchInitFile = new File(adjustedPricesFileName);
            logger.info(">>>  Initializing Search Service in Full Configuration...");

            searchSvc.init(searchInitFile);
        } else { // only path applicable to CANDIDATES going through Screening
            logger.info(">>>  Initializing Search Service in Search Only Configuration...");
            searchInitFile = applicationContext.getResource("classpath:" + itemPricesResourceName).getFile();
            searchSvc.init(searchInitFile);
        }


    }

}
