package com.example.demo20.service;

import com.example.demo20.model.MenuItem;
import com.example.demo20.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MenuService {
    private final MenuItemRepository repo;
    public MenuService(MenuItemRepository repo) { this.repo = repo; }
    public MenuItem create(MenuItem m) { return repo.save(m); }
    public List<MenuItem> listAll(){ return repo.findAll(); }
}
