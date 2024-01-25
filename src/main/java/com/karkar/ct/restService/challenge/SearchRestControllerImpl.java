package com.karkar.ct.restService.challenge;

import com.karkar.ct.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 * This controller needs to expose the following rest endpoints.  You need to fill in the implementation here
 *
 * Required REST Endpoints
 *
 *      /item                       Get all items
 *      /item?category=C            Get all items in category specified by Category shortName
 *      /item/{itemShortName}       Get item that matches the specified Item shortName
 */

@Profile("default")
@RestController
public class SearchRestControllerImpl {
    private final SearchSvcInterface searchService;

    @Autowired
    public SearchRestControllerImpl(SearchSvcInterface searchService) {
        this.searchService = searchService;
    }

    // Get all items or items by category if the 'category' request parameter is provided
    @GetMapping("/item")
    public ResponseEntity<List<Item>> getAllItems(@RequestParam(value = "category", required = false) String category) {
        if (category != null && !category.trim().isEmpty()) {
            List<Item> itemsByCategory = searchService.getItems(category);
            return ResponseEntity.ok(itemsByCategory);
        } else {
            List<Item> allItems = searchService.getItems();
            return ResponseEntity.ok(allItems);
        }
    }

    // Get single item by short name
    @GetMapping("/item/{itemShortName}")
    public ResponseEntity<Item> getItemByShortName(@PathVariable("itemShortName") String itemShortName) {
        List<Item> items = searchService.getItem(itemShortName);
        if (items != null && !items.isEmpty()) {
            // Assuming that short names are unique and there should only be one item with a given short name.
            return ResponseEntity.ok(items.get(0));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
