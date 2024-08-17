package org.ite.mongodb.controller;

import lombok.RequiredArgsConstructor;
import org.ite.mongodb.service.SearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    /**
     * API endpoint to search across Student and Teacher collections.
     *
     * @param search The search term.
     * @return A list of results from both collections.
     */
    @GetMapping("/search")
    public ResponseEntity<List<Object>> search(@RequestParam String search) {
        List<Object> results = searchService.search(search);
        return ResponseEntity.ok(results);
    }
}
