package com.karkar.ct.restService.challenge;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.karkar.ct.model.Item;
import org.springframework.core.io.ClassPathResource;
import java.io.IOException;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Provide your implementation of the SearchSvcImpl here.
 * Also annotate your methods with Rest end point wrappers as required in the problem statement
 *
 * You can create any auxiliary classes or interfaces in this package if you need them.
 *
 * Do NOT add annotations as a Bean or Component or Service.   This is being handled in the custom Config class
 * PriceAdjustConfiguration
 */

public class SearchSvcImpl implements SearchSvcInterface {
    private List<Item> itemList;

    @Override
    public void init(String itemPriceJsonFileName) {
        try {
            InputStream inputStream = new ClassPathResource(itemPriceJsonFileName).getInputStream();
            ObjectMapper mapper = new ObjectMapper();
            itemList = mapper.readValue(inputStream, new TypeReference<List<Item>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init(File itemPriceJsonFile) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            itemList = mapper.readValue(itemPriceJsonFile, new TypeReference<List<Item>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Item> getItems() {
        return itemList;
    }

    @Override
    public List<Item> getItems(String category) {
        return itemList.stream()
                .filter(item -> item.getCategory_short_name().equals(category))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> getItem(String itemShortName) {
        return itemList.stream()
                .filter(item -> item.getShort_name().equals(itemShortName))
                .collect(Collectors.toList());
    }
}
