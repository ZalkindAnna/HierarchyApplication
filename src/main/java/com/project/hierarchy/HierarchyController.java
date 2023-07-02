package com.project.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HierarchyController {

    @Autowired
    private HierarchyManager hierarchyManager;

    @GetMapping("/query")
    @ResponseBody
    public String query(@RequestParam("created_by") String createdBy) {
        return hierarchyManager.query(createdBy);
    }
}
