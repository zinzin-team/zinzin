package com.fanclub.zinzin.domain.searcher.controller;

import com.fanclub.zinzin.domain.searcher.dto.SearcherResponse;
import com.fanclub.zinzin.domain.searcher.service.SearcherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearcherController {

    private final SearcherService searcherService;

    @GetMapping("/{searchId}")
    public ResponseEntity<SearcherResponse> searchBySaearchId(HttpServletRequest request, @PathVariable(name = "searchId") String searchId){
        return ResponseEntity.ok(searcherService.searchBySearchId(request, searchId));
    }
}