package com.example.mongo.controller;

import com.example.mongo.exception.ResourceNotFoundException;
import com.example.mongo.model.Store;
import com.example.mongo.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/store")
public class StoreController {

    private final StoreRepository storeRepository;

    @GetMapping
    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Store getStoreById(@PathVariable String id) {
        return storeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "id", id));
    }

    @GetMapping("/email/{email}")
    public Store getStoreByEmail(@PathVariable String email) {
        return storeRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Store", "email", email));
    }

    @PostMapping
    public List<Store> addStores(@RequestBody List<Store> storeList) {
        return storeRepository.saveAll(storeList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteById(@PathVariable String id) {
        if(storeRepository.existsById(id)) {
            storeRepository.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    public Page<Store> findStoresBySearchText(@RequestParam(required = false, defaultValue = "") String searchText,
                                              @RequestParam(required = false, defaultValue = "0") Integer page,
                                              @RequestParam(required = false, defaultValue = "1") Integer pageSize) {
        Page<Store> searchResults;
        if(searchText.isEmpty()) {
            Pageable pageable = PageRequest.of(page, pageSize);
            searchResults = storeRepository.findAll(pageable);
        } else {
            Sort sort = Sort.by("score");
            Pageable pageable = PageRequest.of(page, pageSize, sort);
            TextCriteria criteria = TextCriteria.forDefaultLanguage().matching(searchText);

            searchResults = storeRepository.findAllBy(criteria, pageable);
        }

        return searchResults;
    }
}
