package com.project.hierarchy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * This class responds to web requests
 */
@Controller
public class HierarchyController {

    @Autowired
    private HierarchyManager hierarchyManager;

    /**
     * Find paths of matched nodes
     */
    @GetMapping("/query")
    @ResponseBody
    public String query(@RequestParam("created_by") String createdBy) {
        return hierarchyManager.query(createdBy);
    }
}
