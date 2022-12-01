package com.zpp.crowd.service.api;

import com.zpp.crowd.entity.Menu;

import java.util.List;

/**
 * @author : Zpp
 * @Date : 2022/9/9-15:30
 */
public interface MenuService {

    List<Menu> getAll();

    void saveMenu(Menu menu);

    void updateMenu(Menu menu);

    void removeMenuById(Integer id);
}
