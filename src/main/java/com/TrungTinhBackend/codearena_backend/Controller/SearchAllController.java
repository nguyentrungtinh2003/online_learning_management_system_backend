package com.TrungTinhBackend.codearena_backend.Controller;

import com.TrungTinhBackend.codearena_backend.Response.APIResponse;
import com.TrungTinhBackend.codearena_backend.Service.Search.SearchAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/searchAll")
public class SearchAllController {

    @Autowired
    private SearchAllService allService;

    @GetMapping("/search")
    public ResponseEntity<APIResponse> searchAll(@RequestParam(required = false) String keyword) throws Exception {
        return ResponseEntity.ok(allService.searchAll(keyword));
    }
}
